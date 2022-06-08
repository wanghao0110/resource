package com.xnfh.cec.controller;

import com.xnfh.cec.anno.Log;
import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.service.SysUserService;

import com.xnfh.common.ApiResponse;
import com.xnfh.entity.SysUser;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/18
 */
@Api(tags = "用户管理")
@RestController
@Slf4j
@RequestMapping(value = "/v1/sysUser")
public class SysUserController extends CCAbstractController {


    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "/register" , method = RequestMethod.POST)
    @ApiOperation(value = "注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser" ,value = "用户对象")
    })
    @Log(name = "注册用户")
    @Permission
    public ResponseEntity<ApiResponse> addUser(@RequestBody SysUser sysUser) {
        return response( sysUserService.addUser(sysUser));
    }


    @RequestMapping(value = "/viewUserMessage" , method = RequestMethod.GET)
    @ApiOperation(value = "查看用户端的用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser" ,value = "用户对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> viewUserMessage(@RequestBody SysUser sysUser) {
        return response( sysUserService.viewUserMessage(sysUser));
    }


    @RequestMapping(value = "/exit" , method = RequestMethod.GET)
    @ApiOperation(value = "用户端的退出操作")
    @Permission
    public ResponseEntity<ApiResponse> exit() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println(userId);

        return response();
    }
    public ResponseEntity<ApiResponse> getuser(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println(userId);

        return response();
    }


}
