package com.xnfh.cec.repository;

import com.xnfh.entity.SysCode;
import com.xnfh.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/18
 */
@Repository
@Mapper
public interface SysCodeRepositroy extends JpaRepository<SysCode, Integer> {


}
