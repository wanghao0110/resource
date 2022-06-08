package com.xnfh.cec.repository;

import com.xnfh.cec.repository.specification.CharacterSpecification;
import com.xnfh.entity.BusCharacterType;
import com.xnfh.entity.BusPlantResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/7
 */
public interface BusCancelRegisterAuditRepository extends JpaRepository<BusPlantResource,Integer>,JpaSpecificationExecutor<BusPlantResource> {

}
