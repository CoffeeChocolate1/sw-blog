package com.sw.blog.service;

import com.sw.blog.vo.CategoryVo;
import com.sw.blog.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
