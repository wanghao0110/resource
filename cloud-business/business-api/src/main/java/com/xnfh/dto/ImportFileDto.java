package com.xnfh.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/25
 */
@Data
public class ImportFileDto {

    private MultipartFile file;
}
