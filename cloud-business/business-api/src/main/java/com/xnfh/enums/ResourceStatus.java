package com.xnfh.enums;

import lombok.Getter;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */
@Getter
public enum ResourceStatus {
    successResource(0,"正常资源"), NocereResource(1,"标记不合格");


    private Integer code;
    private String desc;
    ResourceStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
