package com.sw.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.blog.dao.dos.Archives;
import com.sw.blog.dao.mapper.ArticleBodyMapper;
import com.sw.blog.dao.mapper.ArticleMapper;
import com.sw.blog.dao.mapper.ArticleTagMapper;
import com.sw.blog.dao.pojo.Article;
import com.sw.blog.dao.pojo.ArticleBody;
import com.sw.blog.dao.pojo.ArticleTag;
import com.sw.blog.dao.pojo.SysUser;
import com.sw.blog.service.*;
import com.sw.blog.utils.UserThreadLocal;
import com.sw.blog.vo.*;
import com.sw.blog.vo.params.ArticleParam;
import com.sw.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TagService tagService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
        public Result listArticlesPage(PageParams pageParams) {
        Page<Article> page=new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage=articleMapper.listArticlesPage(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        List<Article> records=articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

//    @Override
//    public Result listArticlesPage(PageParams pageParams) {
//        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        if(pageParams.getCategoryId() !=null){
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleList=new ArrayList<>();
//        if(pageParams.getTagId() !=null){
//            LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag>articleTags=articleTagMapper.selectList(lambdaQueryWrapper);
//            for (ArticleTag articleTag:articleTags) {
//                articleList.add(articleTag.getArticleId());
//            }
//            if(articleList.size()>0){
//                queryWrapper.in(Article::getId,articleList);
//            }
//        }
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        //不一样
//        //return articleVoList;
//        return Result.success(copyList(records,true,true));
//    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id,title from article order by create_date desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }


    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyId和categoryid 去做关联查询
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);
        //查看完文章了，新增阅读数，有没有问题呢？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        // 更新 增加了此次接口的 耗时 如果一旦更新出问题，不能影响 查看文章的操作
        //线程池  可以把更新操作 扔到线程池中去执行，和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);

        String viewCount = (String) redisTemplate.opsForHash().get("view_count", String.valueOf(articleId));
        if (viewCount != null){
            articleVo.setViewCounts(Integer.parseInt(viewCount));
        }
        return Result.success(articleVo);
    }



//    @Override
//    public Result publish(ArticleParam articleParam) {
//        //此接口 要加入到登录拦截当中
//        SysUser sysUser = UserThreadLocal.get();
//        /**
//         * 1. 发布文章 目的 构建Article对象
//         * 2. 作者id  当前的登录用户
//         * 3. 标签  要将标签加入到 关联列表当中
//         * 4. body 内容存储 article bodyId
//         */
//        Article article = new Article();
//        boolean isEdit = false;
//        if (articleParam.getId() != null){
//            article = new Article();
//            article.setId(articleParam.getId());
//            article.setTitle(articleParam.getTitle());
//            article.setSummary(articleParam.getSummary());
//            //article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
//            article.setCategoryId(articleParam.getCategory().getId());
//            articleMapper.updateById(article);
//            isEdit = true;
//        }else{
//            article = new Article();
//            article.setAuthorId(sysUser.getId());
//            article.setWeight(Article.Article_Common);
//            article.setViewCounts(0);
//            article.setTitle(articleParam.getTitle());
//            article.setSummary(articleParam.getSummary());
//            article.setCommentCounts(0);
//            article.setCreateDate(System.currentTimeMillis());
//            //article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
//            article.setCategoryId(articleParam.getCategory().getId());
//            //插入之后 会生成一个文章id
//            this.articleMapper.insert(article);
//        }
//        //tag
//        List<TagVo> tags = articleParam.getTags();
//        if (tags != null){
//            for (TagVo tag : tags) {
//                Long articleId = article.getId();
//                if (isEdit){
//                    //先删除
//                    LambdaQueryWrapper<ArticleTag> queryWrapper = Wrappers.lambdaQuery();
//                    queryWrapper.eq(ArticleTag::getArticleId,articleId);
//                    articleTagMapper.delete(queryWrapper);
//                }
//                ArticleTag articleTag = new ArticleTag();
//                articleTag.setTagId(tag.getId());
//                articleTag.setArticleId(articleId);
//                articleTagMapper.insert(articleTag);
//            }
//        }
//        //body
//        if (isEdit){
//            ArticleBody articleBody = new ArticleBody();
//            articleBody.setArticleId(article.getId());
//            articleBody.setContent(articleParam.getBody().getContent());
//            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
//            LambdaUpdateWrapper<ArticleBody> updateWrapper = Wrappers.lambdaUpdate();
//            updateWrapper.eq(ArticleBody::getArticleId,article.getId());
//            articleBodyMapper.update(articleBody, updateWrapper);
//        }else {
//            ArticleBody articleBody = new ArticleBody();
//            articleBody.setArticleId(article.getId());
//            articleBody.setContent(articleParam.getBody().getContent());
//            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
//            articleBodyMapper.insert(articleBody);
//
//            article.setBodyId(articleBody.getId());
//            articleMapper.updateById(article);
//        }
//        Map<String,String> map = new HashMap<>();
//        map.put("id",article.getId().toString());
//
////        if (isEdit){
////            //发送一条消息给rocketmq 当前文章更新了，更新一下缓存吧
////            ArticleMessage articleMessage = new ArticleMessage();
////            articleMessage.setArticleId(article.getId());
//////            rocketMQTemplate.convertAndSend("blog-update-article",articleMessage);
////        }
//        return Result.success(map);
//    }


    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();

        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        //article.setCategoryId(articleParam.getCategory().getId());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        this.articleMapper.insert(article);

        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                this.articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String,String> map=new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }


    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    public ArticleVo copy(Article article, boolean isAuthor, boolean isTags,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article, articleVo);
        if (isAuthor) {
            SysUser sysUser = sysUserService.findUserById(article.getAuthorId());
            articleVo.setAuthor(sysUser.getNickname());
        }
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (isTags){
            List<TagVo> tags = tagService.findTagsByArticleId(article.getId());
            articleVo.setTags(tags);
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }



    private List<ArticleVo> copyList(List<Article> records,boolean isAuthor,boolean isTags) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article article : records) {
            ArticleVo articleVo = copy(article,isAuthor,isTags,false,false);
            articleVoList.add(articleVo);
        }
        return articleVoList;
    }


    private List<ArticleVo> copyList(List<Article> records,boolean isAuthor,boolean isTags,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article article : records) {
            ArticleVo articleVo = copy(article,isAuthor,isTags,isBody,isCategory);
            articleVoList.add(articleVo);
        }
        return articleVoList;
    }





}
