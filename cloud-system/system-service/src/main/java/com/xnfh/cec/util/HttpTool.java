package com.xnfh.cec.util;


import cn.hutool.core.bean.BeanUtil;
import com.xnfh.cec.jwt.AccountProfile;

import com.xnfh.cec.jwt.JwtToken;
import com.xnfh.cec.service.SysUserService;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.ResponseSmsSendVo;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import java.io.*;
import java.util.Random;

import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.Name;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/20
 */
@Service
public class HttpTool{

    private String apikey = "c4b508483c78554380fbb69cf78e1e1e";

    private String text = "【兴农丰华】您的验证码是****。如非本人操作，请忽略本短信";

    public ResponseSmsSendVo httpPost(String url) throws Exception{
        //请求内容	json格式的参数，可以将我们要发送的内容转换为json格式
        String paramsJson = "";
        //客户端实例化
        HttpClient client = new HttpClient();
        int code = 0;
        //请求方法post，可以将请求路径传入构造参数中
        PostMethod postMethod = new PostMethod(url);
        String property = null;
        //设置请求头
            try {
                if(text.contains("****")){
                    code = new Random().nextInt(9999);
                    String substring = text.substring(0, 12);
                    String str = String.valueOf(code);
                    String substring1 = text.substring(16, 30);
                    property = (substring + str + substring1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        postMethod.addRequestHeader("Content-type","application/x-www-form-urlencoded;charset=UTF-8");
        postMethod.setParameter("apikey",apikey);
        postMethod.setParameter("text",property);
        postMethod.setParameter("mobile","13088710768");
        //将参数转为二进制
        byte[] requestBytes = paramsJson.getBytes("utf-8");
        InputStream inputStream = new ByteArrayInputStream(requestBytes,0,requestBytes.length);
        //设置请求体
        RequestEntity requestEntity = new InputStreamRequestEntity(inputStream,requestBytes.length,"application/json;charset=utf-8");

        //执行方法	这里可以根据请求状态判断请求是否成功，然后根据第三方接口返回的数据格式，解析出我们需要的数据
        int i = client.executeMethod(postMethod);
        paramsJson = postMethod.getResponseBodyAsString();
        //得到响应数据
        byte[] responseBody = postMethod.getResponseBody();
        String s = new String(responseBody);
        ResponseSmsSendVo responseSmsSendVo = new ResponseSmsSendVo();
        responseSmsSendVo.setPhone("13088710768");
        responseSmsSendVo.setCode(code);
        return responseSmsSendVo;
    }

}

