package com.xnfh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xnfh.enums.AuditStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "bus_plant_resource")
public class BusPlantResource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer plantResourceId;

    private Integer userId;

    private String inputNum;

    private String rId;

    private String rn;

    private Integer plantId;

    private Integer plantTypeId;

    private Integer varietyTypeId;

    private Integer resourceId;

    private Integer plantNum;

    private String saveResourceId;

    private String name;

    private String latinName;

    private String businessName;

    private String latinFamilyName;

    private String latinGenusName;

    private String latinPrunusPersica;

    private String code;

    private String chineseName;

    private String chineseFamilyName;

    private String chineseGenusName;

    private String chinesePrunusName;

    private String saveMethodId;

    private String saveExtId;

    private String sourceArea;

    private String characterInformation;

    private String authorizationNumber;

    private String patentNumber;

    private String otherCere;

    private Boolean isJoinCon;

    private Boolean isCommonOpen;

    private Integer resourceStatus;

    private String contactId;

    private String phone;

    private String url;

    private String cancelInputNum;

    private String remark;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date cancelTime;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private Integer auditStatus;

    private String reason;

    @Transient
    private List<Integer> saveResourceIds;

    @Transient
    private List<Integer> saveExtIds;

    @Transient
    private List<Integer> saveMethodIds;


    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date auditTime;

    private Integer isDelete;

    @Transient
    private String plantName;

    @Transient
    private String plantType;

    @Transient
    private String plantVerietyName;

    @Transient
    private String saveResourceName;


    @Transient
    private String auditStatusName;

    @Transient
    private String address;

    @Transient
    private String resourceStatusName;

    @Transient
    private String resourceTypeName;

    @Transient
    private String characterManagerName;

}