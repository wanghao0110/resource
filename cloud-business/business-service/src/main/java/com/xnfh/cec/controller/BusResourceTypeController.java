package com.xnfh.cec.controller;

import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.service.BusResourceTypeService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusPlantType;
import com.xnfh.entity.BusResourceType;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.AbstractController;

import java.util.Collections;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@RestController
@RequestMapping(value = "v1/xnfh/bus")
public class BusResourceTypeController extends CCAbstractController {

    @Autowired
    private BusResourceTypeService busResourceTypeService;

    @RequestMapping(value = "/resource/type",method = RequestMethod.GET)
    @ApiOperation(value = "查询作物类型集合")
    @Permission
    public ResponseEntity<ApiResponse> findBusPlantTypeList(){
        List<BusResourceType> busPlantResourceTypeList = busResourceTypeService.findBusPlantResourceTypeList();
        if(busPlantResourceTypeList != null && busPlantResourceTypeList.size() > 0 ){
            return response(busPlantResourceTypeList);
        }
        return (ResponseEntity<ApiResponse>) Collections.emptyList();
    }
}
