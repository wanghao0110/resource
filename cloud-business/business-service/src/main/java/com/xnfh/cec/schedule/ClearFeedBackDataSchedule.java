package com.xnfh.cec.schedule;

import com.xnfh.cec.service.BusFeedBackService;
import com.xnfh.cec.service.SysOperateService;
import com.xnfh.cec.service.zk.ZKService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */

@Slf4j
@Component
@Api(tags = "定时删除六个月前反馈数据")
public class ClearFeedBackDataSchedule {


    @Autowired
    private BusFeedBackService busFeedBackService;

    @Autowired
    private ZKService zkService;

    private static final String CALL_CLOUD_EXPRATION_LOCK_KEY = "/lock/oper/expration";

    @Scheduled(cron = "0 0 23 * * ?")
    public void clearDataJob() throws Exception {
        log.info("schedule start:{}" , new SimpleDateFormat("HH:mm:ss").format(new Date()));
        clearData(busFeedBackService);
        log.info("schedule end:{}" , new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }


    private void clearData(BusFeedBackService busFeedBackService) throws Exception {
        InterProcessMutex lock = null;
        try {
            lock = zkService.tryLock(CALL_CLOUD_EXPRATION_LOCK_KEY, 3);
            if (lock != null) {
                ClearDataScheduleForBusFeedbackDataJob(busFeedBackService);
            }
        } finally {
            if (lock != null) {
                lock.release();
            }
        }
    }

    private void ClearDataScheduleForBusFeedbackDataJob(BusFeedBackService busFeedBackService) {
        long longStartTime = System.currentTimeMillis();
        busFeedBackService.ClearDataScheduleForBusFeedbackDataJob();
        long longEndTime = System.currentTimeMillis();
        log.info("delete bus feedBack take time :{} ms " ,longEndTime-longStartTime);
    }
}
