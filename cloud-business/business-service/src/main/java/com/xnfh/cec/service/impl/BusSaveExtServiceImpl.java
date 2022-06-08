package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusSaveExtRepository;
import com.xnfh.cec.service.BusSaveExtService;
import com.xnfh.entity.BusSaveExt;
import io.swagger.annotations.ApiImplicitParam;
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
public class BusSaveExtServiceImpl implements BusSaveExtService {

    @Autowired
    private BusSaveExtRepository busSaveExtRepository;

    @Override
    public List<BusSaveExt> findBusSaveExtList() {
        return busSaveExtRepository.findAll();
    }
}
