package com.wj.crowd.mysql.service.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.entity.Do.Comments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.crowd.entity.Vo.comment.CommentFormVo;
import com.wj.crowd.entity.Vo.comment.CommentFrontVo;
import com.wj.crowd.entity.Vo.comment.CommentQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author w
 * @since 2022-06-01
 */
public interface CommentsService extends IService<Comments> {
    // 根据查询对象获取评论列表
    Map<String, Object> getCommentList(String sourceId,String sourceType);

    // 添加评论
    boolean addComment(CommentFormVo commentFormVo);

    // 根据评论编号删除评论信息
    boolean removeCommentById(String commentId);

    // 根据评论编号删除评论信息
    boolean removeComment(String commentId,String uid);

    // 根据查询对象获取评论分页列表
    void pageCommentCondition(Page<Comments> pageParam, CommentQuery commentQuery);

    // 根据评论编号查询评论详细信息
    List<CommentFrontVo> getDetailById(String commentId);

}
