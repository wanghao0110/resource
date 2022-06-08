package com.xnfh.cec.service;

import com.xnfh.common.ApiResponse;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserUnitVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/31
 */
public interface SysUserUnitService {

    /**
     * 分页+提交查询用人单位审核列表
     * @param sysUserUnitVo
     * @param pageRequest
     * @return
     */
    Page<SysUser> getUnitList(SysUserUnitVo sysUserUnitVo, Pageable pageRequest);

    /**
     * 查询单位详情前的操作
     * @param userId
     * @return
     */
    SysUser getUnitById(Integer userId);

    /**
     * 修改单位的禁用/启用状态
     * @param userId
     * @param status
     */
    void updateStatus(Integer userId, Integer status);

    /**
     * 对用人单位的审核
     * @param userId
     * @param auditStatus
     * @param reason
     */
    void updateAuditStatus(Integer userId, Integer auditStatus,String reason);
}
