package com.xnfh.cec.service;

import com.xnfh.entity.BusResourceType;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
public interface BusResourceTypeService {

    /**
     * 查询资源类型
     * @return
     */
    List<BusResourceType> findBusPlantResourceTypeList();
}
