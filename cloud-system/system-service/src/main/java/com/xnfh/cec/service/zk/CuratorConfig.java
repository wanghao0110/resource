package com.xnfh.cec.service.zk;


import org.apache.curator.framework.CuratorFramework;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/19
 */
@Component
@Mapper
public interface CuratorConfig extends CuratorFramework {
    //做第三方的转入
}
