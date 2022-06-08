package com.xnfh.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/6
 */
@Data
public class CharacterManagerVo implements Serializable {


    private String characterName;

    private Integer characterTypeId;

    private String orderBy;

}
