package com.xnfh.exception;




import com.xnfh.common.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author koma <komazhang@foxmail.com>
 * @date 2018-08-29 17:25
 */
@Getter
public class ApiException extends RuntimeException {
    private HttpStatus httpStatus = HttpStatus.OK;
    private String status = ApiResponse.STATUS_FAIL;
    private ExceptionDefinition definition;
    private String extraDesc = "";

    public ApiException(String status, ExceptionDefinition definition) {
        this.status = status;
        this.definition = definition;
    }

    public ApiException(ExceptionDefinition definition) {
        this.definition = definition;
    }

    public ApiException(ExceptionDefinition definition, HttpStatus httpStatus) {
        this.definition = definition;
        this.httpStatus = httpStatus;
    }

    public ApiException(ExceptionDefinition definition, String desc) {
        this.definition = definition;
        this.extraDesc = desc;
    }

    public Integer getErrorCode() {
        return this.definition.getCode();
    }

    public String getErrorDescription() {
        return this.definition.getDescription()+this.extraDesc;
    }
}
