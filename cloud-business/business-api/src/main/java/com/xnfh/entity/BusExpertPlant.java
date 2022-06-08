package com.xnfh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/1
 */
@Data
@Entity
@Table(name = "bus_expert_plant")
public class BusExpertPlant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer expertId;

    private String trueName;

    private String phone;

    private String plantId;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @Transient
    private String userName;

    @Transient
    private String plantName;
}
