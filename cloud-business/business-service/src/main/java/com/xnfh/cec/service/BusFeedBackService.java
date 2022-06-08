package com.xnfh.cec.service;

import com.xnfh.entity.BusFeedBack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */
public interface BusFeedBackService {

    /**
     * 查询用户端意见反馈列表
     * @param status
     * @return
     */
    Page<BusFeedBack> findFeedbackList(Pageable pageable, Integer status);

    /**
     * 用户端提反馈
     * @param busFeedBack
     */
    void addFeedback(BusFeedBack busFeedBack);

    /**
     * 查询单个用户的反馈详情
     * @param id
     * @return
     */
    BusFeedBack viewFeedbackById(Integer id);

    /**
     * 删除反馈中的数据
     * @param id
     */
    void removeFeedBack(Integer id);

    /**
     * 定时删除六个月前反馈数据
     */
    void ClearDataScheduleForBusFeedbackDataJob();
}
