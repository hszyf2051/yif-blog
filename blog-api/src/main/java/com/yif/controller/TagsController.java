package com.yif.controller;

import com.yif.pojo.vo.ResultVo;
import com.yif.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagsController {
    @Autowired
    private TagService tagService;

    /**
     * 最热标签
     * @return
     */
    @GetMapping("/hot")
    public ResultVo hot() {
        int limit = 6;
        return tagService.hots(limit);
    }

    /**
     * 查询所有文章标签
     */
    @GetMapping
    public ResultVo findAll() {
        return tagService.findAll();
    }

    @GetMapping("/detail")
    public ResultVo findAllDetails() {
        return tagService.findAllDetails();
    }

    @GetMapping("/detail/{id}")
    public ResultVo findAllDetailById(@PathVariable Long id) {
        return tagService.findAllDetailById(id);
    }
}
