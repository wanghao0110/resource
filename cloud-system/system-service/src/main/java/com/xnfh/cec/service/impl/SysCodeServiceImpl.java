package com.xnfh.cec.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.xnfh.cec.repository.SysCodeRepositroy;
import com.xnfh.cec.service.SysCodeService;
import com.xnfh.entity.SysCode;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.ResponseSmsSendVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/20
 */
@Service
@Slf4j
public class SysCodeServiceImpl implements SysCodeService {

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private SysCodeRepositroy sysCodeRepositroy;

    @Override
    public void saveCode(ResponseSmsSendVo responseSmsSendVo) {
        //保存验证码
        SysCode sysCode = new SysCode();
        //TODO
       // String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sysCode.setUserId(10001);
        sysCode.setPhone(responseSmsSendVo.getPhone());
        sysCode.setCode(responseSmsSendVo.getCode());
        sysCode.setSendTime(new Date());
        //验证码放入缓存
        String uuId = "v1.xnfh."+responseSmsSendVo.getPhone();
        redis.opsForValue().set(uuId,String.valueOf(responseSmsSendVo.getCode()), Duration.ofMinutes(1));
        sysCodeRepositroy.saveAndFlush(sysCode);
    }
}
