package com.xnfh.cec.controller;

import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.service.BusFeedBackService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusFeedBack;
import com.xnfh.entity.BusPlant;
import com.xnfh.entity.BusResourceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/28
 */

@RestController
@Api(tags = "用户端意见反馈控制器")
@RequestMapping(value = "v1/xnfh/feedBack")
public class BusFeedBackController extends CCAbstractController {

    @Autowired
    private BusFeedBackService busFeedBackService;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "查询用户端意见反馈列表")
    @Permission
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status",value = "status反馈的状态信息"),
            @ApiImplicitParam(name= "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> findFeedbackList(@PageableDefault(page = 0, size = 10) Pageable pageable,Integer status){
        Page<BusFeedBack> busFeedBackPage = busFeedBackService.findFeedbackList(pageable, status);
        return responsePage(busFeedBackPage);

    }

    @RequestMapping(value = "/addFeedBack",method = RequestMethod.POST)
    @ApiOperation(value = "用户端提反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "BusFeedBack" ,value = "提反馈的传输对象")
    })
    public ResponseEntity<ApiResponse> addFeedback(@RequestBody BusFeedBack busFeedBack){
        busFeedBackService.addFeedback(busFeedBack);
        return response();

    }

    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "用户端提反馈的详情操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "提反馈的传输对象id")
    })
    public ResponseEntity<ApiResponse> viewFeedbackById(@PathVariable("id") Integer id){
        return response(busFeedBackService.viewFeedbackById(id));
    }

    @RequestMapping(value = "/view/removeFeedBack",method = RequestMethod.DELETE)
    @ApiOperation(value = "用户端提反馈的删除操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "BusFeedBack" ,value = "提反馈的传输对象")
    })
    public ResponseEntity<ApiResponse> removeFeedBack(Integer id){
        busFeedBackService.removeFeedBack(id);
        return response();
    }


}
