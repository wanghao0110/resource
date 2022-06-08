package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusCharacterTypeRepository;
import com.xnfh.cec.repository.specification.CharacterSpecification;
import com.xnfh.cec.repository.specification.UnitSpecification;
import com.xnfh.cec.service.BusCharacterTypeService;
import com.xnfh.entity.BusCharacterType;
import com.xnfh.entity.SysUser;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import com.xnfh.vo.BusCharacterTypeVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/2
 */

@Service
@Slf4j
public class BusCharacterTypeServiceImpl implements BusCharacterTypeService {

    @Autowired
    private BusCharacterTypeRepository busCharacterTypeRepository;


    @Override
    public Page<BusCharacterType> getCharacterTypePage(BusCharacterTypeVo busCharacterTypeVo, Pageable pageRequest) {

        Page<BusCharacterType> busCharacterTypeRepositoryAll = busCharacterTypeRepository.findAll(CharacterSpecification.characterSpecification(busCharacterTypeVo),pageRequest);
        //共计的个数
        return new PageImpl<BusCharacterType>(busCharacterTypeRepositoryAll.getContent(),pageRequest,busCharacterTypeRepositoryAll.getTotalElements());
    }

    /**
     * 添加性状信息
     * @param busCharacterType
     */
    @Override
    public BusCharacterType addCharacterType(BusCharacterType busCharacterType) {
        String characterTypeName = busCharacterType.getCharacterTypeName();
        if(StringUtils.isEmpty(characterTypeName)){
            throw new ApiException(ExceptionDefinition.GET_CHARACTER_TYPE_NAME_IS_NULL_4082);
        }
        BusCharacterType busCharacterTypeDb  = busCharacterTypeRepository.findBusCharacterTypeByCharacterTypeName(characterTypeName);
        if(busCharacterTypeDb != null){
            throw new ApiException(ExceptionDefinition.GET_CHARACTER_TYPE_NAME_IS_ALREADY_EXIST_4083);
        }
        //查询最大的orderNum数
        Integer orderNum = busCharacterTypeRepository.selectMaxorderNum();
        busCharacterType.setCreateTime(new Date());
        busCharacterType.setUpdateTime(new Date());
        busCharacterType.setIsDelete(0);
        busCharacterType.setOrderNum(orderNum+1);
        return busCharacterTypeRepository.saveAndFlush(busCharacterType);
    }

    /**
     * 查询形状类型的修改前的操作
     * @param id
     * @return
     */
    @Override
    public BusCharacterType viewCharacterById(Integer id) {
        log.info("current search id :{}" , id);
        return busCharacterTypeRepository.getOne(id);
    }

    /**
     * 修改性状类型接口后端接口
     * @param busCharacterType
     */
    @Override
    @Transactional
    public void updateCharacterType(BusCharacterType busCharacterType) {
        if(StringUtils.isEmpty(busCharacterType.getCharacterTypeName())){
            throw new ApiException(ExceptionDefinition.GET_CHARACTER_TYPE_NAME_IS_NULL_4082);
        }
        BusCharacterType characterTypeDb = busCharacterTypeRepository.getOne(busCharacterType.getCharacterTypeId());

        if(characterTypeDb.equals(busCharacterType.getCharacterTypeName())){
            characterTypeDb.setUpdateTime(new Date());
            busCharacterTypeRepository.saveAndFlush(characterTypeDb);
        }else{
            BusCharacterType busCharacterTypeByCharacterTypeName = busCharacterTypeRepository.findBusCharacterTypeByCharacterTypeName(busCharacterType.getCharacterTypeName());
            if(busCharacterTypeByCharacterTypeName!=null){
                throw new ApiException(ExceptionDefinition.GET_CHARACTER_TYPE_NAME_IS_ALREADY_EXIST_4083);
            }else{
                characterTypeDb.setUpdateTime(new Date());
                characterTypeDb.setCharacterTypeName(busCharacterType.getCharacterTypeName());
                busCharacterTypeRepository.saveAndFlush(characterTypeDb);
            }
        }

    }

    /**
     * 批量删除性状类型接口
     * @param ids
     */
    @Override
    @Transactional
    public void batchUpdateCharacterType(List<Integer> ids) {
        log.info("current operate need delete data ids:{}",ids);
        ids.forEach(id->{
            BusCharacterType characterTypeDb = busCharacterTypeRepository.getOne(id);
            if(characterTypeDb == null){
                throw new ApiException(ExceptionDefinition.GET_CHARACTER_TYPE_NAME_IS_NULL_4082);
            }
            characterTypeDb.setIsDelete(1);
            characterTypeDb.setUpdateTime(new Date());
            busCharacterTypeRepository.saveAndFlush(characterTypeDb);
        });
    }

    /**
     * 修改性状类型的上移
     * @param busCharacterType
     */
    @Override
    @Transactional
    public void updateUp(BusCharacterType busCharacterType) {
        log.info("busCharacterTypeService busCharacterType id :{}", busCharacterType.getCharacterTypeId());
        BusCharacterType busCharacterTypeDb = busCharacterTypeRepository.findBusCharacterTypeByCharacterTypeId(busCharacterType.getCharacterTypeId());
        if(busCharacterTypeDb != null){
            busCharacterTypeDb.setOrderNum(busCharacterType.getOrderNum()-1);
            busCharacterTypeDb.setUpdateTime(new Date());
            busCharacterTypeRepository.saveAndFlush(busCharacterTypeDb);
        }
        //查询上一条记录
        BusCharacterType busCharacterTypeByCharacterTypeId = busCharacterTypeRepository.findBusCharacterTypeByCharacterTypeId(busCharacterType.getCharacterTypeId() - 1);
        if(busCharacterTypeByCharacterTypeId != null){
            busCharacterTypeByCharacterTypeId.setOrderNum(busCharacterTypeByCharacterTypeId.getOrderNum()+1);
            busCharacterTypeByCharacterTypeId.setUpdateTime(new Date());
            busCharacterTypeRepository.saveAndFlush(busCharacterTypeByCharacterTypeId);
        }
    }

    /**
     * 性状类型下移动
     * @param busCharacterType
     */
    @Override
    @Transactional
    public void updateDown(BusCharacterType busCharacterType) {
        log.info("busCharacterTypeService busCharacterType id :{}", busCharacterType.getCharacterTypeId());
        BusCharacterType busCharacterTypeDb = busCharacterTypeRepository.findBusCharacterTypeByCharacterTypeId(busCharacterType.getCharacterTypeId());
        if(busCharacterTypeDb != null){
            busCharacterTypeDb.setOrderNum(busCharacterType.getOrderNum()+1);
            busCharacterTypeDb.setUpdateTime(new Date());
            busCharacterTypeRepository.saveAndFlush(busCharacterTypeDb);
        }
        //查询下一条记录
        BusCharacterType busCharacterTypeByCharacterTypeId = busCharacterTypeRepository.findBusCharacterTypeByCharacterTypeId(busCharacterType.getCharacterTypeId() + 1);
        if(busCharacterTypeByCharacterTypeId != null){
            busCharacterTypeByCharacterTypeId.setOrderNum(busCharacterTypeByCharacterTypeId.getOrderNum()-1);
            busCharacterTypeByCharacterTypeId.setUpdateTime(new Date());
            busCharacterTypeRepository.saveAndFlush(busCharacterTypeByCharacterTypeId);
        }
    }

}
