package com.xnfh.cec.service.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/19
 */
@Slf4j
@Service
public class ZKService {

    /**
     * 基于Curator提供ZooKeeper客户端框架实现对接
     */

    @Value("/xnfh")
    private String zkBasePath;

    @Autowired
    private CuratorConfig zkClient;

    /**
     * @param lockPath
     * @param maxWait   等待时间，单位是秒
     * @return
     */
    public InterProcessMutex tryLock(String lockPath, long maxWait) {
        try {
            InterProcessMutex lock = new InterProcessMutex(zkClient, zkBasePath+lockPath);
            if (lock.acquire(maxWait, TimeUnit.SECONDS)) {
                return lock;
            }
        } catch (Exception e) {
            log.error("lock error, {}", e);
        }
        return null;
    }
}
