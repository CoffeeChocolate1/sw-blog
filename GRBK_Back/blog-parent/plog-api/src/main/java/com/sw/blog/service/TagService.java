package com.sw.blog.service;

import com.sw.blog.vo.Result;
import com.sw.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);

    //Result hots(int limit);

    /**
     * 查询所有的文章标签
     * @return
     */
//    Result findAll();
//
//    Result findAllDetail();
//
//    Result findDetailById(Long id);
}
