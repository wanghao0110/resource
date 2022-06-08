package com.xnfh.cec.service;

import com.xnfh.entity.SysOperLog;
import org.springframework.data.domain.Page;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/18
 */
public interface SysOperateService {
    /**
     * 保存日志信息
     * @param sysOperate
     */
    void saveOperate(SysOperLog sysOperate);

    /**
     * 定时删除数据
     */
    void ClearDataScheduleForSysOperDataJob();
}
