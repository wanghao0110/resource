package com.xnfh.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/1
 */
@Data
public class BusExpertPlantVo  implements Serializable {


    private String trueName;

    private String phone;

    private String plantIds;

    private String orderBy;
}
