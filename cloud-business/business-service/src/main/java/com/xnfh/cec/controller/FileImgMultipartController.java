package com.xnfh.cec.controller;

import com.xnfh.cec.service.ImgUploadService;
import com.xnfh.common.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
@RestController
@Api(tags = "图片上传服务器")
@RequestMapping(value = "/v1/xnfh/img")
@Slf4j
public class FileImgMultipartController extends CCAbstractController {

    @ApiOperation(value = "图片上传")
    @RequestMapping(value = "/uploadImg1", method = RequestMethod.POST)
    public String getImgUpload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        List<String> list = new ArrayList<String>();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                // 保存文件
                // 判断文件是否为空
                if (!file.isEmpty()) {
                    try {
                        // D:\WorkSpaceInXNHF\resource\cloud-business\business-service\src\main\resources\img\BusFeedBackFiles
//                        String filePath = request.getSession().getServletContext()
//                                .getRealPath("/")
//                                + "upload/" + file.getOriginalFilename();
                        String filePath =
                                "D:\\WorkSpaceInXNHF\\resource\\cloud-business\\business-service\\src\\main\\resources\\img\\BusFeedBackFiles"
                                + "upload/" + file.getOriginalFilename();
                        list.add(file.getOriginalFilename());
                        File saveDir = new File(filePath);
                        if (!saveDir.getParentFile().exists()) {
                            saveDir.getParentFile().mkdirs();
                        }
                        // 转存文件
                        file.transferTo(saveDir);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "";

    }
}
