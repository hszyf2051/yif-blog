package com.yif.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yif.dao.mapper.SysUserMapper;
import com.yif.pojo.SysUser;
import com.yif.pojo.vo.ErrorCode;
import com.yif.pojo.vo.LoginUserVo;
import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.UserVo;
import com.yif.service.LoginService;
import com.yif.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("码神之路");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("小凡");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysUser::getAccount,account);
        lqw.eq(SysUser::getPassword,password);
        lqw.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        lqw.last("limit 1");

        return sysUserMapper.selectOne(lqw);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser>  lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysUser::getAccount,account);
        lqw.last("limit 1");
        return this.sysUserMapper.selectOne(lqw);
    }

    @Override
    public void save(SysUser sysUser) {
        // 保存用户 id会自动生成
        // 这个地方默认生成的id是分布式id 雪花算法
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public ResultVo getUserInfoByToken(String token) {
        /**
         * 1、token合法性校验
         *    是否为空，redis是否存在
         * 2、如果校验失败 返回错误
         * 3、如果成功 返回对应的结果 LoginUserVo
         */
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null) {
            ResultVo.fail(ErrorCode.TOKEN_NOT_EXIST.getCode(),ErrorCode.TOKEN_NOT_EXIST.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());
        return ResultVo.success(loginUserVo);

    }
}
