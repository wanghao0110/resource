package com.xnfh.cec.repository;

import com.xnfh.data.ResourceAuditStatusData;
import com.xnfh.entity.BusPlant;
import com.xnfh.entity.BusPlantResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/25
 */

@Repository
public interface BusPlantResourceRepository extends JpaRepository<BusPlantResource, Integer> , JpaSpecificationExecutor<BusPlantResource> {

    /**
     * 根据登记批号查询登记详情
     * @param inputNum
     * @return
     */
    BusPlantResource findBusPlantResourceByInputNum(String inputNum);

    /**
     * resourceAuditStatusData
     * @return
     */

    @Query(value = "select b.auditStatus from BusPlantResource b where b.auditStatus =  0 " , nativeQuery = true)
    Integer viewResourceByAuditStatusWithSuccess();

    @Query(value = "select b.auditStatus from BusPlantResource b where b.auditStatus =  1 " , nativeQuery = true)
    Integer viewResourceByAuditStatusToAudit();

    @Query(value = "select b.auditStatus from BusPlantResource b where b.auditStatus =  2 " , nativeQuery = true)
    Integer viewResourceByAuditStatusWithNoSuccess();

    @Query(value = "select count(1) from BusPlantResource b where b.auditStatus =  3 " , nativeQuery = true)
    Integer viewResourceByAuditStatusCancel();

    /**
     * 点击‘登记成功资源XXX个按钮查看登记成功的资源的信息
     * @return
     */
    @Query(value = "select b.* from BusPlantResource b where b.auditStatus =  1 " , nativeQuery = true)
    List<BusPlantResource> findListByAuditSuccess();

    /**
     * 查询单个详情数据
     * @param rId
     * @return
     */
    @Query(value = "select b.* from bus_plant_resource b where b.r_id = ?1", nativeQuery = true)
    BusPlantResource findBusPlantResourceByRId(String rId);


    /**
     * 查看登记成功的种质资源
     * @param plantResourceId
     * @return
     */
    BusPlantResource findBusPlantResourceByPlantResourceId(Integer plantResourceId);
}
