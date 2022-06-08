package com.xnfh.cec.repository;

import com.xnfh.entity.BusPlantResource;
import com.xnfh.entity.BusSaveResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@Repository
public interface BusSaveResourceTypeRepository extends JpaRepository<BusSaveResourceType,Integer> , JpaSpecificationExecutor<BusSaveResourceType> {


    BusSaveResourceType findBusSaveResourceTypeBySaveResourceId(Integer id) ;

}
