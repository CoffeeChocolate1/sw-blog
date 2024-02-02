package com.sw.blog.service;

import com.sw.blog.vo.Result;
import com.sw.blog.vo.params.CommentParam;

public interface CommentsService {
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);


}
