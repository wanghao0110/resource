package com.xnfh.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/31
 */
@Data
public class SysUserVo implements Serializable {

    private String accountName;

    private String trueName;

    private String contactPhone;

    private String orderBy;

    private String orderMethod;

}
