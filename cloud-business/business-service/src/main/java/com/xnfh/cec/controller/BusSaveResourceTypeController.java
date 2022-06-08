package com.xnfh.cec.controller;

import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.service.BusResourceTypeService;
import com.xnfh.cec.service.BusSaveResourceTypeService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusResourceType;
import com.xnfh.entity.BusSaveResourceType;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@RestController
@RequestMapping(value = "v1/xnfh/bus")
public class BusSaveResourceTypeController extends CCAbstractController {

    @Autowired
    private BusSaveResourceTypeService busSaveResourceTypeService;

    @RequestMapping(value = "/save/resource/type",method = RequestMethod.GET)
    @ApiOperation(value = "查询保存资源类型集合")
    @Permission
    public ResponseEntity<ApiResponse> findBusSavePlantTypeList(){
        List<BusSaveResourceType> busSavePlantResourceTypeList = busSaveResourceTypeService.findBusSavePlantTypeList();
        return response(busSavePlantResourceTypeList);
    }


    @RequestMapping(value = "/save/resource/typeSave",method = RequestMethod.POST)
    @ApiOperation(value = "保存资源类型")
    @Permission
    @ApiImplicitParams({
            @ApiImplicitParam(name="busSaveResourceType对象",value = "busSaveResourceType")
    })
    public ResponseEntity<ApiResponse> addResourceSave(@RequestBody BusSaveResourceType busSaveResourceType){
        busSaveResourceTypeService.findBusSaveResourceTypeList(busSaveResourceType);
        return response();
    }
}
