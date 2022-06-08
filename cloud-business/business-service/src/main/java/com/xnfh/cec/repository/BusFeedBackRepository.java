package com.xnfh.cec.repository;

import com.xnfh.entity.BusFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */

@Repository
public interface BusFeedBackRepository extends JpaRepository<BusFeedBack, Integer> , JpaSpecificationExecutor<BusFeedBack> {

    /**
     * 查询最大的id
     * @return
     */
    @Query(value = "select max(id) from BusFeedBack  ")
    Integer findBusFeedBackById();

    /**
     * 查询单个
     */
    BusFeedBack findBusFeedBackById(Integer id);

    /**
     * 删除单个反馈数据
     * @param id
     */
    void removeBusFeedBackById(Integer id);

    /**
     * 定时任务删除六个月前的数据
     * @param time
     * @return
     */
    @Transactional
    @Modifying
    @Query(value ="DELETE FROM bus_feed_back WHERE createTime<=?1",nativeQuery = true)
    int ClearDataScheduleForBusFeedbackDataJob(String time);
}
