package com.yif.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yif.dao.mapper.CategoryMapper;
import com.yif.pojo.Category;
import com.yif.pojo.vo.CategoryVo;
import com.yif.pojo.vo.ResultVo;
import com.yif.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    /**
     * 查找所有类别
     * @return
     */
    @Override
    public ResultVo findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);

        // 页面交互的对象
        return ResultVo.success(copyList(categories));
    }

    /**
     * 查询全部信息
     * @return
     */
    @Override
    public ResultVo findAllDetails() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        //页面交互的对象
        return ResultVo.success(copyList(categories));

    }

    /**
     * 根据id查询文章分类
     * @param id
     * @return
     */
    @Override
    public ResultVo categoryDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return ResultVo.success(category);
    }

    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

}
