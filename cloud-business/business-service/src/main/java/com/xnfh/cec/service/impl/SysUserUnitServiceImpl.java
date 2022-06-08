package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.SysUserRepository;
import com.xnfh.cec.repository.SysUserUnitRepository;
import com.xnfh.cec.repository.specification.UnitSpecification;
import com.xnfh.cec.service.SysUserUnitService;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserUnitVo;
import com.xnfh.enums.AuditStatus;
import com.xnfh.enums.AuditUnitStatus;
import com.xnfh.enums.CurrentStatus;
import com.xnfh.enums.IsInformation;
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
public class SysUserUnitServiceImpl implements SysUserUnitService {


    @Autowired
    private SysUserUnitRepository sysUserUnitRepository;



    @Override
    public Page<SysUser> getUnitList(SysUserUnitVo sysUserUnitVo, Pageable pageable) {

        Page<SysUser> sysUserUnitRepositoryAll = sysUserUnitRepository.findAll(UnitSpecification.unitSpecification(sysUserUnitVo),pageable);
        if(sysUserUnitRepositoryAll.getContent() != null){
            sysUserUnitRepositoryAll.getContent().forEach(sysUser -> {
                checkParamsForName(sysUser);
            });
        }
        //共计的个数
        return new PageImpl<SysUser>(sysUserUnitRepositoryAll.getContent(),pageable,sysUserUnitRepositoryAll.getTotalElements());
    }

    /**
     * 查询用人单位详细数据
     * @param userId
     * @return
     */
    @Override
    public SysUser getUnitById(Integer userId) {
        log.info("current search data unit id:{}", userId);
        SysUser sysUserDb = sysUserUnitRepository.findSysUserByUserId(userId);
        if(sysUserDb == null){
            throw new ApiException(ExceptionDefinition.GET_SYSUSER_SEARCH_DB_NOT_EXIST_4030);
        }
        return sysUserDb;
    }

    /**
     *修改禁用启用状态
     * @param userId
     * @param status
     */
    @Override
    public void updateStatus(Integer userId, Integer status) {
        log.info("current need update status :{} ,userId:{}",status,userId);
        //先查询数据是存在
        SysUser sysUserByUserIdDb = sysUserUnitRepository.findSysUserByUserId(userId);
        if(sysUserByUserIdDb != null){
            sysUserByUserIdDb.setUpdateTime(new Date());
            sysUserByUserIdDb.setStatus(status);
            sysUserUnitRepository.saveAndFlush(sysUserByUserIdDb);
        }
    }

    /**
     * 对用人单位的 信息 审核
     * @param userId
     * @param auditStatus
     */
    @Override
    public void updateAuditStatus(Integer userId, Integer auditStatus,String reason) {
        //点击审核按钮，先查询数据是否存在
        SysUser sysUserByUserIdDb = sysUserUnitRepository.findSysUserByUserId(userId);
        if(sysUserByUserIdDb != null){
            sysUserByUserIdDb.setUpdateTime(new Date());
            sysUserByUserIdDb.setAuditStatus(auditStatus);
            if(auditStatus == 2 ){
                sysUserByUserIdDb.setReason(reason);
            }
            sysUserUnitRepository.saveAndFlush(sysUserByUserIdDb);
        }
    }

    /**
     * 显示名称
     * @param sysUser
     */
    private void checkParamsForName(SysUser sysUser) {
        Integer auditStatus = sysUser.getAuditStatus();
        if(auditStatus!=null){
            switch (auditStatus) {
                case 0:
                    sysUser.setAuditStatusName(AuditUnitStatus.toAuditResource.getDesc());
                    break;
                case 1:
                    sysUser.setAuditStatusName(AuditUnitStatus.successResource.getDesc());
                    break;
                case 2:
                    sysUser.setAuditStatusName(AuditUnitStatus.NosuccessResource.getDesc());
                    break;
                default:
                    break;
            }
        }
        if(sysUser.getIsInformation()!=null){
            Integer isInformation = sysUser.getIsInformation();
            switch (isInformation) {
                case 0:
                    sysUser.setIsInformationName(IsInformation.isInformation_yes.getDesc());
                    break;
                case 1:
                    sysUser.setIsInformationName(IsInformation.isInformation_no.getDesc());
                    break;
                default:
                    break;
            }
        }
        if(sysUser.getOpenCloseStatus()!=null){
            Integer openCloseStatus = sysUser.getOpenCloseStatus();
            switch (openCloseStatus) {
                case 0:
                    sysUser.setCurrentStatusName(CurrentStatus.currentStatus_no.getDesc());
                    break;
                case 1:
                    sysUser.setCurrentStatusName(CurrentStatus.currentStatus_yes.getDesc());
                    break;
                default:
                    break;
            }
        }
    }
}
