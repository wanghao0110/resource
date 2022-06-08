package com.xnfh.cec.repository;


import com.xnfh.entity.BusPlant;
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
@Transactional
public interface BusPlantRepository extends JpaRepository<BusPlant,Integer>, JpaSpecificationExecutor<BusPlant> {

    /**
     * 查询是否重复
     * @param plantName
     * @return
     */
    @Query(value = "select count(1) from BusPlant where plant_name = ?1")
    long selectCount(String plantName);

    /**
     * 批量删除作物信息
     * @param ids
     */
    @Modifying
    @Transactional
    @Query("delete from BusPlant where plant_id in (?1)")
    void deleteChoice(List<Integer> ids);

    /**
     * 根据作物名称查询作物
     * @param plantName
     * @return
     */
    BusPlant findBusPlantByPlantName(String plantName);

    /**
     * 根据作物id查询作物名称
     * @param plantId
     * @return
     */
    BusPlant findBusPlantByPlantId(Integer plantId);


//    /**
//     * 批量插入
//     * @param tblFixChangeList
//     */
//    void insertImportPlant(List<ImportXlsDto> tblFixChangeList);


}
