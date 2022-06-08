package com.xnfh.cec.service;

import com.xnfh.entity.BusCharacterManager;
import com.xnfh.vo.CharacterManagerVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/6
 */
public interface CharacterManagerService {

    /**
     * 分页查询性状名称列表
     * @param characterManagerVo
     * @param pageRequest
     * @return
     */
    Page<BusCharacterManager> getCharacterManagerPage(CharacterManagerVo characterManagerVo, Pageable pageRequest);

    /**
     * 添加性状名称
     * @param busCharacterManager
     */
    void addCharacterManager(BusCharacterManager busCharacterManager);

    /**
     * 执行编辑性状前的查询名称操作
     * @param characterTypeId
     * @return
     */
    BusCharacterManager updatePreCharacterManager(Integer characterTypeId);

    /**
     * 执行编辑性状的操作
     * @param busCharacterManager
     */
    void updateCharacterManager(BusCharacterManager busCharacterManager);

    /**
     * 批量删除接口，逻辑删除
     * @param ids
     */
    void batchDelete(List<Integer> ids);

    /**
     * 性状名称管理上移动
     * @param busCharacterManager
     */
    void updateUp(BusCharacterManager busCharacterManager);

    /**
     * 性状名称管理下移动
     * @param busCharacterManager
     */
    void updateDown(BusCharacterManager busCharacterManager);
}
