package com.yif.controller;

import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.params.LoginParam;
import com.yif.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResultVo register(@RequestBody LoginParam loginParam) {
        // sso 单点登录 后期如果把注册登录功能提出去（单独的服务，可以独立提供接口服务）
        return loginService.register(loginParam);
    }
}
