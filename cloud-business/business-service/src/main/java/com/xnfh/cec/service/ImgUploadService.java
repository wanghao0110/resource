package com.xnfh.cec.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
public interface ImgUploadService {
    /**
     * 处理图片上传
     * @param file
     */
    void getImgUpload(MultipartFile file);
}
