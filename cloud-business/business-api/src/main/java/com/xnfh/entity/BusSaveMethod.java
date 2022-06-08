package com.xnfh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "bus_save_method")
public class BusSaveMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer saveMethodId;

    private String saveMethodName;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


}