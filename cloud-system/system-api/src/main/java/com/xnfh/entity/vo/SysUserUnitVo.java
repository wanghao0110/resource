package com.xnfh.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/31
 */
@Data
public class SysUserUnitVo implements Serializable {

    private String businessName;

    private String accountName;

    private Integer openCloseStatus;

    private String orderBy;

    private String orderMethod;
}
