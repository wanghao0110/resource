package com.xnfh.cec.controller;

import com.xnfh.common.ApiResponse;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public abstract class CCAbstractController extends AbstractController {

    protected ResponseEntity<ApiResponse> response() {
        return new ResponseEntity<>(ApiResponse.get(ApiResponse.STATUS_OK), HttpStatus.OK);
    }

    protected ResponseEntity<ApiResponse> response(Object object) {
        if (object == null) {
            throw new ApiException(ExceptionDefinition.RESPONSE_BODY_EMPTY_ERROR_204);
        }

        ApiResponse response = ApiResponse.get(ApiResponse.STATUS_OK);
        response.setEntity(object);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ResponseEntity<ApiResponse> responsePage(Page page) {
        if (page == null || page.getContent().isEmpty()) {
            throw new ApiException(ExceptionDefinition.RESPONSE_BODY_EMPTY_ERROR_204);
        }

        ApiResponse response = ApiResponse.get(ApiResponse.STATUS_OK);
        response.setEntities(page.getContent());
        BeanUtils.copyProperties(page, response);
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ResponseEntity<ApiResponse> response(List<?> entities) {
        if (entities == null || entities.isEmpty()) {
            throw new ApiException(ExceptionDefinition.RESPONSE_BODY_EMPTY_ERROR_204);
        }

        ApiResponse response = ApiResponse.get(ApiResponse.STATUS_OK);
        response.setEntities(entities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ResponseEntity<ApiResponse> response(Object object, String errorCode, String errorDescription) {
        if (object == null) {
            throw new ApiException(ExceptionDefinition.RESPONSE_BODY_EMPTY_ERROR_204);
        }

        ApiResponse response = ApiResponse.get(ApiResponse.STATUS_OK);
        response.setEntity(object);
        response.setErrorCode(Integer.valueOf(errorCode));
        response.setErrorDescription(errorDescription);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
