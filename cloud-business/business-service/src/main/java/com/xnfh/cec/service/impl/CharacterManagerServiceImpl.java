package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusCharacterManagerRespository;
import com.xnfh.cec.repository.BusCharacterTypeRepository;
import com.xnfh.cec.repository.specification.CharacterManagerSpecification;
import com.xnfh.cec.service.CharacterManagerService;
import com.xnfh.entity.BusCharacterManager;
import com.xnfh.entity.BusCharacterType;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import com.xnfh.vo.CharacterManagerVo;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/6
 */
@Service
@Slf4j
public class CharacterManagerServiceImpl implements CharacterManagerService {

    @Autowired
    private BusCharacterManagerRespository busCharacterManagerRespository;

    @Autowired
    private BusCharacterTypeRepository busCharacterTypeRepository;


    @Override
    public Page<BusCharacterManager> getCharacterManagerPage(CharacterManagerVo characterManagerVo, Pageable pageable) {
        //如果遇到findAll(specification,pageable);是红色，记住把repsoitory里面继承-JpaSpecificationExecutor<BusPlantResource>即可
        Page<BusCharacterManager> busCharacterManagerRespositoryAll = busCharacterManagerRespository.findAll(CharacterManagerSpecification.characterManagerSpecification(characterManagerVo),pageable);
        if(busCharacterManagerRespositoryAll.getContent()!=null && busCharacterManagerRespositoryAll.getContent().size()>0){
            busCharacterManagerRespositoryAll.getContent().forEach(busCharacterManager -> {
                //获取性状类型
                Integer characterTypeId = busCharacterManager.getCharacterTypeId();
                BusCharacterType busCharacterDb = busCharacterTypeRepository.findBusCharacterTypeByCharacterTypeId(characterTypeId);
                if(busCharacterDb != null){
                    busCharacterManager.setCharacterTypeName(busCharacterDb.getCharacterTypeName());
                }
            });
        }

        //共计的个数
        return new PageImpl<BusCharacterManager>(busCharacterManagerRespositoryAll.getContent(),pageable,busCharacterManagerRespositoryAll.getTotalElements());
    }

    /**
     * 添加性状名称
     * @param busCharacterManager
     */
    @Override
    public void addCharacterManager(BusCharacterManager busCharacterManager) {
        log.info("CharacterManagerServiceImpl busCharacterManager",busCharacterManager);
        checkCharacterManagerParams(busCharacterManager);
        busCharacterManager.setIsDelete(0);
        //查询最大的maxNum
        Integer orderNum = busCharacterManagerRespository.selectMaxOrderNum();
        busCharacterManager.setOrderNum(orderNum+1);
        busCharacterManager.setUpdateTime(new Date());
        busCharacterManager.setCreateTime(new Date());
        busCharacterManagerRespository.saveAndFlush(busCharacterManager);
    }

    /**
     * 执行编辑性状前的查询名称操作
     * @param characterTypeId
     * @return
     */
    @Override
    public BusCharacterManager updatePreCharacterManager(Integer characterTypeId) {
        BusCharacterManager busCharacterManagerDb = busCharacterManagerRespository.getOne(characterTypeId);
        return busCharacterManagerDb;
    }

    /**
     *执行编辑性状的操作
     * @param busCharacterManager
     */
    @Override
    public void updateCharacterManager(BusCharacterManager busCharacterManager) {
        log.info("CharacterManagerServiceImpl busCharacterManager",busCharacterManager);
        BusCharacterManager busCharacterManagerDb =  busCharacterManagerRespository.findBusCharacterManagerByCharacterManagerId(busCharacterManager.getCharacterManagerId());
        if(busCharacterManagerDb!=null){
            checkCharacterManagerParams(busCharacterManager);
        }
        if(busCharacterManager.getCharacterTypeId()!=null){
            BusCharacterType busCharacterTypeByCharacterTypeId = busCharacterTypeRepository.findBusCharacterTypeByCharacterTypeId(busCharacterManager.getCharacterTypeId());
            busCharacterManagerDb.setCharacterTypeName(busCharacterTypeByCharacterTypeId.getCharacterTypeName());
        }
        busCharacterManagerDb.setUpdateTime(new Date());
        busCharacterManagerDb.setCharacterName(busCharacterManager.getCharacterName());
        busCharacterManagerDb.setAddress(busCharacterManager.getAddress());
        busCharacterManager.setMaxNum(busCharacterManager.getMaxNum());
        busCharacterManagerDb.setMinNum(busCharacterManager.getMinNum());
        busCharacterManagerRespository.saveAndFlush(busCharacterManagerDb);

    }

