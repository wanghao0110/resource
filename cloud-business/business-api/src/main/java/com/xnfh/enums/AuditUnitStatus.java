package com.xnfh.enums;

import lombok.Getter;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */
@Getter
public enum AuditUnitStatus {
    successResource("审核通过"), toAuditResource("待审核"), NosuccessResource("审核不通过");


    private String desc;
    AuditUnitStatus(String desc) {
        this.desc = desc;
    }


}
