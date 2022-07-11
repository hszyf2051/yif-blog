package com.yif.controller;

import com.yif.pojo.vo.ResultVo;
import com.yif.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public ResultVo logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}