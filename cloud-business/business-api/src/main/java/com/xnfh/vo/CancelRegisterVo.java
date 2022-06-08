package com.xnfh.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/7
 */
@Data
public class CancelRegisterVo implements Serializable {

    private String businessName;

    private Integer plantId;

    private String contactId;

    private String createTime;

    private String inputNum;

    private String cancelInputNum;

    private Integer status;

    private String cancelDate;

    private String orderBy;


}
