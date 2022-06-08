package com.xnfh.cec.jwt;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {

    private Integer userId;

    private String username;

    private String avatar;

    private String ext;

    private String accountNumber;


}
