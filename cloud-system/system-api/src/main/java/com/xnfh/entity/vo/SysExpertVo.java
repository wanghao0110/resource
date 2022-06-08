package com.xnfh.entity.vo;


import lombok.Data;


import java.io.Serializable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
@Data
public class SysExpertVo implements Serializable {


    private String userName;

    private String expertName;

    private String phone;

    private String orderBy;

    private String orderMethod;

}
