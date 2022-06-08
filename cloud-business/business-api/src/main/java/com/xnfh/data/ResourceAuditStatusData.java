package com.xnfh.data;

import lombok.Data;

import java.io.Serializable;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/26
 */
@Data
public class ResourceAuditStatusData  implements Serializable {

        private Integer successResource;

        private Integer toAuditResource;

        private Integer NosuccessResource;

        private Integer cancelResource;

}
