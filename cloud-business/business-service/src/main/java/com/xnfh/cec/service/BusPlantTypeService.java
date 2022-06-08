package com.xnfh.cec.service;

import com.xnfh.entity.BusPlant;
import com.xnfh.entity.BusPlantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
public interface BusPlantTypeService {

    /**
     * 查询种质资源登记作物类型
     * @return
     */
    List<BusPlantType> findBusPlantTypeList();

    /**
     * 根据种质类型查询种植名称
     * @param plantTypeId
     * @return
     */
    String getPlantTypeForName(Integer plantTypeId);

    /**
     * 查询作物类型列表
     * @param plantTypeName
     * @param pageable
     * @return
     */
    Page<BusPlantType> plantTypeList(String plantTypeName, Pageable pageable);

    /**
     * 新增作物类型
     * @param busPlantType
     * @return
     */
    BusPlantType externalAdd(BusPlantType busPlantType);

    /**
     * 编辑作物类型接口
     * @param busPlantType
     */
    void externalUpdate(BusPlantType busPlantType);

    /**
     * 根据主键查询作物类型
     * @param plantTypeId
     * @return
     */
    BusPlantType findPlantTypeByPlantId(Integer plantTypeId);

    /**
     * 批量删除作物类型信息
     * @param ids
     */
    void batchDelete(List<Integer> ids);

    /**
     * 作物类型对象上移动
     * @param busPlantType
     */
    void updateUp(BusPlantType busPlantType);

    /**
     * 作物类型对象下移动
     * @param busPlantType
     */
    void updateDown(BusPlantType busPlantType);
}
