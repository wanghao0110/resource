package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusFeedBackRepository;
import com.xnfh.cec.service.BusFeedBackService;
import com.xnfh.entity.*;
import com.xnfh.enums.FeedBackStatus;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.Transient;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */

@Service
@Slf4j
public class BusFeedBackServiceImpl implements BusFeedBackService {


    @Autowired
    private BusFeedBackRepository busFeedBackRepository;

    private static Pattern p=Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");

    /**
     * 查询用户端意见反馈列表
     * @param status
     * @return
     */
    @Override
    public Page<BusFeedBack> findFeedbackList(Pageable pageable, Integer status) {
        log.info("current search status :{}",status);

        Specification<BusFeedBack> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            //状态
            if (status != null) {
                Predicate statuss = criteriaBuilder.equal(root.get("status"), status);
                predicates.add(statuss);
            }

            Predicate[] predicatees = new Predicate[predicates.size()];
            return criteriaQuery.where(predicates.toArray(predicatees)).getRestriction();
        };

        //如果遇到findAll(specification,pageable);是红色，记住把repsoitory里面继承-JpaSpecificationExecutor<BusPlantResource>即可
        Page<BusFeedBack> busFeedBackRepositoryAll = busFeedBackRepository.findAll(specification,pageable);
        if(busFeedBackRepositoryAll.getContent() != null){
            busFeedBackRepositoryAll.getContent().forEach(busFeedBack -> {
                checkStatusForFeedBack(busFeedBack);
            });
        }
        //共计的个数
        return new PageImpl<BusFeedBack>(busFeedBackRepositoryAll.getContent(),pageable,busFeedBackRepositoryAll.getTotalElements());
    }

    /**
     * 用户端提出反馈
     * @param busFeedBack
     */
    @Override
    public void addFeedback(BusFeedBack busFeedBack) {
        log.info("current transform object data:{}",busFeedBack);
        busFeedBack.setCreateTime(new Date());
        String phone = busFeedBack.getPhone();
        if(!StringUtils.isEmpty(phone)){
            if (!p.matcher(phone).matches()) {
                throw new ApiException(ExceptionDefinition.GET_PHONE_DATA_REGEX_NOT_CORRECT_4031);
            }
        }
        if(StringUtils.isEmpty(busFeedBack.getContent())){
            throw new ApiException(ExceptionDefinition.GET_BUS_FEEDBACK_CONTENT_IS_NULL_4033);
        }else {
            if(busFeedBack.getContent().length() > 200){
                throw new ApiException(ExceptionDefinition.GET_BUS_FEEDBACK_CONTENT_TO_LONG_4032);
            }
        }
        //单发一个请求处理图片上传
        busFeedBack.setStatus(1);
        busFeedBack.setUpdateTime(new Date());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        // 获取访问的ip地址
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    log.error("addFeedback , getLocalHost error, e: {}", e);
                }
                ipAddress = inet.getHostAddress();
                if (ipAddress == null) {
                    throw new ApiException(ExceptionDefinition.GET_CALL_IPADDRESS_ERROR_4002);
                }
            } else {
                throw new ApiException(ExceptionDefinition.GET_CALL_IPADDRESS_ERROR_4002);
            }
        }
        // 获取图片上传信息
        // 获取浏览器信息
        String requestHeaderUserAgent = request.getHeader("user-agent");
        busFeedBack.setUserId("admin");
        busFeedBack.setRemark("ip address: "+ ipAddress+ "。" +"提出反馈建议。" + "浏览器信息:"+requestHeaderUserAgent);
        busFeedBackRepository.saveAndFlush(busFeedBack);
    }

    /**
     * 查询反馈的单个详情
     * @param id
     * @return
     */
    @Override
    public BusFeedBack viewFeedbackById(Integer id) {
        log.info("current operate feed back id :{}",id);
        BusFeedBack busFeedBackDb = busFeedBackRepository.findBusFeedBackById(id);
        if(busFeedBackDb == null){
            throw new ApiException(ExceptionDefinition.GET_BACK_FEED_DATA_DB_NOT_EXIST_4036);
        }
        if(busFeedBackDb.getStatus()!=null){
            checkStatusForFeedBack(busFeedBackDb);
        }
        return busFeedBackDb;
    }

    /**
     * 删除反馈中的数据
     * @param id
     */

    @Override
    public void removeFeedBack(Integer id) {
        log.info("current need remove id:{}",id);
        //查询需要删除的数据是否存在
        BusFeedBack busFeedBackById = busFeedBackRepository.findBusFeedBackById(id);
        if(busFeedBackById == null){
            throw new ApiException(ExceptionDefinition.GET_BACK_FEED_DATA_DB_NOT_EXIST_4036);
        }
        log.info("current remove removeFeedBack:{}, id:{}, phone:{}" ,busFeedBackById,id,busFeedBackById.getPhone());
        busFeedBackRepository.removeBusFeedBackById(id);
    }

    /**
     *定时删除六个月前反馈数据
     */
    @Override
    public void ClearDataScheduleForBusFeedbackDataJob() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.MONTH, -6);
        Date getTime = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sf.format(getTime);
        int selectDeleteData = busFeedBackRepository.ClearDataScheduleForBusFeedbackDataJob(time);
        log.info("ClearDataScheduleForBusFeedbackDataJob delete record count :{}, schedule delete data time:{} ",selectDeleteData,time);
    }

    /**
     * 校验状态
     * @param busFeedBack
     */
    private void checkStatusForFeedBack(BusFeedBack busFeedBack) {
        Integer status = busFeedBack.getStatus();
        switch (status){
                case 0:
                    busFeedBack.setFeedBackStatusName(FeedBackStatus.alreadImprove.getDesc());
                    break;
                case 1:
                    busFeedBack.setFeedBackStatusName(FeedBackStatus.alreadImprove.getDesc());
                    break;
                case 2:
                    busFeedBack.setFeedBackStatusName(FeedBackStatus.alreadySlove.getDesc());
                    break;
                case 3:
                    busFeedBack.setFeedBackStatusName(FeedBackStatus.toSlove.getDesc());
                    break;
                default:
                    break;
            }
    }
}
