package com.xnfh.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/26
 */

@Data
public class PlantResourceVerietyVo implements Serializable {

    private String inputNum;

    private String varietyName;

    private Integer plantId;

    private String createTime;

    private Integer varietyTypeId;

    private String plantName;

    private String saveResourceTypes;



}
