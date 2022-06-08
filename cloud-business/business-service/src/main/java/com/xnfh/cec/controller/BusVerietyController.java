package com.xnfh.cec.controller;

import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.service.BusVerietyTypeService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusSaveMethod;
import com.xnfh.entity.BusVarietyType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@Api(tags = "种质类型控制器")
@RequestMapping(value = "/v1/xnfh/plant")
public class BusVerietyController  extends  CCAbstractController{


    @Autowired
    private BusVerietyTypeService busVerietyTypeService;

    @RequestMapping(value = "/variety/findAll",method = RequestMethod.GET)
    @ApiOperation(value = "查询种质类型集合")
    @Permission
    public ResponseEntity<ApiResponse> findBusVerietyList(){
        List<BusVarietyType> busVarietyTypeList = busVerietyTypeService.findBusVerietyList();
        return response(busVarietyTypeList);
    }





}
