package com.xnfh.cec.controller.manager;

import com.xnfh.cec.controller.CCAbstractController;
import com.xnfh.cec.repository.specification.CancelInputNumSpecification;
import com.xnfh.cec.repository.specification.CharacterManagerSpecification;
import com.xnfh.cec.repository.specification.ViewAuditSpecification;
import com.xnfh.cec.service.BusCancelRegisterAuditService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusCharacterManager;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.vo.CancelRegisterVo;
import com.xnfh.vo.CharacterManagerVo;
import com.xnfh.vo.ViewAuditVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/7
 */
@RestController
@Api(tags = "撤销登记审核控制器")
@RequestMapping(value = "/v1/xnfh/cancel")
public class BusCancelRegisterAuditController  extends CCAbstractController {


    @Autowired
    private BusCancelRegisterAuditService busCancelRegisterAuditService;

    @RequestMapping(value = "/pageList",method = RequestMethod.GET)
    @ApiOperation(value = "撤销登记审核分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "characterManagerVo" ,value = "characterManagerVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> findCharacterManagerPage(CancelRegisterVo cancelRegisterVo,
                                                                @RequestParam(required = false) String orderBy,
                                                                @PageableDefault(page = 0, size = 10) Pageable pageable) {

        cancelRegisterVo.setOrderBy(orderBy);
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), CancelInputNumSpecification.getSort(cancelRegisterVo));
        Page<BusPlantResource> busCharacterManagerPage = busCancelRegisterAuditService.findCharacterManagerPage(cancelRegisterVo, pageRequest);
        return responsePage(busCharacterManagerPage);
    }


    @ApiOperation(value = "审核通过")
    @RequestMapping(value = "/audit/result",method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> auditSuccess(@RequestBody BusPlantResource busPlantResource){
        busCancelRegisterAuditService.auditSuccess(busPlantResource);
        return response();
    }

    //TODO次方法已经实现
//    @RequestMapping(value = "/dataAudit/viewAudit",method = RequestMethod.GET)
//    @ApiOperation(value = "查看数据审核详情")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "dataAuditResourceVo" ,value = "dataAuditResourceVo传输展示对象"),
//            @ApiImplicitParam(name = "pageable",value = "分页对象")
//    })
//    public ResponseEntity<ApiResponse> viewAudit(ViewAuditVo viewAuditVo,
//                                                 @Param("orderBy") String orderBy,
//                                                 @PageableDefault(page = 0, size = 10) Pageable pageable){
//
//        viewAuditVo.setOrderBy(orderBy);
//        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ViewAuditSpecification.getSort(viewAuditVo));
//        Page<BusPlantResource> busPlantResourcePage =  busPlantResourceService.viewAudit(viewAuditVo,pageRequest);
//        return responsePage(busPlantResourcePage);
//    }

}
