package com.xnfh.cec.repository;

import com.xnfh.entity.BusPlantResource;
import com.xnfh.entity.BusVarietyType;
import com.xnfh.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/31
 */
@Repository
public interface SysUserUnitRepository extends JpaRepository<SysUser, Integer>, JpaSpecificationExecutor<SysUser> {

    /**
     * 查询用人单位详情数据
     * @param userId
     * @return
     */
    SysUser findSysUserByUserId(Integer userId);
}
