package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusCancelRegisterAuditRepository;
import com.xnfh.cec.repository.BusPlantRepository;
import com.xnfh.cec.repository.BusPlantResourceRepository;
import com.xnfh.cec.repository.specification.CancelInputNumSpecification;
import com.xnfh.cec.repository.specification.CharacterSpecification;
import com.xnfh.cec.service.BusCancelRegisterAuditService;
import com.xnfh.entity.BusCharacterType;
import com.xnfh.entity.BusPlant;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.enums.AuditStatus;
import com.xnfh.vo.CancelRegisterVo;
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
 * @Author wanghaohao ON 2022/6/7
 */
@Service
@Slf4j
public class BusCancelRegisterAuditServiceImpl implements BusCancelRegisterAuditService {

    @Autowired
    private BusCancelRegisterAuditRepository busCancelRegisterAuditRepository;

    @Autowired
    private BusPlantRepository busPlantRepository;

    @Autowired
    private BusPlantResourceRepository busPlantResourceRepository;

    @Override
    public Page<BusPlantResource> findCharacterManagerPage(CancelRegisterVo cancelRegisterVo, Pageable pageRequest) {


        Page<BusPlantResource> busCancelRegisterAuditRepositoryAll = busCancelRegisterAuditRepository.findAll(CancelInputNumSpecification.cancelInputNumSpecification(cancelRegisterVo),pageRequest);
        if(busCancelRegisterAuditRepositoryAll.getContent()!=null){
             busCancelRegisterAuditRepositoryAll.getContent().forEach(busPlantResource -> {
                 Integer plantId = busPlantResource.getPlantId();
                 //查询作物
                 if(plantId!=null){
                     BusPlant busPlantByPlantId = busPlantRepository.findBusPlantByPlantId(plantId);
                     if(busPlantByPlantId!=null){
                         String plantName = busPlantByPlantId.getPlantName();
                         busPlantResource.setPlantName(plantName);
                     }
                 }
                 //审核状态
                 checkStatus(busPlantResource);
             });
        }
        //共计的个数
        return new PageImpl<BusPlantResource>(busCancelRegisterAuditRepositoryAll.getContent(),pageRequest,busCancelRegisterAuditRepositoryAll.getTotalElements());
    }

    /**
     * 审核通过，修改状态
     * @param busPlantResource
     */
    @Override
    public void auditSuccess(BusPlantResource busPlantResource) {
        Integer plantResourceId = busPlantResource.getPlantResourceId();
        BusPlantResource busPlantResourceDb = busPlantResourceRepository.findBusPlantResourceByPlantResourceId(plantResourceId);
        if(busPlantResourceDb!=null){
            busPlantResource.setAuditStatus(AuditStatus.cancelResource.getCode());
            busPlantResource.setAuditTime(new Date());
            busPlantResource.setCancelTime(new Date());
            busPlantResource.setIsDelete(1);
            busPlantResourceRepository.saveAndFlush(busPlantResource);
        }

    }

    private void checkStatus(BusPlantResource busPlantResource) {
        Integer status = busPlantResource.getAuditStatus();
        switch (status){
            case 0:
                busPlantResource.setAuditStatusName(AuditStatus.successResource.getDesc());
                break;
            case 1:
                busPlantResource.setAuditStatusName(AuditStatus.toAuditResource.getDesc());
                break;
            case 2:
                busPlantResource.setAuditStatusName(AuditStatus.NosuccessResource.getDesc());
                break;
            case 3:
                busPlantResource.setAuditStatusName(AuditStatus.cancelResource.getDesc());
                break;
            case 4:
                busPlantResource.setAuditStatusName(AuditStatus.cancelToAutitResource.getDesc());
                break;
            default:
                break;
        }
    }
}
