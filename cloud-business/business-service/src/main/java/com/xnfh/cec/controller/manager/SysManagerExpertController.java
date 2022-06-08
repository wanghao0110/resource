package com.xnfh.cec.controller.manager;

import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.controller.CCAbstractController;
import com.xnfh.cec.repository.specification.ExpertSpecification;

import com.xnfh.cec.service.SysExpertService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.vo.SysExpertVo;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
@RestController
@Api(tags = "后台专家管理控制器")
@RequestMapping(value = "/v1/xnfh/manager")
public class SysManagerExpertController extends CCAbstractController {

    @Autowired
    private SysExpertService sysExpertService;


    @RequestMapping(value = "/expert/list",method = RequestMethod.GET)
    @ApiOperation(value = "查看后端的专家管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SysExpertVo" ,value = "SysExpertVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> expertList(SysExpertVo sysExpertVo,
                                                  @RequestParam(required = false) String orderBy,
                                                  @RequestParam(required = false) String orderMethod,
                                                  @PageableDefault(page = 0, size = 10) Pageable pageable){
        sysExpertVo.setOrderBy(orderBy);
        sysExpertVo.setOrderMethod(orderMethod);
        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ExpertSpecification.getSort(sysExpertVo));
        Page<SysExpert> sysExpertPage =  sysExpertService.expertList(sysExpertVo,pageRequest);
        return responsePage(sysExpertPage);
    }

    @RequestMapping(value = "/expert/addExpert",method = RequestMethod.POST)
    @ApiOperation(value = "添加专家的后端接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SysExpert" ,value = "SysExpertVo传输展示对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> addExpert(@RequestBody SysExpert sysExpert){
        sysExpertService.addExpert(sysExpert);
        return response();
    }


    @RequestMapping(value = "/expert/viewSysExpertById/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "查询专家的后端接口详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "专家id值")
    })
    @Permission
    public ResponseEntity<ApiResponse> getSysExpertById(@PathVariable("id") Integer id){
        return response(sysExpertService.getSysExpertById(id));
    }


    @RequestMapping(value = "/expert/updateSysExpert",method = RequestMethod.PUT)
    @ApiOperation(value = "修改专家的后端接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SysExpert" ,value = "SysExpertVo传输展示对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> updateSysExpert(@RequestBody SysExpert sysExpert){
        sysExpertService.updateSysExpert(sysExpert);
        return response();
    }


    @ApiOperation(value = "批量删除专家的后端接口")
    @RequestMapping(value = "/expert/batchdelete", method = RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "批量删除专家信息,专家和作物关联？")
    })
    public ResponseEntity<ApiResponse> batchDelete(@RequestBody List<Integer> ids) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            sysExpertService.batchDelete(ids);
            apiResponse.setStatus(ApiResponse.STATUS_OK);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (ApiException e) {
            apiResponse.setStatus(ApiResponse.STATUS_FAIL);
            apiResponse.setErrorCode(e.getErrorCode());
            apiResponse.setErrorDescription(e.getErrorDescription());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/expert/updateStatus",method = RequestMethod.PUT)
    @ApiOperation(value = "修改专家禁用/启用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SysExpert" ,value = "SysExpert传输展示对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> updateStatus(@RequestBody SysExpert sysExpert){
        sysExpertService.updateStatus(sysExpert);
        return response();
    }


    @RequestMapping(value = "/expert/importBatch",method = RequestMethod.POST)
    @ApiOperation(value = "专家管理的批量导入接口")
    @ApiImplicitParam(name = "file" ,value = "专家管理的批量导入")
    @Permission
    public Map<String, Object>  importBatch(@RequestParam("file") MultipartFile file){
        Map<String, Object> map = new HashMap<>();
        try {
            map = sysExpertService.importExcel(file);
        }catch (Exception e){
            map.put("status",-1);
            map.put("data", "导入异常");
            logger.error(e.getMessage(),e);
        }
        return map;
    }

}
