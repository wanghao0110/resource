package com.xnfh.cec.service;

import com.xnfh.entity.BusPlantResource;
import com.xnfh.vo.CancelRegisterVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/7
 */
public interface BusCancelRegisterAuditService {

    /**
     * 撤销登记审核分页查询
     * @param cancelRegisterVo
     * @param pageRequest
     * @return
     */
    Page<BusPlantResource> findCharacterManagerPage(CancelRegisterVo cancelRegisterVo, Pageable pageRequest);

    /**
     * 审核通过，修改状态
     * @param busPlantResource
     */
    void auditSuccess(BusPlantResource busPlantResource);
}
