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
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResultVo login(@RequestBody LoginParam loginParam) {
        return loginService.login(loginParam);
    }

}
