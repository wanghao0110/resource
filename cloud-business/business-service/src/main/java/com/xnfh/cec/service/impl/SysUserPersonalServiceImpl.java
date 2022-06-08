package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.SysUserPersonalRepository;
import com.xnfh.cec.repository.specification.ExpertSpecification;
import com.xnfh.cec.repository.specification.PersonalSpecification;
import com.xnfh.cec.service.SysUserPersonalService;
import com.xnfh.entity.BusPlant;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserVo;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/31
 */
@Service
@Slf4j
public class SysUserPersonalServiceImpl implements SysUserPersonalService {


    @Autowired
    private SysUserPersonalRepository sysUserPersonalRepository;


    @Override
    public Page<SysUser> listSysUserPersonal(SysUserVo sysUserVo, Pageable pageable) {
        Page<SysUser> sysUserPersonalRepositoryAll = sysUserPersonalRepository.findAll(PersonalSpecification.PersonalSpecification(sysUserVo),pageable);
        //共计的个数
        return new PageImpl<SysUser>(sysUserPersonalRepositoryAll.getContent(),pageable,sysUserPersonalRepositoryAll.getTotalElements());
    }

    /**
     * 查询用户详情数据
     * @param id
     * @return
     */
    @Override
    public SysUser viewSysUserById(Integer id) {
        log.info("current operate data id :{}",id);
        SysUser sysUserDb = sysUserPersonalRepository.findSysUserByUserId(id);
        if(sysUserDb != null){
            return  sysUserDb;
        }else{
         return null;
        }
    }

    /**
     * 修改用户的禁用/启用状态
     * @param sysUser
     * @return
     */
    @Override
    public SysUser updateStatus(SysUser sysUser) {
        if(sysUser.getUserId() != null){
            SysUser sysuserDb = sysUserPersonalRepository.findSysUserByUserId(sysUser.getUserId());
            if(sysuserDb == null){
                throw new ApiException(ExceptionDefinition.CURRENT_SEARCH_SYSUSER_PERSONAL_IS_NULL_4077);
            }else{
                sysuserDb.setOpenCloseStatus(sysUser.getOpenCloseStatus());
                sysuserDb.setUpdateTime(new Date());
                sysUserPersonalRepository.saveAndFlush(sysuserDb);
            }
        }
        return sysUser;
    }
}
