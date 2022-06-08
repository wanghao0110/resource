package com.xnfh.entity.dto;

import lombok.Data;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/19
 */
@Data
public class LoginDto {

    private String accountName;

    private String password;

    /**
     * 登陆类型
     */
    private Integer loginType;

    //jie
    private String phoneNumber;

    //接收验证码
    private Integer code;
}
