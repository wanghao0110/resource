package com.xnfh.cec.repository;

import com.xnfh.entity.BusVarietyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/25
 */
@Repository
public interface BusVerietyRepository extends JpaRepository<BusVarietyType, Integer> {

    /**
     *查看用户端的登记成功的种质资源
     * @param verietyVoVarietyName
     * @return
     */
    BusVarietyType findBusVarietyTypeByVarietyName(String verietyVoVarietyName);

    /**
     * 查询种质类型名称
     * @param varietyTypeId
     * @return
     */
    BusVarietyType findBusVarietyTypeByVarietyId(Integer varietyTypeId);
}
