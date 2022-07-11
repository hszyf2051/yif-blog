package com.yif.controller;

import com.yif.pojo.SysUser;
import com.yif.pojo.vo.ResultVo;
import com.yif.utils.UserThreadLocal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping
    public ResultVo test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return ResultVo.success(null);
    }
}