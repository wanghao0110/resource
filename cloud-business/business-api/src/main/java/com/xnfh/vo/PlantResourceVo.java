package com.xnfh.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xnfh.entity.BusPlant;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/26
 */

@Data
public class PlantResourceVo  implements Serializable {

    private Integer plantResourceId;

    private String inputNum;

    private Integer plantId;

    private Integer plantTypeId;

    private Integer varietyTypeId;

    private Integer resourceId;

    private Integer plantNum;

    private String plantName;

    private String createTime;

    private Integer auditStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date auditTime;


}
