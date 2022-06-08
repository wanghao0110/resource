package com.xnfh.cec.repository;

import com.xnfh.entity.BusCharacterManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/6
 */
@Repository
public interface BusCharacterManagerRespository extends JpaRepository<BusCharacterManager,Integer>, JpaSpecificationExecutor<BusCharacterManager> {

    /**
     * 查询最大的值
     * @return
     */
    @Query(value = "select max(orderNum) from BusCharacterManager ")
    Integer selectMaxOrderNum();

    /**
     * 查询需要编辑的数据
     * @param characterManagerId
     * @return
     */
    BusCharacterManager findBusCharacterManagerByCharacterManagerId(Integer characterManagerId);

    BusCharacterManager findBusCharacterManagerByOrderNum(int orderNum);
}
