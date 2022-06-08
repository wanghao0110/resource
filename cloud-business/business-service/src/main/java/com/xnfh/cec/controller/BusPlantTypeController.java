package com.xnfh.cec.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.repository.specification.PersonalSpecification;
import com.xnfh.cec.repository.specification.PlantTypeSpecification;
import com.xnfh.cec.repository.specification.UnitSpecification;
import com.xnfh.cec.service.BusPlantTypeService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusPlant;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.entity.BusPlantType;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserVo;
import com.xnfh.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */

@RestController
@RequestMapping("/v1/xnfh/bus")
@Api(tags = "作物类型控制器")
public class BusPlantTypeController extends CCAbstractController {

    @Autowired
    private BusPlantTypeService busPlantTypeService;


    @RequestMapping(value = "/plant/type",method = RequestMethod.GET)
    @ApiOperation(value = "查询作物类型集合")
    @Permission
    public ResponseEntity<ApiResponse> findBusPlantTypeList(){
        List<BusPlantType> busPlantTypeList = busPlantTypeService.findBusPlantTypeList();
        if(busPlantTypeList != null && busPlantTypeList.size() > 0 ){
            return response(busPlantTypeList);
        }
        return (ResponseEntity<ApiResponse>) Collections.emptyList();
    }


    @RequestMapping(value = "/plantType/list",method = RequestMethod.GET)
    @ApiOperation(value = "查询作物类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plantType" ,value = "作物类型传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> plantTypeList(String plantTypeName,
                                                           @PageableDefault(page = 0, size = 10) Pageable pageable){
        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), PlantTypeSpecification.getSort());
        Page<BusPlantType> busPlantBatchResourcePage =  busPlantTypeService.plantTypeList(plantTypeName,pageRequest);
        return responsePage(busPlantBatchResourcePage);
    }



    @ApiOperation(value = "新增作物类型")
    @RequestMapping(value = "/plantType/add", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plantType",value = "plantType")
    })
    public ResponseEntity<ApiResponse> externalAdd(@RequestBody BusPlantType busPlantType) {
        return response(busPlantTypeService.externalAdd(busPlantType));
    }


    @ApiOperation(value = "编辑作物类型")
    @RequestMapping(value = "/plantType/update", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plantType",value = "plantType对应的修改值")
    })
    public ResponseEntity<ApiResponse> externalUpdate(@RequestBody BusPlantType busPlantType) {
        busPlantTypeService.externalUpdate(busPlantType);
        return response();
    }

    @ApiOperation(value = "根据主键id查询")
    @RequestMapping(value = "/plantType/{plantTypeId}",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name ="plantTypeId",value ="plantTypeId主键id" )
    })
    public ResponseEntity<ApiResponse> findPlantTypeByPlantId(@PathVariable("plantTypeId") Integer plantTypeId){
        BusPlantType busPlantType = busPlantTypeService.findPlantTypeByPlantId(plantTypeId);
        if(busPlantType !=null){
            return response(busPlantType);
        }
        return response();
    }


    @ApiOperation(value = "批量删除作物类型信息")
    @RequestMapping(value = "/plantType/batchdelete", method = RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "批量删除作物信息")
    })
    public ResponseEntity<ApiResponse> batchDelete(@RequestBody List<Integer> ids) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            busPlantTypeService.batchDelete(ids);
            apiResponse.setStatus(ApiResponse.STATUS_OK);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (ApiException e) {
            apiResponse.setStatus(ApiResponse.STATUS_FAIL);
            apiResponse.setErrorCode(e.getErrorCode());
            apiResponse.setErrorDescription(e.getErrorDescription());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }



    @ApiOperation(value = "作物类型对象上移动")
    @RequestMapping(value = "/plantType/updateUp", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busPlantType",value = "plantType对应的修改值")
    })
    public ResponseEntity<ApiResponse> updateUp(@RequestBody BusPlantType busPlantType) {
        busPlantTypeService.updateUp(busPlantType);
        return response();
    }

    @ApiOperation(value = "作物类型对象下移动")
    @RequestMapping(value = "/plantType/updateDown", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busPlantType",value = "plantType对应的修改值")
    })
    public ResponseEntity<ApiResponse> updateDown(@RequestBody BusPlantType busPlantType) {
        busPlantTypeService.updateDown(busPlantType);
        return response();
    }


}
