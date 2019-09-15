package com.example.demo;

import com.fasterxml.jackson.annotation.JsonInclude;

public class JsonResult<T> {
    @JsonInclude(value= JsonInclude.Include.ALWAYS)
    private Integer state;
    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(value= JsonInclude.Include.ALWAYS)
    private T data;

    public JsonResult() {
    }

    public JsonResult(Integer state) {
        this.state = state;
    }

    public JsonResult(Integer state, String message) {
        super();
        this.state = state;
        this.message = message;
    }

    public JsonResult(Integer state, T data) {
        super();
        this.state = state;
        this.data = data;
    }

    public JsonResult(String message) {
        this.message = message;
    }

    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
