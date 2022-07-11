package com.yif.pojo.vo.params;

import com.yif.pojo.vo.CategoryVo;
import com.yif.pojo.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
