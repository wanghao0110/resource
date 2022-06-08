package com.xnfh.cec.controller.manager;

import com.xnfh.cec.controller.CCAbstractController;
import com.xnfh.cec.repository.specification.PersonalSpecification;
import com.xnfh.cec.repository.specification.UnitSpecification;
import com.xnfh.cec.service.SysUserUnitService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserUnitVo;
import com.xnfh.entity.vo.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * @Author wanghaohao ON 2022/5/31
 */
@RestController
@RequestMapping(value = "/v1/xnfh/sysUserUnit")
@Api(tags = "查询用户单位控制器")
public class SysUserUnitController extends CCAbstractController {


    @Autowired
    private SysUserUnitService sysUserUnitService;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "查询单位列表详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUserUnitVo" ,value = "sysUserUnitVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> getUnitList(SysUserUnitVo sysUserUnitVo,
                                                   @RequestParam(required = false) String orderBy,
                                                   @RequestParam(required = false) String orderMethod,
                                                   @PageableDefault(page = 0, size = 10) Pageable pageable){
        sysUserUnitVo.setOrderBy(orderBy);
        sysUserUnitVo.setOrderMethod(orderMethod);
        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), UnitSpecification.getSort(sysUserUnitVo));
        Page<SysUser> sysUserPage =  sysUserUnitService.getUnitList(sysUserUnitVo,pageRequest);
        return responsePage(sysUserPage);
    }


    @ApiOperation(value = "查询用人单位的详情信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="userId" ,value = "userId查询用人单位详情数据")
    })
    @RequestMapping(value = "/viewUnitById/{userId}" , method =RequestMethod.GET)
    public ResponseEntity<ApiResponse> findUnitById(@PathVariable("userId") Integer userId){
        SysUser unitById = sysUserUnitService.getUnitById(userId);
        return response(unitById);
    }


    @ApiOperation(value = "查询用人单位的禁用/启用信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name =" userId" ,value = "userId值 "),
            @ApiImplicitParam(name =" status" ,value = "status值 ")
    })
    @RequestMapping(value = "/updateStatus" , method =RequestMethod.PUT)
    public ResponseEntity<ApiResponse> updateStatus(Integer userId,Integer status){
        sysUserUnitService.updateStatus(userId,status);
        return response();
    }


    @ApiOperation(value = "用人单位的信息审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name =" userId" ,value = "userId值 "),
            @ApiImplicitParam(name =" auditStatus" ,value = "auditStatus ")
    })
    @RequestMapping(value = "/updateAuditStatus" , method =RequestMethod.PUT)
    public ResponseEntity<ApiResponse> updateAuditStatus(Integer userId,Integer auditStatus,String reason){
        sysUserUnitService.updateAuditStatus(userId,auditStatus,reason);
        return response();
    }
}
