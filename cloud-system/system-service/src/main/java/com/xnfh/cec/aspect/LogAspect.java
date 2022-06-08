package com.xnfh.cec.aspect;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Date;

import com.xnfh.cec.anno.Log;
import com.xnfh.cec.service.SysOperateService;
import com.xnfh.entity.SysOperLog;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Autowired
    private SysOperateService sysOperateService;

    @Around("@annotation(com.xnfh.cec.anno.Log)")
    public Object recordLog(ProceedingJoinPoint ponit) throws Throwable {
        SysOperLog sysOperate = new SysOperLog();

        MethodSignature methodSignature = (MethodSignature) ponit.getSignature();
        Method proxyMethod = methodSignature.getMethod();
        String methodName = methodSignature.getMethod().getName();
        sysOperate.setOperateAddr(methodName);
        Object object = ponit.getTarget();

        // 真正的要执行该方法是来自一个实际对象的
        Method realMethod = null;
        try {
            realMethod = object.getClass().getMethod(methodName, proxyMethod.getParameterTypes());
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }

        Log log = realMethod.getAnnotation(Log.class);
        String operation = log.name();
        sysOperate.setOperateName(operation);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        // 获取访问的ip地址
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
					throw new ApiException(ExceptionDefinition.GET_CALL_IPADDRESS_NOT_FOUND_4001);
                }
                ipAddress = inet.getHostAddress();
                if (ipAddress == null) {
                    throw new ApiException(ExceptionDefinition.GET_CALL_IPADDRESS_NOT_FOUND_4001);
                }
            } else {
                throw new ApiException(ExceptionDefinition.GET_CALL_IPADDRESS_ERROR_4002);
            }
        }
		String requestHeaderUserAgent = request.getHeader("user-agent");
        if(!StringUtils.isEmpty(requestHeaderUserAgent)){
			sysOperate.setBrowser(requestHeaderUserAgent);
		}
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if(userId.equals("anonymousUser")){
            sysOperate.setUserId(10001);
        }else{
            sysOperate.setUserId(Integer.parseInt(userId));
        }
        sysOperate.setOperateClickTime(new Date());
        sysOperate.setIp(ipAddress);
        String refererHolder = request.getHeader("referer");
        if (refererHolder != null) {
            sysOperate.setUrl(refererHolder);
        }
        Object result = ponit.proceed(ponit.getArgs());
        sysOperate.setCreateTime(new Date());
        sysOperateService.saveOperate(sysOperate);
        return result;
    }
}
