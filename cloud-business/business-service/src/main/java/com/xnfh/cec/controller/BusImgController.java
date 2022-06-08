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


/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
@RestController
@Api(tags = "图片上传服务器")
@RequestMapping(value = "/v1/xnfh/img")
@Slf4j
public class BusImgController extends CCAbstractController {


    @Autowired
    private ImgUploadService imgUploadService;

    @ApiOperation(value = "图片上传")
    @RequestMapping(value = "/uploadImg",method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> getImgUpload(@RequestParam("files") MultipartFile file) {
        imgUploadService.getImgUpload(file);
        return response();

    }

}
