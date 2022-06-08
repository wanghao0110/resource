package com.xnfh.enums;

import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.Data;
import lombok.Getter;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */
@Getter
public enum  AuditStatus {
    successResource(0,"审核成功"), toAuditResource(1,"待审核"), NosuccessResource(2,"审核不成功"),
    cancelResource(3,"撤销审核"),cancelToAutitResource(4,"撤销待审核");


    private Integer code;
    private String desc;
    AuditStatus(Integer code,String desc) {
        this.code = code;
        this.desc = desc;
    }


}
