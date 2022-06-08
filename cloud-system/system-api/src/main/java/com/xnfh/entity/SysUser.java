package com.xnfh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/18
 */
@Data
@Entity
@Table(name = "sys_user")
public class SysUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    private String trueName;

    private String idCard;

    private String accountName;

    private String password;

    private String contactName;

    private String businessName;

    private String contactPhone;

    private String legalPerson;

    private String creditCode;

    private String email;

    private Integer type;

    private String business;

    private Integer status;


    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private Integer auditStatus;

    private Integer isInformation;

    private Integer openCloseStatus;

    private String reason;

    @Transient
    private String auditStatusName;

    @Transient
    private String isInformationName;

    @Transient
    private String currentStatusName;


}
