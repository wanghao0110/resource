package com.xnfh.enums;

import lombok.Getter;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */
@Getter
public enum IsInformation {
    isInformation_yes("是"), isInformation_no("否");


    private String desc;
    IsInformation(String desc) {
        this.desc = desc;
    }


}
