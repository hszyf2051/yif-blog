package com.yif.service;


import com.yif.pojo.Article;
import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.params.ArticleParam;
import com.yif.pojo.vo.params.PageParams;

import java.util.List;

public interface ArticleService {
    /**
     * 分页查询实现文章列表
     * @param pageParams
     * @return
     */
    ResultVo listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    ResultVo hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    ResultVo newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    ResultVo listArchives();

    /**
     * 查询文章详情
     * @param articleId
     * @return
     */
    ResultVo findArticleById(Long articleId);

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    ResultVo publish(ArticleParam articleParam);

    /**
     * 获取文章详情
     * @param articleId
     * @return
     */
    Article getArticleById(Long articleId);

    /**
     * 获取所有文章详情
     * @return
     */
    List<Article> findArticleAll();

    /**
     * 更新（浏览量，评论数）
     * @param article
     */
    Boolean updateNumById(Article article);
}
