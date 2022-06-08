package com.xnfh.cec.repository;

import com.xnfh.entity.BusCharacterType;
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
 * @Author wanghaohao ON 2022/6/2
 */
@Repository
public interface BusCharacterTypeRepository extends JpaRepository<BusCharacterType,Integer>, JpaSpecificationExecutor<BusCharacterType> {

    /**
     * 查询是否已经存在这个性状名称
     * @param characterTypeName
     * @return
     */
    BusCharacterType findBusCharacterTypeByCharacterTypeName(String characterTypeName);

    /**
     * 查询最大的orderNum数
     * @return
     */
    @Query(value = "select max(orderNum) from BusCharacterType  ")
    Integer selectMaxorderNum();

    /**
     * 上移需要查询当前需要修改的
     * @param characterTypeId
     * @return
     */
    BusCharacterType findBusCharacterTypeByCharacterTypeId(Integer characterTypeId);
}
