package com.yif.service;

import com.yif.pojo.SysUser;
import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LoginService {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    ResultVo login(LoginParam loginParam);

    /**
     * 注册功能
     * @param loginParam
     * @return
     */
    ResultVo register(LoginParam loginParam);

    /**
     * 检查token是否合法
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    ResultVo logout(String token);
}
