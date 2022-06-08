package com.xnfh.cec.service;

import com.xnfh.entity.BusExpertPlant;
import com.xnfh.vo.BusExpertPlantVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/1
 */
public interface BusExpertPlantService {

    /**
     * 查询专家作物分配信息
     * @param busExpertPlantVo
     * @param pageRequest
     * @return
     */
    Page<BusExpertPlant> getExpertPlantPage(BusExpertPlantVo busExpertPlantVo, Pageable pageRequest);

    /**
     * 作家配置作物导出数据
     * @param request
     * @param response
     */
    void export(HttpServletRequest request, HttpServletResponse response, Page<BusExpertPlant> busExpertPlantPage);



}
