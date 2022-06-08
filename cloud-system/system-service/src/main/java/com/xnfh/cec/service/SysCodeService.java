package com.xnfh.cec.service;

import com.xnfh.entity.vo.ResponseSmsSendVo;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/20
 */
public interface SysCodeService {

    /**
     * 保存发送的验证码
     * @param responseSmsSendVo
     */
    void saveCode(ResponseSmsSendVo responseSmsSendVo);
}
