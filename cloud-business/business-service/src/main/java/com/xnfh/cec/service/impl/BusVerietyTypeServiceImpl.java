package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusVerietyRepository;
import com.xnfh.cec.service.BusVerietyTypeService;
import com.xnfh.entity.BusVarietyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/25
 */
@Service
@Slf4j
public class BusVerietyTypeServiceImpl implements BusVerietyTypeService {


    @Autowired
    private BusVerietyRepository busVerietyRepository;

    @Override
    public List<BusVarietyType> findBusVerietyList() {
        return busVerietyRepository.findAll();
    }
}
