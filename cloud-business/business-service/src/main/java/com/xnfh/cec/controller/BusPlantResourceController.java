package com.xnfh.cec.controller;

import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.service.BusPlantResourceService;
import com.xnfh.common.ApiResponse;
import com.xnfh.data.ResourceAuditStatusData;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.vo.PlantResourceVerietyVo;
import com.xnfh.vo.PlantResourceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/25
 */

@RestController
@Api(tags = "资源登记的controller")
@RequestMapping(value = "/v1/xnfh/plant")
public class BusPlantResourceController extends CCAbstractController {

    @Autowired
    private BusPlantResourceService busPlantResourceService;


    @RequestMapping(value = "/resource/add",method = RequestMethod.POST)
    @ApiOperation(value = "用户端资源登记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busPlantResource" ,value = "busPlantResource对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> savePlantResource(@RequestBody BusPlantResource busPlantResource){
        busPlantResourceService.addPlantResource(busPlantResource);
        return response();
    }


    @RequestMapping(value = "/resource/viewBatchResource",method = RequestMethod.GET)
    @ApiOperation(value = "查看用户端的批次资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plantResourceVo" ,value = "plantResourceVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> viewBatchResource( PlantResourceVo plantResourceVo,
                                            @PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<BusPlantResource> busPlantBatchResourcePage =  busPlantResourceService.viewBatchResource(plantResourceVo,pageable);
        //取到用户id
//        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return responsePage(busPlantBatchResourcePage);
    }

    @RequestMapping(value = "/resource/viewBatchResourceByInputNum",method = RequestMethod.GET)
    @ApiOperation(value = "查看用户端的批次资源详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inputNum" ,value = "inputNum批次号")
    })
    @Permission
    public ResponseEntity<ApiResponse> viewBatchResourceByInputNum(String inputNum){
        BusPlantResource busPlantResource =  busPlantResourceService.viewBatchResourceByInputNum(inputNum);
        return response(busPlantResource);
    }

    @RequestMapping(value = "/resource/viewResourceByAuditStatus",method = RequestMethod.GET)
    @ApiOperation(value = "查看用户端的批次资源审核状态,我的种质资源")
    @Permission
    public ResponseEntity<ApiResponse> viewResourceByAuditStatus(){
        ResourceAuditStatusData resourceAuditStatusData = busPlantResourceService.viewResourceByAuditStatus();
        return response(resourceAuditStatusData);
    }

    @RequestMapping(value = "/resource/viewResourceByAuditStatusSuccess",method = RequestMethod.GET)
    @ApiOperation(value = "点击‘登记成功资源XXX个’按钮查看登记成功的资源的信息")
    @Permission
    public ResponseEntity<ApiResponse> viewResourceByAuditStatusSuccess(){
        List<BusPlantResource> busPlantResourceList = busPlantResourceService.viewResourceByAuditStatusSuccess();
        return response(busPlantResourceList);
    }

    @RequestMapping(value = "/resource/viewResourceVariety",method = RequestMethod.GET)
    @ApiOperation(value = "查看用户端的登记成功的种质资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plantResourceVerietyVo" ,value = "plantResourceVerietyVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> viewResourceVaruety( PlantResourceVerietyVo plantResourceVerietyVo,
                                                          @PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<BusPlantResource> busPlantBatchResourcePage =  busPlantResourceService.viewResourceVaruety(plantResourceVerietyVo,pageable);
        return responsePage(busPlantBatchResourcePage);
    }


    @ApiOperation(value = "查看用户端的登记成功的种质资源详情")
    @RequestMapping(value = "/resource/viewSuccessPlantResource",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inputNum" ,value = "inputNum登记号")
    })
    @Permission
    public ResponseEntity<ApiResponse> viewSuccessPlantResource(String inputNum){
        BusPlantResource busPlantResource =  busPlantResourceService.viewSuccessPlantResource(inputNum);
        return response(busPlantResource);
    }


    @ApiOperation(value = "用户端撤销登记信息，不可物理删除")
    @RequestMapping(value = "/resource/CancelPlantResource",method = RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inputNum" ,value = "inputNum登记号")
    })
    @Permission
    public ResponseEntity<ApiResponse> CancelPlantResource(String inputNum){
        busPlantResourceService.CancelPlantResource(inputNum);
        return response();
    }

}
