package com.xnfh.cec.repository;

import com.xnfh.entity.BusPlantType;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@Repository
public interface BusPlantTypeRepository extends JpaRepository<BusPlantType, Integer>, JpaSpecificationExecutor<BusPlantType> {


    @Query("select b.typeName from BusPlantType b where b.plantTypeId =  ?1 ")
    String findBusPlantTypeByPlantTypeId(@Param("plantTypeId")Integer plantTypeId);

    @Query("select max(orderNum) from BusPlantType ")
    Integer findBusPlantTypeByMax();

    /**
     * 批量删除作物类型信息
     * @param ids
     */
    @Modifying
    @Transactional
    @Query("delete from BusPlantType where plant_type_id in (?1)")
    void deleteChoice(List<Integer> ids);

    /**
     * 作物类型对象上下移动
     * @param plantTypeId
     * @return
     */
    BusPlantType findByPlantTypeId(Integer plantTypeId);

    /**
     * 查询本条记录的上一条数据
     * @param orderNum
     * @return
     */
    BusPlantType findBusPlantTypeByOrderNum(int orderNum);

    /**
     * 查询是否已经存在
     * @param typeName
     * @return
     */
    @Query("select count(*) from BusPlantType   where typeName =  ?1 ")
    int findBusPlantTypeByTypeName(String typeName);
}
