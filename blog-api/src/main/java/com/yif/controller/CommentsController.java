package com.yif.controller;

import com.yif.pojo.vo.ResultVo;
import com.yif.pojo.vo.params.CommentParam;
import com.yif.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    @GetMapping("/article/{id}")
    public ResultVo comments(@PathVariable("id") Long id) {
        return commentsService.commentsByArticleId(id);
    }

    @PostMapping("/create/change")
    public ResultVo comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}
