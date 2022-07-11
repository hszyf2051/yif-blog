package com.yif.service;

import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.params.CommentParam;

public interface CommentsService {
    /**
     * 根据文章id查询所有评论的表
     * @param id
     * @return
     */
    ResultVo commentsByArticleId(Long id);

    /**
     * 添加评论
     * @param commentParam
     * @return
     */
    ResultVo comment(CommentParam commentParam);
}
