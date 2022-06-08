package com.xnfh.cec.repository;

import com.xnfh.entity.BusExpertPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/1
 */
@Repository
public interface BusExpertPlantRepository extends JpaRepository<BusExpertPlant,Integer>, JpaSpecificationExecutor<BusExpertPlant> {
}
