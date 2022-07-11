package com.yif.handler;

import com.alibaba.fastjson.JSON;
import com.yif.pojo.SysUser;
import com.yif.pojo.vo.ErrorCode;
import com.yif.pojo.vo.ResultVo;
import com.yif.service.LoginService;
import com.yif.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在执行controller方法(Handler) 是否为 HandlerMethod(controller方法)
        /**
         * 1、需要判断 请求的接口路径 是否为 HandlerMethod(controller方法)
         * 2、判断token是否为空，如果为空 未登录
         * 3、如果token不为空，登录验证loginService checkToken
         */
        if (!(handler instanceof HandlerMethod)){
            // handle 可能是 RequestResourceHandler
            // springboot 程序 访问静态资源 默认去classpath下的static目录去查询
            return true;
        }

        String token = request.getHeader("Authorization");

        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (token == null){
            ResultVo result = ResultVo.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            ResultVo result = ResultVo.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        // 登录验证成功，放行
        // 在controller中 直接获取用户的信息
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 如果不删除 ThreadLocal中用完的信息，会有内存泄漏的信息
        UserThreadLocal.remove();
    }
}

