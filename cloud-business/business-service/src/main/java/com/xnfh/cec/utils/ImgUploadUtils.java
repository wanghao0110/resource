package com.xnfh.cec.utils;

import cn.hutool.core.date.DateTime;
import com.xnfh.common.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
@Slf4j
public class ImgUploadUtils {

    public String getImgUpload(@RequestParam("files") MultipartFile file) throws Exception {
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
                    System.out.println("文件名称："+fileName);

                    // 添加日期
                    String datePath = new DateTime().toString("yyyy/MM/dd");
                    path = "D:/WorkSpaceInXNHF/resource/cloud-business/business-service/src/main/resources/img/busFeedBackImg/"+datePath+"/";
                    File f = new File(path);
                    if (!f.exists()){
                        f.mkdirs();
                    }
                    String pathName = path + fileName;
                    System.out.println("存放图片文件的路径:" + pathName);
                    // 转存文件到指定的路径
                    file.transferTo(new File(pathName));
                   // return (datePath + "/" + fileName + "." + type);
                   return pathName;
                }

            }else {
                log.error("不是我们想要的文件类型,请按要求重新上传");
            }
        } else {
            log.error("文件类型为空");
        }
        return "ok";
    }
}
