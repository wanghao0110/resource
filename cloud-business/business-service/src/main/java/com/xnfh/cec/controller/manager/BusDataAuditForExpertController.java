package com.xnfh.cec.controller.manager;

import com.xnfh.cec.controller.CCAbstractController;
import com.xnfh.cec.repository.BusPlantResourceRepository;
import com.xnfh.cec.repository.specification.CharacterManagerSpecification;
import com.xnfh.cec.repository.specification.ViewAuditSpecification;
import com.xnfh.cec.service.BusPlantResourceService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusCharacterManager;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.vo.CharacterManagerVo;
import com.xnfh.vo.DataAuditResourceVo;
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
 * @Author wanghaohao ON 2022/6/6
 */
@RestController
@Api(tags = "数据审核专家使用")
@RequestMapping(value = "/v1/xnfh/manager")
public class BusDataAuditForExpertController extends CCAbstractController {


    @Autowired
    private BusPlantResourceService busPlantResourceService;


    @RequestMapping(value = "/dataAudit/pageList",method = RequestMethod.GET)
    @ApiOperation(value = "在线登记的资源分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataAuditResourceVo" ,value = "dataAuditResourceVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> findDataAuditPage(DataAuditResourceVo dataAuditResourceVo,
                                            @PageableDefault(page = 0, size = 10) Pageable pageable){

        Page<BusPlantResource> busPlantBatchResourcePage =  busPlantResourceService.dataAuditResource(dataAuditResourceVo,pageable);
        return responsePage(busPlantBatchResourcePage);
    }


    @ApiOperation(value = "审核不通过")
    @RequestMapping(value = "/audit/result",method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> addResult(@RequestBody BusPlantResource busPlantResource){
        busPlantResourceService.addResult(busPlantResource);
        return response();
    }

    @RequestMapping(value = "/dataAudit/viewAudit",method = RequestMethod.GET)
    @ApiOperation(value = "查看数据审核详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataAuditResourceVo" ,value = "dataAuditResourceVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> viewAudit(ViewAuditVo viewAuditVo,
                                                 @Param("orderBy") String orderBy,
                                                 @PageableDefault(page = 0, size = 10) Pageable pageable){

        viewAuditVo.setOrderBy(orderBy);
        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ViewAuditSpecification.getSort(viewAuditVo));
        Page<BusPlantResource> busPlantResourcePage =  busPlantResourceService.viewAudit(viewAuditVo,pageRequest);
        return responsePage(busPlantResourcePage);
    }

    @ApiOperation(value = "查看详情（标记旁边可删）")
    @RequestMapping(value = "/audit/viewDetail/{rId}",method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> viewDetail(@PathVariable("rId") String rId){
        BusPlantResource busPlantResource = busPlantResourceService.viewDetail(rId);
        return response(busPlantResource);
    }

    @ApiOperation(value = "修改资源标记")
    @RequestMapping(value = "/audit/resourceClick/{rId}",method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> resourceClick(@PathVariable("rId") String rId){
        busPlantResourceService.resourceClick(rId);
        return response();
    }

    //TODO图片显示

}
