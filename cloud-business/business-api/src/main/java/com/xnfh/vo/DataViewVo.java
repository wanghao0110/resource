package com.xnfh.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/6
 */
@Data
public class DataViewVo implements Serializable {


    private Integer plantId;

    private String name;

    private String rn;

    private String createTime;

    private String resourceId;

    private String orderBy;

    private String plantTypeId;

    private String saveResourceTypes;
}
