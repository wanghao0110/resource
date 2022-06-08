package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.SysOperRepositroy;
import com.xnfh.cec.service.SysOperateService;
import com.xnfh.entity.SysOperLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @Author wanghaohao ON 2022/5/18
 */
@Service
@Slf4j
public class SysOperateServiceImpl implements SysOperateService {

    @Autowired
    private SysOperRepositroy sysOperRepositroy;

    @Override
    public void saveOperate(SysOperLog sysOperate) {
        sysOperRepositroy.saveAndFlush(sysOperate);
    }

    @Override
    public void ClearDataScheduleForSysOperDataJob() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.MONTH, -3);
        Date getTime = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sf.format(getTime);
        int selectDeleteData = sysOperRepositroy.deleteOperLogsThreeMonthBefore(time);
        log.info("SysOperateServiceImpl RemoveDataJob delete record count :{}, schedule delete data time:{} ",selectDeleteData,time);
    }

}
