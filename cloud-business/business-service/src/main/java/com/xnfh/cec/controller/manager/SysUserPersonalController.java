package com.xnfh.cec.controller.manager;

import com.xnfh.cec.controller.CCAbstractController;
import com.xnfh.cec.repository.specification.ExpertSpecification;
import com.xnfh.cec.repository.specification.PersonalSpecification;
import com.xnfh.cec.service.SysUserPersonalService;
import com.xnfh.cec.service.SysUserService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.SysUser;
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
@RequestMapping(value = "/v1/xnfh/sysUserPersonal")
@Api(tags = "个人信息控制器")
public class SysUserPersonalController extends CCAbstractController {

    @Autowired
    private SysUserPersonalService sysUserPersonalService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "查询列表详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUserVo" ,value = "sysUserVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> listSysUserPersonal(SysUserVo sysUserVo,
                                                           @RequestParam(required = false) String orderBy,
                                                           @RequestParam(required = false) String orderMethod,
                                                           @PageableDefault(page = 0, size = 10) Pageable pageable){

        sysUserVo.setOrderBy(orderBy);
        sysUserVo.setOrderMethod(orderMethod);
        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), PersonalSpecification.getSort(sysUserVo));
        Page<SysUser> sysUserPage =  sysUserPersonalService.listSysUserPersonal(sysUserVo,pageRequest);
        return responsePage(sysUserPage);
    }


    @RequestMapping(value = "/viewUserById/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "查询用户详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "userId传输展示对象")
    })
    public ResponseEntity<ApiResponse> viewSysUserById(@PathVariable("id") Integer id){
        return response( sysUserPersonalService.viewSysUserById(id));
    }


    @RequestMapping(value = "/updateStatus",method = RequestMethod.PUT)
    @ApiOperation(value = "修改用户的禁用/启用状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser" ,value = "sysUser传输展示对象")
    })
    public ResponseEntity<ApiResponse> updateStatus(@RequestBody SysUser sysUser){
        return response( sysUserPersonalService.updateStatus(sysUser));
    }


}
