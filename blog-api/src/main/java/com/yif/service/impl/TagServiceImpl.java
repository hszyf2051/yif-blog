package com.yif.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yif.dao.mapper.TagMapper;
import com.yif.pojo.Tag;
import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.TagVo;
import com.yif.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        // mybatis-plus无法支持多表查询，需要自己写方法
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public ResultVo hots(int limit) {
        /**
         * 1.标签所拥有的文章数量最多，最热标签
         * 查询 根据tag_id 分组 计数 从小到大 排列 取前limit个
         */
        List<Long> tagIds = tagMapper.findHotsTagsIds(limit);
        // 查询结果为空
        if (CollectionUtils.isEmpty(tagIds)) {
            return ResultVo.success(Collections.emptyList());
        }else {
            // 需求的是 tagId 和 tagName Tag对象
            /**
             * 通过获取的tagIds查询数据库中tag标签里的信息
             */
            List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
            return ResultVo.success(tagList);
        }
    }

    /**
     * 查询所有标签
     * @return
     */
    @Override
    public ResultVo findAll() {
        LambdaQueryWrapper<Tag> lqw = new LambdaQueryWrapper<>();
        lqw.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(lqw);
        return ResultVo.success(copyList(tags));
    }

    /**
     * 查询所有标签详情
     * @return
     */
    @Override
    public ResultVo findAllDetails() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<>());
        return ResultVo.success(copyList(tags));
    }

    /**
     * 根据id查询标签
     * @param id
     * @return
     */
    @Override
    public ResultVo findAllDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return ResultVo.success(copy(tag));
    }

    private List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;

    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }

}
