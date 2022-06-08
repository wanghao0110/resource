package com.xnfh.cec.repository;

import com.xnfh.entity.BusPlantResource;
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
public interface SysUserPersonalRepository extends JpaRepository<SysUser, Integer>, JpaSpecificationExecutor<SysUser> {

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    SysUser findSysUserByUserId(Integer id);
}
