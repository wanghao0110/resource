package com.xnfh.common;



import java.io.Serializable;

public class AjaxResult implements Serializable {

    private Object data;
    private int code;
    private String message;
    public static AjaxResult succ(int code, String message, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMessage(message);
        ajaxResult.setData(data);
        return ajaxResult;
    }
    public static AjaxResult succ(Object data) {
        return succ(200, "操作成功", data);
    }

    public static AjaxResult succ(String msg) {
        return fail(200, msg, null);
    }
    public static AjaxResult fail(int code, String message, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMessage(message);
        ajaxResult.setData(data);
        return ajaxResult;
    }
    public static AjaxResult fail(String msg) {
        return fail(400, msg, null);
    }

    public static AjaxResult fail(String msg, Object data) {
        return fail(400, msg, data);
    }

    public static AjaxResult succ(int size, int size1) {
        return AjaxResult.succ(size,size1);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}