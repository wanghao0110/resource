package com.xnfh.cec.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.xnfh.cec.repository.BusExpertPlantRepository;
import com.xnfh.cec.repository.BusPlantRepository;
import com.xnfh.cec.repository.specification.ExpertPlantSpecification;
import com.xnfh.cec.repository.specification.UnitSpecification;
import com.xnfh.cec.service.BusExpertPlantService;
import com.xnfh.cec.service.BusPlantService;
import com.xnfh.cec.service.SysExpertService;
import com.xnfh.cec.utils.ExportExcel;
import com.xnfh.entity.BusExpertPlant;
import com.xnfh.entity.BusPlant;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.SysUser;
import com.xnfh.vo.BusExpertPlantVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
@Service
@Slf4j
public class BusExpertPlantServiceImpl implements BusExpertPlantService {

    @Autowired
    private BusExpertPlantRepository busExpertPlantRepository;

    @Autowired
    private SysExpertService sysExpertService;

    @Autowired
    private BusPlantRepository busPlantRepository;


    @Override
    public Page<BusExpertPlant> getExpertPlantPage(BusExpertPlantVo busExpertPlantVo, Pageable pageable) {
        Page<BusExpertPlant> busExpertPlantPage = busExpertPlantRepository.findAll(ExpertPlantSpecification.busExpertPlantSpecification(busExpertPlantVo),pageable);
        if(busExpertPlantPage.getContent() != null){
            busExpertPlantPage.getContent().forEach(busExpertPlant -> {
                SysExpert sysExpertDb = sysExpertService.getSysExpertById(busExpertPlant.getExpertId());
                if(sysExpertDb!=null){
                    //获取专家的信息
                    String userName = sysExpertDb.getUserName();
                    String expertName = sysExpertDb.getExpertName();
                    String phone = sysExpertDb.getPhone();
                    busExpertPlant.setTrueName(expertName);
                    busExpertPlant.setPhone(phone);
                    busExpertPlant.setUserName(userName);
                    //获取作物信息
                    String plantId = busExpertPlant.getPlantId();
                    if(!StringUtils.isEmpty(plantId)){
                        if(plantId.contains("、")){
                            String[] split = org.springframework.util.StringUtils.split(plantId, "、");
                            String plantName = null;
                            String sb = "";
                            for (String pId : split) {
                                BusPlant busPlantByPlantId = busPlantRepository.findBusPlantByPlantId(Integer.valueOf(pId));
                                plantName= busPlantByPlantId.getPlantName();
                                sb += plantName+"、";
                            }
                            busExpertPlant.setPlantName(sb.substring(0,sb.length()-1));
                        }else{
                            BusPlant busPlantByPlantId = busPlantRepository.findBusPlantByPlantId(Integer.valueOf(plantId));
                            String plantName = busPlantByPlantId.getPlantName();
                            busExpertPlant.setPlantName(plantName);
                        }
                    }
                }
            });
        }
        //共计的个数
        return new PageImpl<BusExpertPlant>(busExpertPlantPage.getContent(),pageable,busExpertPlantPage.getTotalElements());
    }

    /**
     * 专家配置作物导出
     * @param request
     * @param response
     * @param busExpertPlantPage
     */
    @Override
    public void export(HttpServletRequest request, HttpServletResponse response, Page<BusExpertPlant> busExpertPlantPage) {
        List<BusExpertPlant> busExpertPlantList = busExpertPlantPage.getContent();
        //创建excel表头
        List<String> column = new ArrayList<>();
        column.add("专家用户名");
        column.add("专家名称");
        column.add("专家手机号");
        column.add("配置作物");

        //表头对应的数据
        List<Map<String,Object>> data = new ArrayList<>();

        //遍历获取到的需要导出的数据，k要和表头一样
        for (int i = 0; i < busExpertPlantList.size(); i++) {
            Map<String,Object> dataMap = new HashMap<>();
           BusExpertPlant busExpertPlant = busExpertPlantList.get(i);
            dataMap.put("专家用户名", busExpertPlant.getUserName());
            dataMap.put("专家名称",busExpertPlant.getTrueName());
            dataMap.put("专家手机号",busExpertPlant.getPhone());
            dataMap.put("配置作物",busExpertPlant.getPlantName());
            data.add(dataMap);
        }

        //调用导出工具类
        ExportExcel.exportExcel("专家配置作物",column,data,request,response);
    }

}
