package com.xnfh.cec.controller;

import com.xnfh.cec.anno.Log;
import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.service.SysCodeService;
import com.xnfh.cec.service.SysUserService;
import com.xnfh.cec.util.HttpTool;
import com.xnfh.cec.util.SendSms;
import com.xnfh.common.ApiResponse;
import com.xnfh.entity.dto.LoginDto;
import com.xnfh.entity.vo.ResponseSmsSendVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;



/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/19
 */
@RestController
@Api(tags = "登录接口")
@RequestMapping(value = "/v1/xnfh")
public class LoginController extends CCAbstractController {


    @Autowired
    private SysUserService sysUserService;


    private RestTemplate restTemplate;


    private HttpHeaders httpHeaders;

    @Autowired
    private SysCodeService sysCodeService;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginDto" ,value = "用户对象")
    })
    @Log(name = "用户登录")
    @Permission
    public ResponseEntity<ApiResponse> addUser(@RequestBody LoginDto loginDto) {
        return response(sysUserService.loadUsernameAndPassword(loginDto));
    }


    @ApiOperation(value = "发送短信注册验证码")
    @Log(name = "发送验证码")
    @RequestMapping(value = "/sms/send",method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> sendSms() throws Exception {
        return post("https://sms.yunpian.com/v2/sms/single_send.json");

    }

    private ResponseEntity<ApiResponse> post(String url) throws Exception {
        HttpTool httpTool = new HttpTool();
        ResponseSmsSendVo responseSmsSendVo = httpTool.httpPost(url);
        sysCodeService.saveCode(responseSmsSendVo);
        return response();
    }

}


