package com.xnfh.cec.service;

import com.xnfh.entity.BusPlant;
import com.xnfh.vo.PlantVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
public interface BusPlantService {

    /**
     * 分页条件查询作物数据
     * @param plantVo
     * @param pageable
     * @return
     */
    Page<BusPlant> getPlantPage(PlantVo plantVo, Pageable pageable);

    /**
     * 新增作物信息
     * @param busPlant
     * @return
     */
    BusPlant externalAdd(BusPlant busPlant);

    /**
     * 编辑作物信息
     * @param busPlant
     */
    void externalUpdate(BusPlant busPlant);

    /**
     * 修改前的查询
     * @param plantId
     * @return
     */
    BusPlant findPlantByPlantId(Integer plantId);

    /**
     * 批量删除作物
     * @param ids
     */
    void batchDelete(List<Integer> ids);

    /**
     * 批量导入作物数据
     * @param file
     * @return
     */
    Map<String, Object> importExcel(MultipartFile file);

    /**
     * 根据名称得到plantId
     * @param plantName
     * @return
     */
    BusPlant findPlantByPlantName(String plantName);
}
