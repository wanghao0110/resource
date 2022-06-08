package com.xnfh.enums;

import lombok.Getter;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */
@Getter
public enum CurrentStatus {
    currentStatus_yes("启用"), currentStatus_no("禁用");


    private String desc;
    CurrentStatus(String desc) {
        this.desc = desc;
    }


}
