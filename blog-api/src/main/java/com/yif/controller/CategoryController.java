package com.yif.controller;

import com.yif.pojo.vo.ResultVo;
import com.yif.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询文章分类
     * @return
     */
    @GetMapping
    public ResultVo categories() {
        return categoryService.findAll();
    }

    /**
     * 查询文章分类详情
     * @return
     */
    @GetMapping("/detail")
    public ResultVo categoriesDetails() {
        return categoryService.findAllDetails();
    }

    /**
     * 根据id分类文章
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public ResultVo categoryDetailById(@PathVariable Long id) {
        return categoryService.categoryDetailById(id);
    }
}
