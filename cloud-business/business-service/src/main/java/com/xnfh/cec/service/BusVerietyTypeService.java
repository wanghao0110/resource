package com.xnfh.cec.service;

import com.xnfh.entity.BusVarietyType;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/25
 */
public interface BusVerietyTypeService {

    /**
     * 查询种质类型集合
     * @return
     */
    List<BusVarietyType> findBusVerietyList();
}
