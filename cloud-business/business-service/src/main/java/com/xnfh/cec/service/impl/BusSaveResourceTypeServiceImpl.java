package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusSaveResourceTypeRepository;
import com.xnfh.cec.service.BusSaveResourceTypeService;
import com.xnfh.entity.BusSaveResourceType;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */

@Service
@Slf4j
public class BusSaveResourceTypeServiceImpl implements BusSaveResourceTypeService {

    @Autowired
    private BusSaveResourceTypeRepository busSaveResourceTypeRepository;

    @Override
    public List<BusSaveResourceType> findBusSavePlantTypeList() {
        return busSaveResourceTypeRepository.findAll();
    }

    @Override
    public BusSaveResourceType findBusSaveResourceTypeList(BusSaveResourceType busSaveResourceType) {
        busSaveResourceType.setCreateTime(new Date());
        //可用唯一索引代替下面代码
        List<BusSaveResourceType> all = busSaveResourceTypeRepository.findAll();
        for (BusSaveResourceType saveResourceType : all) {
            if(saveResourceType.getSaveResourceName().equals(busSaveResourceType.getSaveResourceName())){
                throw new ApiException(ExceptionDefinition.INSERT_SAVE_RESOURCE_TYPE_EXIST_4012);
            }
        }
        return busSaveResourceTypeRepository.save(busSaveResourceType);
    }
}
