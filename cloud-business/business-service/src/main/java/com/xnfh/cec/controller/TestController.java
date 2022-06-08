package com.xnfh.cec.controller;

import com.xnfh.cec.repository.BusPlantTypeRepository;
import com.xnfh.entity.BusPlantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@Component
public class TestController {

    @Autowired
    private BusPlantTypeRepository busPlantType;


    public void test(){
        System.out.println(1111);
        List<BusPlantType> all = busPlantType.findAll();
        System.out.println(all.size());

    }
}
