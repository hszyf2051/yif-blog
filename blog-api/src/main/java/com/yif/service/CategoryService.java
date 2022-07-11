package com.yif.service;

import com.yif.pojo.vo.CategoryVo;
import com.yif.pojo.vo.ResultVo;

public interface CategoryService {
     CategoryVo findCategoryById(Long categoryId);

     ResultVo findAll();

     ResultVo findAllDetails();

     ResultVo categoryDetailById(Long id);
}
