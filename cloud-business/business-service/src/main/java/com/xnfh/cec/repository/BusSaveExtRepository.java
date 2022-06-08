package com.xnfh.cec.repository;

import com.xnfh.entity.BusSaveExt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@Repository
public interface BusSaveExtRepository  extends JpaRepository<BusSaveExt,Integer> {
}
