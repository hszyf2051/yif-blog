package com.yif.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yif.dao.mapper.CommentMapper;
import com.yif.pojo.Comment;
import com.yif.pojo.SysUser;
import com.yif.pojo.vo.CommentVo;
import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.UserVo;
import com.yif.pojo.vo.params.CommentParam;
import com.yif.service.CommentsService;
import com.yif.service.SysUserService;
import com.yif.utils.UserThreadLocal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;


    @Override
    public ResultVo commentsByArticleId(Long id) {
        /**
         * 1、根据文章id 查询评论列表 从comment列表查询
         * 2、根据作者的id 查询作者的信息
         * 3、判断 如果level = 1 要去查询它有没有子评论
         * 4、 如果有 根据评论id 进行查询（parent_id)
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return ResultVo.success(commentVoList);
    }

    @Override
    public ResultVo comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();    // 取到登录时的用户
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return ResultVo.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        // 作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        // 子评论
        Integer level = comment.getLevel();
        if (1 == level) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentByParentId(id);
            commentVo.setChildrens(commentVoList); // childrens为子评论的封装数据
        }
        // to User 给谁评论
        if (level > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    /**
     * 查找子评论
     * @param id
     * @return
     */
    private List<CommentVo> findCommentByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}