    /**
     * 批量删除接口，逻辑删除
     * @param ids
     */
    @Override
    public void batchDelete(List<Integer> ids) {
        if(ids.size()!=0){
            ids.forEach(id->{
                BusCharacterManager busCharacterManagerDb = busCharacterManagerRespository.getOne(id);
                if(busCharacterManagerDb == null){
                    throw new ApiException(ExceptionDefinition.GET_CHARACTER_MANAGER_IS_NULL_4088);
                }
                busCharacterManagerDb.setIsDelete(1);
                busCharacterManagerDb.setUpdateTime(new Date());
                busCharacterManagerRespository.saveAndFlush(busCharacterManagerDb);
            });
        }
    }

    /**
     * 性状名称管理上移动
     *
     * @param busCharacterManager
     */
    @Override
    public void updateUp(BusCharacterManager busCharacterManager) {
        log.info("CharacterManagerServiceImpl busCharacterManager",busCharacterManager);
        Integer characterManagerId = busCharacterManager.getCharacterManagerId();
        if(characterManagerId != null){
            BusCharacterManager busCharacterManagerDb = busCharacterManagerRespository.findBusCharacterManagerByCharacterManagerId(characterManagerId);
            if(busCharacterManagerDb != null){
                busCharacterManagerDb.setOrderNum(busCharacterManagerDb.getOrderNum()-1);
                busCharacterManagerDb.setUpdateTime(new Date());
                busCharacterManagerRespository.saveAndFlush(busCharacterManagerDb);
            }
            BusCharacterManager busCharacterManagerPre = busCharacterManagerRespository.findBusCharacterManagerByOrderNum(busCharacterManager.getOrderNum()-1);
            if(busCharacterManagerPre != null){
                busCharacterManagerPre.setOrderNum(busCharacterManagerPre.getOrderNum()+1);
                busCharacterManagerPre.setUpdateTime(new Date());
                busCharacterManagerRespository.saveAndFlush(busCharacterManagerPre);
            }
        }
    }


    /**
     * 性状名称管理下移动
     *
     * @param busCharacterManager
     */
    @Override
    public void updateDown(BusCharacterManager busCharacterManager) {
        log.info("CharacterManagerServiceImpl busCharacterManager",busCharacterManager);
        Integer characterManagerId = busCharacterManager.getCharacterManagerId();
        if(characterManagerId != null){
            BusCharacterManager busCharacterManagerDb = busCharacterManagerRespository.findBusCharacterManagerByCharacterManagerId(characterManagerId);
            if(busCharacterManagerDb != null){
                busCharacterManagerDb.setOrderNum(busCharacterManagerDb.getOrderNum()+1);
                busCharacterManagerDb.setUpdateTime(new Date());
                busCharacterManagerRespository.saveAndFlush(busCharacterManagerDb);
            }
            BusCharacterManager busCharacterManagerNext = busCharacterManagerRespository.findBusCharacterManagerByOrderNum(busCharacterManager.getOrderNum()+1);
            if(busCharacterManagerNext != null){
                busCharacterManagerNext.setOrderNum(busCharacterManagerNext.getOrderNum()-1);
                busCharacterManagerNext.setUpdateTime(new Date());
                busCharacterManagerRespository.saveAndFlush(busCharacterManagerNext);
            }
        }
    }

    /**
     * 校验参数
     * @param busCharacterManager
     */
    private void checkCharacterManagerParams(BusCharacterManager busCharacterManager) {
        if(StringUtils.isEmpty(busCharacterManager.getCharacterName())){
            throw new ApiException(ExceptionDefinition.GET_CHARACTER_MANAGER_NAME_IS_NULL_4085);
        }
        if(StringUtils.isEmpty(busCharacterManager.getInputType())){
            throw new ApiException(ExceptionDefinition.GET_CHARACTER_MANAGER_INPUTTYPE_IS_NULL_4086);
        }
        if(busCharacterManager.getAddress()==null && busCharacterManager.getAddress().length()>100){
            throw new ApiException(ExceptionDefinition.GET_CHARACTER_MANAGER_ADDRESS_IS_NULL_OR_LENGTH_TOO_LONG_4087);
        }
        if(busCharacterManager.getMinNum()<0){
            busCharacterManager.setMinNum(0);
        }
    }

}
