package com.xnfh.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/6
 */
@Data
public class DataAuditResourceVo implements Serializable {


    private Integer userId;

    private Integer plantId;

    private String contactId;

    private String createTime;

    private String inputNum;

    private Integer auditStatus;

    private String orderBy;

    private String address;

}
