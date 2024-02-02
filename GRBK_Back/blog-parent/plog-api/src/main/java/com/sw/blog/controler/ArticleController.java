package com.sw.blog.controler;


import com.sw.blog.common.aop.LogAnnotation;
import com.sw.blog.common.cache.Cache;
import com.sw.blog.service.ArticleService;
import com.sw.blog.vo.ArticleVo;
import com.sw.blog.vo.Result;
import com.sw.blog.vo.params.ArticleParam;
import com.sw.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//json数据进行交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


//    @PostMapping("search")
//    public Result search(@RequestBody ArticleParam articleParam){
//        //写一个搜索接口
//        String search = articleParam.getSearch();
//        return articleService.searchArticle(search);
//    }


    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    //加上此注解 代表要对此接口记录日志
    @LogAnnotation(module="文章",operator="获取文章列表")
    //@Cache(expire = 5 * 60 * 1000,name = "listArticle")
    public Result articles(@RequestBody PageParams pageParams) {
        //ArticleVo 页面接收的数据
//        List<ArticleVo> articles = articleService.listArticlesPage(pageParams);
//        return Result.success(articles);
       return articleService.listArticlesPage(pageParams);


    }


    /**
     * 首页 最热文章
     * @return
     */
    @PostMapping("hot")
    //@Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("new")
    //@Cache(expire = 5 * 60 * 1000,name = "news_article")
    public Result newArticles(){
        int limit = 5;
        return articleService.newArticles(limit);
    }

    /**
     * 首页 文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    @PostMapping("view/{id}")
    //@Cache(expire = 5 * 60 * 1000,name = "view_article")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }
    //接口url：/articles/publish
    //
    //请求方式：POST
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){

        return articleService.publish(articleParam);
    }



//    @PostMapping("{id}")
//    public Result articleById(@PathVariable("id") Long articleId){
//        return articleService.findArticleById(articleId);
//    }
}
