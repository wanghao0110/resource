package com.xnfh.cec.controller.manager;

import com.xnfh.cec.controller.CCAbstractController;
import com.xnfh.cec.repository.specification.ExpertPlantSpecification;
import com.xnfh.cec.repository.specification.ExpertSpecification;
import com.xnfh.cec.service.BusExpertPlantService;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.BusExpertPlant;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.vo.SysExpertVo;
import com.xnfh.vo.BusExpertPlantVo;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/1
 */
@RestController
@Api(tags = "查询专家配置作物信息控制器")
@RequestMapping(value = "/v1/xnfh/expertPlant")
public class BusExpertPlantController extends CCAbstractController {


    @Autowired
    private BusExpertPlantService busExpertPlantService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "查询专家配置作物信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busExpertPlantVo" ,value = "busExpertPlantVo传输展示对象"),
            @ApiImplicitParam(name = "pageable",value = "分页对象")
    })
    public ResponseEntity<ApiResponse> getExpertPlantPage(BusExpertPlantVo busExpertPlantVo,
                                                          @PageableDefault(page = 0, size = 10) Pageable pageable){


        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ExpertPlantSpecification.getSort(busExpertPlantVo));
        Page<BusExpertPlant> busExpertPlantPage =  busExpertPlantService.getExpertPlantPage(busExpertPlantVo,pageRequest);

        return responsePage(busExpertPlantPage);
    }


    //TODO导出excel格式

    /**
     * 导出数据到excel表格
     * @param request
     * @param response
     */
    @RequestMapping(value = "/export")
    @ResponseBody
    @ApiOperation(value = "专家配置作物导出")
    public void export(HttpServletRequest request, HttpServletResponse response,
                                          BusExpertPlantVo busExpertPlantVo,
                                          @PageableDefault(page = 0, size = 10)  Pageable pageable){

        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ExpertPlantSpecification.getSort(busExpertPlantVo));
        Page<BusExpertPlant> busExpertPlantPage = busExpertPlantService.getExpertPlantPage(busExpertPlantVo,pageRequest);
        busExpertPlantService.export(request,response,busExpertPlantPage);

    }


}
