package com.xnfh.cec.service;

import com.xnfh.entity.BusSaveExt;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
public interface BusSaveExtService {

    /**
     * 查询保存方式
     * @return
     */
    List<BusSaveExt> findBusSaveExtList();
}
