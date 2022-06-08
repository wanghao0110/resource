package com.xnfh.cec.service;

import com.xnfh.entity.SysExpert;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.dto.LoginDto;
import com.xnfh.entity.vo.SysUserVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/18
 */

public interface SysUserService {
    SysUser findByUserId(int i);

    /**
     * 注册用户
     * @param sysUser
     * @return
     */
    SysUser addUser(SysUser sysUser);

    /**
     *
     * @param loginDto
     * @return
     */
    SysUser loadUsernameAndPassword(LoginDto loginDto);

    /**
     * 发送验证码
     * @param sendMap
     * @return
     */
    void sendMsg(Map<String, String> sendMap);

    /**
     * 查询用户端的用户信息
     * @param sysUser
     * @return
     */
    SysUser viewUserMessage(SysUser sysUser);
}
