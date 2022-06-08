package com.xnfh.cec.service;

import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/31
 */
public interface SysUserPersonalService {

    /**
     * 分页+条件查询个人列表
     * @param sysUserVo
     * @param pageRequest
     * @return
     */
    Page<SysUser> listSysUserPersonal(SysUserVo sysUserVo, Pageable pageRequest);

    /**
     * 查询用户详情数据
     * @param id
     * @return
     */
    SysUser viewSysUserById(Integer id);

    /**
     * 修改个人用户的禁用/启用状态
     * @param sysUser
     * @return
     */
    SysUser updateStatus(SysUser sysUser);
}
