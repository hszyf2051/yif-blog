package com.yif.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yif.dao.mapper.ArticleBodyMapper;
import com.yif.dao.mapper.ArticleMapper;
import com.yif.dao.mapper.ArticleTagMapper;
import com.yif.pojo.Article;
import com.yif.pojo.ArticleBody;
import com.yif.pojo.ArticleTag;
import com.yif.pojo.SysUser;
import com.yif.pojo.vo.ArticleBodyVo;
import com.yif.pojo.vo.TagVo;
import com.yif.pojo.vo.dos.Archives;
import com.yif.pojo.vo.params.ArticleParam;
import com.yif.service.*;
import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.ArticleVo;
import com.yif.pojo.vo.params.PageParams;
import com.yif.utils.UserThreadLocal;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ThreadService threadService;

    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public ResultVo listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        return ResultVo.success(copyList(articleIPage.getRecords(),true,true));
    }

//    @Override
//    public ResultVo listArticle(PageParams pageParams) {
//        /**
//         * 分页查询article数据库表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId() != null) {
//            // and category_id = #{category_id}
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null) {
//            // 加入标签 条件查询
//            // article中并没有tagId字段，一篇文章可能对应多个标签
//            // article_tag表中 article_id 1:n tag_id
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            // 查询出所有的标签
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                // 根据之前查出来的tagId在article_tag表中查询对应的article_id
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0) {
//                // and id in(articleIdList)
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//
//        }
//        // 置顶排序
//        // order by create_date desc
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        return ResultVo.success(articleVoList);
//    }

    /**
     * 最热文章
     * @param limit
     * @return
     */
    @Override
    public ResultVo hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 根据浏览量排序
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        /**
         * select id,title from article order by view_count desc limit 5
         */
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return ResultVo.success(copyList(articles,false,false));
    }

    /**
     * 最新文章
     * @param limit
     * @return
     */
    @Override
    public ResultVo newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 根据创建时间排序
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        // select id,title from article order by create_date desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return ResultVo.success(copyList(articles,false,false));
    }

    /**
     * 文章归档
     * @return
     */
    @Override
    public ResultVo listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return ResultVo.success(archivesList);
    }

    /**
     * 查询文章详情
     * @param articleId
     * @return
     */
    @Override
    public ResultVo findArticleById(Long articleId) {
        /**
         * 1、根据id查询 文章信息
         * 2、根据bodyId和categoryid 去关联查询
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article,true,true,true,true);
        // 查看完文章之后，本应该直接返回数据，此时做一个更新操作，更新时加入写锁，阻塞其他读操作，性能会比较低
        // 更新 增加了此次接口的 耗时 */优化* 如果一旦更新出现问题 不能影响查看文章的操作
        // 线程池 可以把更新操作 扔到线程池中去执行 和主线程不相关了
        /**
         * updateArticleViewCount方法为浏览量
         */
//        threadService.updateArticleViewCount(articleMapper,article);
        return ResultVo.success(articleVo);
    }

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    @Override
    public ResultVo publish(ArticleParam articleParam) {
        // 从线程中取到用户
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1、发布文章 构建Article对象
         * 2、作者id 当前登录用户
         * 3、标签 要将标签加入列 关联到表中
         * 4、body内容存储 article bodyId
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCategoryId(articleParam.getCategory().getId());


        // 插入之后，会生成一个文章id
        // insert后主键会自动set到实体id字段
        this.articleMapper.insert(article);
        // tag 根据标签的内容关联表
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
            // body
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        // 插入之后才会有bodyId
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        // 将数据更新
        articleMapper.updateById(article);
        // "data": {"id":12232323} 返回类型
        Map<String, String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return ResultVo.success(map);
    }

    /**
     * 获取文章详情
     * @param articleId
     * @return
     */
    @Override
    public Article getArticleById(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        return article;
    }

    /**
     * 获取所有文章详情
     * @return
     */
    @Override
    public List<Article> findArticleAll() {
        List<Article> articleList = articleMapper.selectList(null);
        return articleList;
    }

    /**
     * 更新（浏览量，评论数）
     * @param article
     * @return
     */
    @Override
    public Boolean updateNumById(Article article) {
        return articleMapper.updateNumById(article) > 0;
    }


    /**
     * 通过id查看文章内容详情
     * @param bodyId
     * @return
     */
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }


    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        // 将Long类型的时间转化为string类型
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-mm-dd HH:mm"));
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }


}
