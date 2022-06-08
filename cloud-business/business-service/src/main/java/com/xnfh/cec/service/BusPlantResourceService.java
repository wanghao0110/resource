package com.xnfh.cec.service;

import com.xnfh.data.ResourceAuditStatusData;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/25
 */
public interface BusPlantResourceService {

    /**
     * 新增在线资源登记
     * @param busPlantResource
     */
    void addPlantResource(BusPlantResource busPlantResource);

    /**
     * 查看用户端的批次资源
     * @param plantResourceVo
     * @param pageable
     * @return
     */
    Page<BusPlantResource> viewBatchResource(PlantResourceVo plantResourceVo, Pageable pageable);

    /**
     * 查询用户端的详情操作
     * @param inputNum
     * @return
     */
    BusPlantResource viewBatchResourceByInputNum(String inputNum);

    /**
     * 查看用户端的批次资源审核状态
     * @return
     */
    ResourceAuditStatusData viewResourceByAuditStatus();

    /**
     * 点击‘登记成功资源XXX个’按钮查看登记成功的资源的信息
     * @return
     */
    List<BusPlantResource> viewResourceByAuditStatusSuccess();

    /**
     * 查看用户端的登记成功的种质资源
     * @param plantResourceVerietyVo
     * @param pageable
     * @return
     */
    Page<BusPlantResource> viewResourceVaruety(PlantResourceVerietyVo plantResourceVerietyVo, Pageable pageable);

    /**
     * 查看用户端的登记成功的种质资源详情
     * @param inputNum
     * @return
     */
    BusPlantResource viewSuccessPlantResource(String inputNum);

    /**
     *  用户端撤销登记信息，不可物理删除
     * @param inputNum
     */
    void CancelPlantResource(String inputNum);

    /**
     * 查询管理员段的在线登记资源
     * @param dataAuditResourceVo
     * @param pageable
     * @return
     */
    Page<BusPlantResource> dataAuditResource(DataAuditResourceVo dataAuditResourceVo, Pageable pageable);

    /**
     * 资源审核不通过，需要填写原因
     * @param busPlantResource
     */
    void addResult(BusPlantResource busPlantResource);

    /**
     * 查看数据审核详情
     * @param viewAuditVo
     * @param pageable
     * @return
     */
    Page<BusPlantResource> viewAudit(ViewAuditVo viewAuditVo, Pageable pageable);

    /**
     * 查看详情
     * @param rId
     * @return
     */
    BusPlantResource viewDetail(String rId);

    /**
     * 修改资源标记
     * @param rId
     */
    void resourceClick(String rId);

    /**
     * 在线登记的资源分页查询
     * @param dataViewVo
     * @param pageRequest
     * @return
     */
    Page<BusPlantResource> pageList(DataViewVo dataViewVo, Pageable pageRequest);

    /**
     * 查看登记成功的种质资源
     * @param plantResourceId
     * @return
     */
    BusPlantResource findByPlantIdForCharacter(Integer plantResourceId);
}
