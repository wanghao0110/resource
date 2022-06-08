package com.xnfh.cec.controller.manager;

import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.controller.CCAbstractController;
import com.xnfh.cec.repository.specification.CharacterSpecification;
import com.xnfh.cec.repository.specification.UnitSpecification;
import com.xnfh.cec.service.BusCharacterTypeService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusCharacterType;
import com.xnfh.entity.BusPlantType;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserUnitVo;
import com.xnfh.exception.ApiException;
import com.xnfh.vo.BusCharacterTypeVo;
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

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/2
 */
@RestController
@Api(tags = "形状类型控制器")
@RequestMapping(value = "/v1/xnfh/characterType")
public class BusCharacterTypeController extends CCAbstractController {


    @Autowired
    private BusCharacterTypeService busCharacterTypeService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "形状类型列表分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "characterTypeVo" ,value = "characterTypeVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> getCharacterTypePage(BusCharacterTypeVo busCharacterTypeVo,
                                                            @RequestParam(required = false) String orderBy,
                                                            @PageableDefault(page = 0, size = 10) Pageable pageable){
        busCharacterTypeVo.setOrderBy(orderBy);
        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), CharacterSpecification.getSort(busCharacterTypeVo));
        Page<BusCharacterType> busCharacterTypePage =  busCharacterTypeService.getCharacterTypePage(busCharacterTypeVo,pageRequest);
        return responsePage(busCharacterTypePage);

    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加性状类型接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "BusCharacterType" ,value = "BusCharacterType传输展示对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> addCharacterType(@RequestBody BusCharacterType busCharacterType){
        return response(busCharacterTypeService.addCharacterType(busCharacterType));
    }


    @RequestMapping(value = "/viewCharacterById/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "查询性状类型接口详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "性状类型id值")
    })
    @Permission
    public ResponseEntity<ApiResponse> viewCharacterById(@PathVariable("id") Integer id){
        return response(busCharacterTypeService.viewCharacterById(id));
    }


    @RequestMapping(value = "/updateCharacterType",method = RequestMethod.PUT)
    @ApiOperation(value = "修改性状类型接口后端接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "BusCharacterType" ,value = "BusCharacterType传输展示对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> updateCharacterType(@RequestBody BusCharacterType busCharacterType){
        busCharacterTypeService.updateCharacterType(busCharacterType);
        return response();
    }


    @ApiOperation(value = "批量删除性状类型接口，逻辑删除")
    @RequestMapping(value = "/batchdelete", method = RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "批量性状信息")
    })
    public ResponseEntity<ApiResponse> batchDelete(@RequestBody List<Integer> ids) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            busCharacterTypeService.batchUpdateCharacterType(ids);
            apiResponse.setStatus(ApiResponse.STATUS_OK);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (ApiException e) {
            apiResponse.setStatus(ApiResponse.STATUS_FAIL);
            apiResponse.setErrorCode(e.getErrorCode());
            apiResponse.setErrorDescription(e.getErrorDescription());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }


    @ApiOperation(value = "性状类型对象上移动")
    @RequestMapping(value = "/characterType/updateUp", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busCharacterType",value = "busCharacterType对应的修改值")
    })
    public ResponseEntity<ApiResponse> updateUp(@RequestBody BusCharacterType busCharacterType) {
        busCharacterTypeService.updateUp(busCharacterType);
        return response();
    }

    @ApiOperation(value = "性状类型对象下移动")
    @RequestMapping(value = "/characterType/updateDown", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busCharacterType",value = "busCharacterType对应的修改值")
    })
    public ResponseEntity<ApiResponse> updateDown(@RequestBody BusCharacterType busCharacterType) {
        busCharacterTypeService.updateDown(busCharacterType);
        return response();
    }

}
