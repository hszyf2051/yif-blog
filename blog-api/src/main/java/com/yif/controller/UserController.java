package com.yif.controller;

import com.yif.pojo.vo.ResultVo;
import com.yif.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录后查询用户信息
     * @param token
     * @return
     */
    @GetMapping("/currentUser")
    public ResultVo currentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.getUserInfoByToken(token);
    }
}
