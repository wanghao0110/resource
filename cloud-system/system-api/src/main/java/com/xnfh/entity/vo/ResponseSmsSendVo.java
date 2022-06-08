package com.xnfh.entity.vo;

import lombok.Data;

/**
 * resource:验证码返回参数
 *
 * @Author wanghaohao ON 2022/5/20
 */

@Data
public class ResponseSmsSendVo {

    private String phone;

    private Integer code;

}
