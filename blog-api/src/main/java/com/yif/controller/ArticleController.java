package com.yif.controller;

import com.yif.common.aop.LogAnnotation;
import com.yif.common.cache.Cache;
import com.yif.pojo.vo.params.ArticleParam;
import com.yif.service.ArticleService;
import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @author yif
 */
@RestController
@RequestMapping("articles")

public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @LogAnnotation(module = "文章",operation="获取文章列表")
    public ResultVo listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    /**
     * 首页 最热文章
     * @return
     */
    @PostMapping("/hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public ResultVo hostArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("/new")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
    public ResultVo newArticle(){
        int limit = 5;
        return articleService.newArticles(limit);
    }

    /**
     * 首页 文章归档
     * @return
     */
    @PostMapping("/listArchives")
    public ResultVo listArchives(){
        return articleService.listArchives();
    }

    /**
     * 根据id查询文章详情
     * @param articleId
     * @return
     */
    @PostMapping("/view/{id}")
    public ResultVo findArticleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }

    /**
     * 发布文章
     */
    @PostMapping("/publish")
    public ResultVo publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }
}
