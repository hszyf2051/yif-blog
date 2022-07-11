package com.yif.service;

import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    ResultVo hots(int limit);

    ResultVo findAll();

    ResultVo findAllDetails();

    ResultVo findAllDetailById(Long id);
}
