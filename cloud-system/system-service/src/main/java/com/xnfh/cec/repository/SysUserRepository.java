package com.xnfh.cec.repository;

import com.xnfh.entity.SysOperLog;
import com.xnfh.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/19
 */

@Mapper
public interface SysUserRepository extends JpaRepository<SysUser, Integer> {

    /**
     *账户登陆
     * @param accountName
     * @return
     */
    @Transactional
    @Query(value ="SELECT a FROM SysUser a WHERE a.accountName=?1")
    SysUser getSysUserByAccountNameAndPassword(String accountName);

    /**
     * 查询用户端的用户信息
     * @param accountName
     * @return
     */
    SysUser findSysUserByAccountName(String accountName);

    /**
     * 查询单位地址
     * @param address
     * @return
     */
    SysUser findSysUserByBusiness(String address);
}
