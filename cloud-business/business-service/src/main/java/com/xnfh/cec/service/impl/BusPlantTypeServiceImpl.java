package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusPlantTypeRepository;
import com.xnfh.cec.repository.specification.ExpertPlantSpecification;
import com.xnfh.cec.repository.specification.PlantTypeSpecification;
import com.xnfh.cec.service.BusPlantTypeService;
import com.xnfh.entity.*;
import com.xnfh.entity.vo.SysUserUnitVo;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@Service
@Slf4j
public class BusPlantTypeServiceImpl implements BusPlantTypeService {

    @Autowired
    private BusPlantTypeRepository busPlantTypeRepository;

    /**
     *查询作物等级资源的作物类型
     * @return
     */
    @Override
    public List<BusPlantType> findBusPlantTypeList() {
        log.info("current operate data search busPlantType");
        return busPlantTypeRepository.findAll();
    }

    @Override
    public String getPlantTypeForName(Integer plantTypeId) {
        return busPlantTypeRepository.findBusPlantTypeByPlantTypeId(plantTypeId);
    }

    /**
     * 查询作物类型
     * @param plantTypeName
     * @param pageable
     * @return
     */
    @Override
    public Page<BusPlantType> plantTypeList(String plantTypeName, Pageable pageable) {

        //如果遇到findAll(specification,pageable);是红色，记住把repsoitory里面继承-JpaSpecificationExecutor<BusPlantResource>即可
        Page<BusPlantType> busPlantTypeRepositoryAll = busPlantTypeRepository.findAll(PlantTypeSpecification.plantTypeSpecification(plantTypeName),pageable);
        //共计的个数
        return new PageImpl<BusPlantType>(busPlantTypeRepositoryAll.getContent(),pageable,busPlantTypeRepositoryAll.getTotalElements());
    }


    /**
     * 新增作物类型
     * @param busPlantType
     * @return
     */
    @Override
    public BusPlantType externalAdd(BusPlantType busPlantType) {
        Assert.notNull(busPlantType,"current operate data busPlantType");
        BusPlantType plantType = new BusPlantType();
        if(StringUtils.isEmpty(busPlantType.getTypeName())){
            throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_ADD_PLANT_TYPE_TYPE_NAME_IS_NULL_4078);
        }else{
            if(busPlantType.getTypeName().length()>10){
                throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_BUS_PLANTTYPE_NAME_TOO_LONG_4080);
            }else{
                int count = busPlantTypeRepository.findBusPlantTypeByTypeName(busPlantType.getTypeName());
                if(count>=1){
                    throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_BUS_PLANTTYPE_NAME_IS_EXIST_4081);
                }else{
                    plantType.setTypeName(busPlantType.getTypeName());
                }
            }
        }
        plantType.setCreateTime(new Date());
        Integer orderNum = busPlantTypeRepository.findBusPlantTypeByMax();
        plantType.setOrderNum(orderNum + 1);
        plantType.setUpdateTime(new Date());
        plantType.setTypeName(busPlantType.getTypeName());
        plantType.setRemark(busPlantType.getTypeName());
        return busPlantTypeRepository.saveAndFlush(plantType);
    }

    /**
     * 编辑作物类型
     * @param busPlantType
     */
    @Override
    public void externalUpdate(BusPlantType busPlantType) {
        log.info("current search busPlantType typeName:{}",busPlantType.getTypeName());
        //修改前查询数据是否存在
        BusPlantType busPlantTypeDb = busPlantTypeRepository.getOne(busPlantType.getPlantTypeId());
        if(busPlantTypeDb == null){
            throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_SEARCH_PLANT_TYPE_IS_NULL_4079);
        }
        busPlantTypeDb.setUpdateTime(new Date());
        busPlantTypeDb.setTypeName(busPlantType.getTypeName());
        busPlantTypeRepository.saveAndFlush(busPlantTypeDb);
    }

    /**
     * 查询作物类型信息
     * @param plantTypeId
     * @return
     */
    @Override
    public BusPlantType findPlantTypeByPlantId(Integer plantTypeId) {
        log.info("current search plantType id:{}",plantTypeId);
        return busPlantTypeRepository.getOne(plantTypeId);
    }

    /**
     * 批量删除作物类型信息
     * @param ids
     */
    @Override
    @Transactional
    public void batchDelete(List<Integer> ids) {
        log.info("current operate  ids:{}",ids);
        List<BusPlantType> allById = busPlantTypeRepository.findAllById(ids);
        if(!CollectionUtils.isEmpty(allById)){
            busPlantTypeRepository.deleteChoice(ids);
        }
    }

    /**
     * 作物类型对象上移动
     * @param busPlantType
     */
    @Override
    @Transactional
    public void updateUp(BusPlantType busPlantType) {
        //1.先查询是否存在于数据库
         BusPlantType busPlantTypeDb = busPlantTypeRepository.findByPlantTypeId(busPlantType.getPlantTypeId());
         if(busPlantTypeDb!=null){
             //如果是上移，查询上一条记录
             //本条记录的order号码
             Integer orderNum = busPlantType.getOrderNum();
             busPlantTypeDb.setOrderNum(orderNum-1);
             busPlantTypeRepository.saveAndFlush(busPlantTypeDb);
             BusPlantType busPlantTypeDbForUp = busPlantTypeRepository.findBusPlantTypeByOrderNum(orderNum-1);
             busPlantTypeDbForUp.setOrderNum(orderNum+1);
             busPlantTypeRepository.saveAndFlush(busPlantTypeDbForUp);
         }

    }

    /**
     * 作物类型对象下移动
     * @param busPlantType
     */
    @Override
    @Transactional
    public void updateDown(BusPlantType busPlantType) {
        //1.先查询是否存在于数据库
        BusPlantType busPlantTypeDb = busPlantTypeRepository.findByPlantTypeId(busPlantType.getPlantTypeId());
        if(busPlantTypeDb!=null){
            //如果是上移，查询上一条记录
            //本条记录的order号码
            Integer orderNum = busPlantType.getOrderNum();
            busPlantTypeDb.setOrderNum(orderNum+1);
            busPlantTypeRepository.saveAndFlush(busPlantTypeDb);
            BusPlantType busPlantTypeDbForUp = busPlantTypeRepository.findBusPlantTypeByOrderNum(orderNum+1);
            busPlantTypeDbForUp.setOrderNum(orderNum-1);
            busPlantTypeRepository.saveAndFlush(busPlantTypeDbForUp);
        }
    }
}
