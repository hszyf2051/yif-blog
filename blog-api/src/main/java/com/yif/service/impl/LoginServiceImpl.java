package com.yif.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yif.pojo.SysUser;
import com.yif.pojo.vo.ErrorCode;
import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.params.LoginParam;
import com.yif.service.LoginService;
import com.yif.service.SysUserService;
import com.yif.utils.JWTUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //加盐
    private static final String slat = "yif!@#";

    @Override
    public ResultVo login(LoginParam loginParam) {
        /**
         * 1、检查参数是否合法
         * 2、根据用户名和密码去user表中查询是否存在
         * 3、如果不存在，登陆失败
         * 4、如果存在，使用jwt生成token 返回给前端
         * 5、token放入redis当中，redis token：user信息 设置过期时间（登录认证的时候 先认证token字符串是否合法，去redis认证是否存在）
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        // isBlank(判断字符串中是否全是空白字符)
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        // 在原有的密码加盐后进行md5加密
        password = DigestUtils.md5Hex(password + slat);
        SysUser userByAccount = sysUserService.findUserByAccount(account);
        // 先查询账户是否存在 如不存在 则提示用户不存在
        if (userByAccount !=null) {
            SysUser sysUser = sysUserService.findUser(account,password); // 不为空，此时判断密码是否正确
            if (sysUser == null){
                return ResultVo.fail(ErrorCode.ACCOUNT_PWD_ERROR.getCode(),ErrorCode.ACCOUNT_PWD_ERROR.getMsg());
            }else {
                String token = JWTUtils.createToken(userByAccount.getId());
                redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(userByAccount),1, TimeUnit.DAYS);
                return ResultVo.success(token);
            }
        }
        // 用户账号不存在，则提示不存在
        return ResultVo.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());

    }

    /**
     * 注册功能
     * @param loginParam
     * @return
     */
    @Override
    public ResultVo register(LoginParam loginParam) {
        /**
         * 1、判断参数是否合法
         * 2、判断账户是否存在 存在 返回账户已经被注册
         * 3、如果不存在，注册用户
         * 4、生成token
         * 5、存入redis，并返回
         * 6、注意加上事务，一旦中间出现错误，进行回滚
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser != null){
            return ResultVo.fail(ErrorCode.ACCOUNT_EXIST.getCode(),"账号已经被注册了！");
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);

        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return ResultVo.success(token);
    }

    /**
     * 校验token是否合法
     * @param token
     * @return
     */
    @Override
    public SysUser checkToken(String token) {
        if(StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null) {
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token); // 通过token 获取json数据
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class); // 把json数据封装成对象
        return sysUser;
    }

    @Override
    public ResultVo logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return ResultVo.success(null);
    }
}
