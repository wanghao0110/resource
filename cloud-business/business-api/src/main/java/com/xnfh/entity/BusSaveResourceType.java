package com.xnfh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "bus_save_resource_type")
public class BusSaveResourceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer saveResourceId;

    private String saveResourceName;


    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}