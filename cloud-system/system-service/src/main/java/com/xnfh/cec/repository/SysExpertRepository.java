package com.xnfh.cec.repository;

import com.xnfh.entity.BusPlant;
import com.xnfh.entity.SysExpert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
@Repository
public interface SysExpertRepository extends JpaRepository<SysExpert, Integer>, JpaSpecificationExecutor<SysExpert> {

    /**
     * 根据username查询数据是否存在
     * @return
     */
    SysExpert getSysExpertByUserName(String userName);

    /**
     * 根据电话号码查询是否存在
     * @return
     */
    SysExpert getSysExpertByPhone(String phone);

    /**
     * 查询专家详情
     * @param id
     * @return
     */
    SysExpert findSysExpertById(Integer id);

    /**
     * 选中的id进行删除
     * @param ids
     */
    @Modifying
    @Transactional
    @Query("delete from SysExpert where id in (?1)")
    void deleteChoice(List<Integer> ids);
}
