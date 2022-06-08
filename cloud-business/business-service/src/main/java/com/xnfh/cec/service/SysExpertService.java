package com.xnfh.cec.service;

import com.xnfh.entity.SysExpert;
import com.xnfh.entity.vo.SysExpertVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
public interface SysExpertService {

    /**
     * 查看后端的专家管理
     * @param sysExpertVo
     * @param pageable
     * @return
     */
    Page<SysExpert> expertList(SysExpertVo sysExpertVo, Pageable pageable);

    /**
     * 后端添加专家
     * @param sysExpert
     */
    void addExpert(SysExpert sysExpert);

    /**
     * 查询专家详情
     * @param id
     * @return
     */
    SysExpert getSysExpertById(Integer id);

    /**
     * 修改专家信息
     * @param sysExpert
     */
    void updateSysExpert(SysExpert sysExpert);

    /**
     * 批量删除专家的信息
     * @param ids
     */
    void batchDelete(List<Integer> ids);

    /**
     * 修改专家的状态
     * @param sysExpert
     */
    void updateStatus(SysExpert sysExpert);

    /**
     * 专家管理批量导入接口
     * @param file
     * @return
     */
    Map<String, Object> importExcel(MultipartFile file);
}
