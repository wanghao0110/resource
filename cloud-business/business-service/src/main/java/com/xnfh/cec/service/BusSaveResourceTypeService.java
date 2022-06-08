package com.xnfh.cec.service;

import com.xnfh.entity.BusSaveResourceType;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
public interface BusSaveResourceTypeService {
    /**
     * 查询保存资源类型表
     * @return
     */
    List<BusSaveResourceType> findBusSavePlantTypeList();

    /**
     * 新增资源保存类型
     * @param busSaveResourceType
     * @return
     */
    BusSaveResourceType findBusSaveResourceTypeList(BusSaveResourceType busSaveResourceType);
}
