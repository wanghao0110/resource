package com.xnfh.cec.service;

import com.xnfh.entity.BusSaveMethod;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
public interface BusSaveMethodService {
    /**
     * 查询保存方法
     * @return
     */
    List<BusSaveMethod> findBusSaveMethodList();
}
