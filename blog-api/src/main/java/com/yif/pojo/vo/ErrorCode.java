package com.yif.pojo.vo;

public enum  ErrorCode {

    PARAMS_ERROR(10001,"参数有误"),
    ACCOUNT_PWD_NOT_EXIST(10002,"用户不存在，请重新登录"),
    ACCOUNT_PWD_ERROR(10003,"密码输入错误"),
    TOKEN_ILLEGAL(10004,"token不合法"),
    TOKEN_NOT_EXIST(10005,"token不存在"),
    ACCOUNT_EXIST(10006,"账号已存在"),
    NO_PERMISSION(70001,"无访问权限"),
    SESSION_TIME_OUT(90001,"会话超时"),
    NO_LOGIN(90002,"未登录"),;

    private int code;
    private String msg;

    ErrorCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}