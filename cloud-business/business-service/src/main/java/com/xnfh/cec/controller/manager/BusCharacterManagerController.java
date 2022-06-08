package com.xnfh.cec.controller.manager;

import com.xnfh.cec.controller.CCAbstractController;
import com.xnfh.cec.repository.specification.CharacterManagerSpecification;
import com.xnfh.cec.repository.specification.CharacterSpecification;
import com.xnfh.cec.service.CharacterManagerService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusCharacterManager;
import com.xnfh.entity.BusCharacterType;
import com.xnfh.exception.ApiException;
import com.xnfh.vo.CharacterManagerVo;
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
 * @Author wanghaohao ON 2022/6/6
 */
@RestController
@Api(tags = "性状名称控制器")
@RequestMapping(value = "/v1/xnfh/characterManager")
public class BusCharacterManagerController extends CCAbstractController {


    @Autowired
    private CharacterManagerService characterManagerService;

    @RequestMapping(value = "/pageList",method = RequestMethod.GET)
    @ApiOperation(value = "性状名称分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "characterManagerVo" ,value = "characterManagerVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> findCharacterManagerPage(CharacterManagerVo characterManagerVo,
                                                                @RequestParam(required = false) String orderBy,
                                                                @PageableDefault(page = 0, size = 10) Pageable pageable){


        characterManagerVo.setOrderBy(orderBy);
        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), CharacterManagerSpecification.getSort(characterManagerVo));
        Page<BusCharacterManager> busCharacterManagerPage =  characterManagerService.getCharacterManagerPage(characterManagerVo,pageRequest);
        return responsePage(busCharacterManagerPage);
    }


    @ApiOperation(value = "执行添加性状名称操作")
    @RequestMapping(value = "/addCharacterManager" ,method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busCharacterManager" , value = "busCharacterManager接收前端传值")
    })
    public ResponseEntity<ApiResponse> addCharacterManager(@RequestBody BusCharacterManager busCharacterManager){
        characterManagerService.addCharacterManager(busCharacterManager);
        return response();
    }


    @ApiOperation(value = "执行编辑性状前的查询名称操作")
    @RequestMapping(value = "/updatePreCharacterManager/{characterManagerId}" ,method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "characterTypeId" , value = "characterTypeId接收前端传值")
    })
    public ResponseEntity<ApiResponse> updatePreCharacterManager(@PathVariable("characterManagerId") Integer characterTypeId){
        BusCharacterManager busCharacterManager = characterManagerService.updatePreCharacterManager(characterTypeId);
        return response(busCharacterManager);
    }

    @ApiOperation(value = "执行编辑性状的操作")
    @RequestMapping(value = "/updateCharacterManager" ,method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "characterTypeId" , value = "characterTypeId接收前端传值")
    })
    public ResponseEntity<ApiResponse> updateCharacterManager(@RequestBody BusCharacterManager busCharacterManager){
        characterManagerService.updateCharacterManager(busCharacterManager);
        return response();
    }



    @ApiOperation(value = "批量删除接口，逻辑删除")
    @RequestMapping(value = "/batchdelete", method = RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "批量性状信息")
    })
    public ResponseEntity<ApiResponse> batchDelete(@RequestBody List<Integer> ids) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            characterManagerService.batchDelete(ids);
            apiResponse.setStatus(ApiResponse.STATUS_OK);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (ApiException e) {
            apiResponse.setStatus(ApiResponse.STATUS_FAIL);
            apiResponse.setErrorCode(e.getErrorCode());
            apiResponse.setErrorDescription(e.getErrorDescription());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "性状名称管理上移动")
    @RequestMapping(value = "/characterManager/updateUp", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busCharacterManager",value = "busCharacterManager对应的修改值")
    })
    public ResponseEntity<ApiResponse> updateUp(@RequestBody BusCharacterManager busCharacterManager) {
        characterManagerService.updateUp(busCharacterManager);
        return response();
    }

    @ApiOperation(value = "性状名称管理下移动")
    @RequestMapping(value = "/characterManager/updateDown", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busCharacterManager",value = "busCharacterManager对应的修改值")
    })
    public ResponseEntity<ApiResponse> updateDown(@RequestBody BusCharacterManager busCharacterManager) {
        characterManagerService.updateDown(busCharacterManager);
        return response();
    }

}
