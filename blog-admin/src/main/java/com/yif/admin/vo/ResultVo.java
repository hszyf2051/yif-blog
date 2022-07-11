package com.yif.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultVo {

    private boolean success;

    private int code;

    private String msg;

    private Object data;


    public static ResultVo success(Object data){
        return new ResultVo(true,200,"success",data);

    }

    public static ResultVo fail(int code, String msg){
        return new ResultVo(false,code,msg,null);
    }
}
