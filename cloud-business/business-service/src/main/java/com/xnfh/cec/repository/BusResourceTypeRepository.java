package com.xnfh.cec.repository;

import com.xnfh.entity.BusResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@Repository
public interface BusResourceTypeRepository extends JpaRepository<BusResourceType,Integer> {

    /**
     * 查询资源类型
     * @param resourceId
     * @return
     */
    BusResourceType findBusResourceTypeByResourceId(Integer resourceId);
}
