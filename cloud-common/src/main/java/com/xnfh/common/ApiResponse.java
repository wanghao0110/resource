package com.xnfh.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author koma <komazhang@foxmail.com>
 * @date 2018-08-28 16:44
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
public class ApiResponse {
    public static final String STATUS_OK = "OK";
    public static final String STATUS_FAIL = "FAIL";
    private String status;
    private Integer errorCode;
    private String errorDescription;
    private List<?> entities;
    private Object entity;
    private Boolean first;
    private Boolean last;
    private Integer size;
    private Integer number;
    private Integer numberOfElements;
    private Integer totalPages;
    private Long totalElements;

    public static ApiResponse get(String status) {
        ApiResponse response = new ApiResponse();
        response.setStatus(status);
        return response;
    }
}
