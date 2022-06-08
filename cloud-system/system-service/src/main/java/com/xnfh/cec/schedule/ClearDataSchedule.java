package com.xnfh.cec.schedule;

import com.xnfh.cec.service.SysOperateService;
import com.xnfh.cec.service.zk.ZKService;
import com.xnfh.entity.SysOperLog;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/19
 */

@Slf4j
@Component
@Api(tags = "定时删除三个月前日志数据")
public class ClearDataSchedule {

    @Autowired
    private SysOperateService sysOperateService;

    @Autowired
    private ZKService zkService;

    private static final String CALL_CLOUD_EXPRATION_LOCK_KEY = "/lock/oper/expration";

    @Scheduled(cron = "0 0 22 * * ?")
    public void clearDataJob() throws Exception {
        log.info("schedule start:{}" , new SimpleDateFormat("HH:mm:ss").format(new Date()));
        clearData(sysOperateService);
        log.info("schedule end:{}" , new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }


    private void clearData(SysOperateService sysOperateService) throws Exception {
        InterProcessMutex lock = null;
        try {
            lock = zkService.tryLock(CALL_CLOUD_EXPRATION_LOCK_KEY, 3);
            if (lock != null) {
               ClearDataScheduleForSysOperDataJob(sysOperateService);
              }
        } finally {
            if (lock != null) {
                lock.release();
            }
        }
    }

    private void ClearDataScheduleForSysOperDataJob(SysOperateService sysOperateService) {
        long longStartTime = System.currentTimeMillis();
        sysOperateService.ClearDataScheduleForSysOperDataJob();
        long longEndTime = System.currentTimeMillis();
        log.info("delete sysOper take time :{} ms " ,longEndTime-longStartTime);
    }

}
