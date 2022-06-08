package com.xnfh.cec.controller.manager;

import com.xnfh.cec.controller.CCAbstractController;
import com.xnfh.cec.repository.specification.CharacterSpecification;
import com.xnfh.cec.repository.specification.DataViewSpecification;
import com.xnfh.cec.service.BusPlantResourceService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusCharacterType;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.vo.DataAuditResourceVo;
import com.xnfh.vo.DataViewVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * @Author wanghaohao ON 2022/6/6
 */
@Api(tags = "查看数据成功已经审核完成控制器")
@RequestMapping(value = "/v1/xnfh/dataManager")
@RestController
public class BusDataViewSuccessController extends CCAbstractController {


    @Autowired
    private BusPlantResourceService busPlantResourceService;


    @RequestMapping(value = "/view/pageList",method = RequestMethod.GET)
    @ApiOperation(value = "在线登记的资源分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataAuditResourceVo" ,value = "dataAuditResourceVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> pageList(DataViewVo dataViewVo,
                                                @RequestParam(required = false) String orderBy,
                                                @PageableDefault(page = 0, size = 10) Pageable pageable){

        dataViewVo.setOrderBy(orderBy);
        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), DataViewSpecification.getSort(dataViewVo));
        Page<BusPlantResource> busPlantResourcePage =  busPlantResourceService.pageList(dataViewVo,pageRequest);
        return responsePage(busPlantResourcePage);
    }


    @RequestMapping(value = "/view/findByPlantIdForCharacter/{plantResourceId}",method = RequestMethod.GET)
    @ApiOperation(value = "查看登记成功的种质资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plantResourceId" ,value = "plantResourceId")
    })
    public ResponseEntity<ApiResponse> findByPlantIdForCharacter(@PathVariable("plantResourceId") Integer plantResourceId){

        BusPlantResource busPlantResourceServiceByPlantIdForCharacter =  busPlantResourceService.findByPlantIdForCharacter(plantResourceId);
        return response(busPlantResourceServiceByPlantIdForCharacter);
    }

}
