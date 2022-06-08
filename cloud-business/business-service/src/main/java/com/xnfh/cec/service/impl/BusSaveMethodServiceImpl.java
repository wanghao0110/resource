package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusResourceTypeRepository;
import com.xnfh.cec.repository.BusSaveMethodRepository;
import com.xnfh.cec.service.BusResourceTypeService;
import com.xnfh.cec.service.BusSaveMethodService;
import com.xnfh.entity.BusResourceType;
import com.xnfh.entity.BusSaveMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@Service
@Slf4j
public class BusSaveMethodServiceImpl implements BusSaveMethodService {


    @Autowired
    private BusSaveMethodRepository busSaveMethodRepository;


    @Override
    public List<BusSaveMethod> findBusSaveMethodList() {
        return busSaveMethodRepository.findAll();
    }
}