package com.xnfh.cec.service.impl;

import cn.hutool.core.date.DateTime;
import com.xnfh.cec.repository.BusFeedBackRepository;
import com.xnfh.cec.service.ImgUploadService;
import com.xnfh.entity.BusFeedBack;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
@Service
@Slf4j
public class ImgUploadServiceImpl implements ImgUploadService {


    @Autowired
    private BusFeedBackRepository busFeedBackRepository;
    /**
     * 处理图片上传
     * @param file
     */
    @Override
    public void getImgUpload(MultipartFile file) {
        String path = null;
        // 判断上传的文件是否为空
        if (file != null) {
            // 文件类型
            String type = null;
            // 文件原名称
            String fileName = file.getOriginalFilename();
            // 判断文件类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            if (type != null) {
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {
                    String uuid = UUID.randomUUID().toString().replaceAll("-","");
                    fileName = uuid + "."+type;
                    // 添加日期
                    String datePath = new DateTime().toString("yyyy/MM/dd");
                    path = "D:/WorkSpaceInXNHF/resource/cloud-business/business-service/src/main/resources/img/busFeedBackImg/"+datePath+"/";
                    File f = new File(path);
                    if (!f.exists()){
                        f.mkdirs();
                    }
                    String pathName = path + fileName;
                    // 转存文件到指定的路径
                    try {
                        file.transferTo(new File(pathName));
                    } catch (IOException e) {
                       log.error("current operate transferTo error .e :{}",e.getMessage());
                    }
                    saveImgForFeedBack(pathName);
                }
            }else {
                throw new ApiException(ExceptionDefinition.CURRENT_UPLOAD_IMG_NOT_CORRECT_4035);
            }
        } else {
            throw new ApiException(ExceptionDefinition.CURRENT_UPLOAD_IMG_NOT_CORRECT_4035);
        }
    }

    /**
     * 处理图片上传
     * @param pathName
     */
    private void saveImgForFeedBack(String pathName) {
        //先查询最大的id
        int maxId = busFeedBackRepository.findBusFeedBackById();
        BusFeedBack busFeedBackDb = busFeedBackRepository.findBusFeedBackById(maxId);
        busFeedBackDb.setImg(pathName);
        busFeedBackDb.setUpdateTime(new Date());
        busFeedBackRepository.saveAndFlush(busFeedBackDb);
    }
}
