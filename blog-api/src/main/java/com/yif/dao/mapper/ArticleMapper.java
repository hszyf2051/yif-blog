package com.yif.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yif.pojo.Article;
import com.yif.pojo.vo.dos.Archives;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 文章归档
     * @return
     */
    List<Archives> listArchives();

    /**
     * 查询文章
     * @param page
     * @param categoryId
     * @param tagId
     * @param year
     * @param month
     * @return
     */
    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month
    );

    /**
     * 更新（浏览量，评论数）
     * @param article
     * @return
     */
    int updateNumById(Article article);
}
