package com.yif.common.aop;

import com.yif.service.ArticleService;
import com.yif.utils.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 定义一个名为"myPointCut()"的切面，getById()这个方法中
     */
    @Pointcut("execution(public * com.yif.controller.ArticleController.findArticleById(..))")
    public void myPointCut() { }

    /**
     * 在这个方法执行后 更新浏览量
     * @param joinPoint
     * @throws Throwable
     */
    @After("myPointCut()")
    public void doAfter(JoinPoint joinPoint) throws Throwable {
        Object[] objs = joinPoint.getArgs();
        Long id = (Long) objs[0];
        //根据id更新浏览量
        redisUtil.zIncrementScore("viewNum", id.toString(), 1);
    }
}
