package com.xnfh.enums;

import lombok.Getter;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */
@Getter
public enum FeedBackStatus {
    alreadyRead(0,"已读"), alreadImprove(1,"已提交"), alreadySlove(2,"已处理"), toSlove(3,"待处理");


    private String desc;
    private Integer code;
    FeedBackStatus(Integer code , String desc) {
        this.code = code;
        this.desc = desc;
    }


}
