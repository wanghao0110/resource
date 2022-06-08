package com.xnfh.cec.controller;

import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.service.BusSaveExtService;
import com.xnfh.cec.service.BusSaveMethodService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusSaveExt;
import com.xnfh.entity.BusSaveMethod;
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
public class BusSaveExtController extends CCAbstractController {

    @Autowired
    private BusSaveExtService busSaveExtService;

    @RequestMapping(value = "/save/ext",method = RequestMethod.GET)
    @ApiOperation(value = "查询保存方式集合")
    @Permission
    public ResponseEntity<ApiResponse> findBusSaveExtList(){
        List<BusSaveExt> busSaveExts = busSaveExtService.findBusSaveExtList();
        if(busSaveExts != null){
            return response(busSaveExts);
        }
        return null;
    }
}
