package com.xnfh.cec.controller;

import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.service.BusSaveMethodService;
import com.xnfh.cec.service.BusSaveResourceTypeService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusSaveMethod;
import com.xnfh.entity.BusSaveResourceType;
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
 * @Author wanghaohao ON 2022/5/23
 */

@RestController
@RequestMapping(value = "v1/xnfh/bus")
public class BusSaveMethodController  extends CCAbstractController  {

    @Autowired
    private BusSaveMethodService busSaveMethodService;

    @RequestMapping(value = "/save/method",method = RequestMethod.GET)
    @ApiOperation(value = "查询保存方法集合")
    @Permission
    public ResponseEntity<ApiResponse> findBusSaveMethodList(){
        List<BusSaveMethod> busSaveMethodServiceBusSaveMethodList = busSaveMethodService.findBusSaveMethodList();
        return response(busSaveMethodServiceBusSaveMethodList);
    }
}
