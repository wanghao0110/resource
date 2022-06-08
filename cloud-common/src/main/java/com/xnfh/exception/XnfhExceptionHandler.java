package com.xnfh.exception;


import com.xnfh.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author koma <komazhang@foxmail.com>
 * @date 2018-08-29 16:22
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class XnfhExceptionHandler {
    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException e) {
        ApiResponse response = new ApiResponse();
        response.setStatus(e.getStatus());
        response.setErrorCode(e.getErrorCode());
        response.setErrorDescription(e.getErrorDescription());
        log.warn("request {} error, method {}, code {}, {}", request.getRequestURI(), request.getMethod(), e.getErrorCode(), e.getErrorDescription());
        return new ResponseEntity<>(response, e.getHttpStatus());
    }
}
