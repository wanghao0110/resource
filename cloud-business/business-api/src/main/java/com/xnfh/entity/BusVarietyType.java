package com.xnfh.entity;

import com.xnfh.common.CommonClassParam;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bus_variety_type")
public class BusVarietyType extends CommonClassParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer varietyId;

    private String varietyName;

    private String remark;


}