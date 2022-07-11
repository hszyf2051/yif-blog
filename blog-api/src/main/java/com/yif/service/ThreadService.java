package com.yif.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yif.dao.mapper.ArticleMapper;
import com.yif.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    @Async("taskExecutor")
    // 期望此操作在线程池执行，不会影响原有的主线程
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

        /**
         * 乐观锁思想
         */
        int viewCount = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCount + 1);
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        // 设置一个为了在多线程的环境下 线程安全
        updateWrapper.eq(Article::getViewCounts,viewCount); // 检查
        // update article set view_count=100 where view_count=99 and id=1
        articleMapper.update(articleUpdate,updateWrapper);
        try {
            Thread.sleep(5000);
            System.out.println("更新完成了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
