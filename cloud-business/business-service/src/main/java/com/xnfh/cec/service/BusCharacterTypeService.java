package com.xnfh.cec.service;

import com.xnfh.entity.BusCharacterType;
import com.xnfh.vo.BusCharacterTypeVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/2
 */
public interface BusCharacterTypeService {

    /**
     * 分页查询形状的列表信息
     * @param busCharacterTypeVo
     * @param pageRequest
     * @return
     */
    Page<BusCharacterType> getCharacterTypePage(BusCharacterTypeVo busCharacterTypeVo, Pageable pageRequest);

    /**
     * 添加性状信息数据
     * @param busCharacterType
     */
    BusCharacterType addCharacterType(BusCharacterType busCharacterType);

    /**
     * 查询性状类型接口详情
     * @param id
     * @return
     */
    BusCharacterType viewCharacterById(Integer id);

    /**
     * 修改性状类型接口后端接口
     * @param busCharacterType
     */
    void updateCharacterType(BusCharacterType busCharacterType);

    /**
     *  批量删除性状类型接口
     * @param ids
     */
    void batchUpdateCharacterType(List<Integer> ids);

    /**
     * 修改形状类型的上移
     * @param busCharacterType
     */
    void updateUp(BusCharacterType busCharacterType);

    /**
     * 性状类型下移动
     * @param busCharacterType
     */
    void updateDown(BusCharacterType busCharacterType);
}
