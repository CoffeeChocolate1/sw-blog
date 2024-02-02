package com.sw.blog.service;


import com.sw.blog.vo.ArticleVo;
import com.sw.blog.vo.Result;
import com.sw.blog.vo.params.ArticleParam;
import com.sw.blog.vo.params.PageParams;

import java.util.List;

public interface ArticleService {
    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    //Result listArticle(PageParams pageParams);
    //List<ArticleVo> listArticlesPage(PageParams pageParams);
    Result listArticlesPage(PageParams pageParams);

    Result hotArticle(int limit);

    Result newArticles(int limit);

    Result listArchives();

    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);

    /**
     * 最热文章
     * @param limit
     * @return
     */
//    Result hotArticle(int limit);
//
//    /**
//     * 最新文章
//     * @param limit
//     * @return
//     */
//    Result newArticles(int limit);
//
//    /**
//     * 文章归档
//     * @return
//     */
//    Result listArchives();
//
//    /**
//     * 查看文章详情
//     * @param articleId
//     * @return
//     */
//    Result findArticleById(Long articleId);
//
//    /**
//     * 文章发布服务
//     * @param articleParam
//     * @return
//     */
//    Result publish(ArticleParam articleParam);
//
//    /**
//     * 文章搜索
//     * @param search
//     * @return
//     */
//    Result searchArticle(String search);
}
