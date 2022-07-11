package com.yif.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yif.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查询tag
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * 查询最热标签
     * @param limit
     * @return
     */
    List<Long> findHotsTagsIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
