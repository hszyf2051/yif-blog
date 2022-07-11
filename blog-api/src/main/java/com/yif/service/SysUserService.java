package com.yif.service;

import com.yif.pojo.SysUser;
import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.UserVo;

public interface SysUserService {
    /**
     * 查找评论人信息
     * @param id
     * @return
     */
    UserVo findUserVoById(Long id);

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据账号查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);


    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    ResultVo getUserInfoByToken(String token);
}
