package com.xnfh.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/6
 */
@Data
public class ViewAuditVo implements Serializable {


    private String inputNum;

    private String resourceType;

    private String name;

    private String resourceId;

    private String saveResourceId;

    private String orderBy;

    private String RID;

    private String saveResourceTypes;
}
