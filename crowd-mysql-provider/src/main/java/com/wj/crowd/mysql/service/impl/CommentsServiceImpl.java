package com.wj.crowd.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.entity.Do.Comments;
import com.wj.crowd.entity.Do.Project;
import com.wj.crowd.entity.Vo.comment.CommentFormVo;
import com.wj.crowd.entity.Vo.comment.CommentFrontVo;
import com.wj.crowd.entity.Vo.comment.CommentQuery;
import com.wj.crowd.mysql.mapper.CommentsMapper;
import com.wj.crowd.mysql.service.api.CommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.mysql.service.api.ProjectService;
import com.wj.crowd.mysql.utils.CommentHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author w
 * @since 2022-06-01
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {

    @Autowired
    private ProjectService projectService;

    /**
     * 根据查询对象获取评论列表
     * @return 评论列表
     */
    @Override
    public Map<String, Object> getCommentList(String sourceId,String sourceType) {
        // 1. 初始化返回数据
        Map<String, Object> map = new HashMap<>();
        // 2. 根据查询对象构造条件构造器
        QueryWrapper<Comments> wrapper = new QueryWrapper<>();
        wrapper.eq("source_id", sourceId);
        wrapper.eq("source_type",sourceType);

        // 3. 获取评论数量
        Long count = baseMapper.selectCount(wrapper);
        map.put("count", count);

        // 4. 构造评论列表
        List<CommentFrontVo> commentFrontList = new ArrayList<>();
        for (Comments comment : baseMapper.selectList(wrapper)) {
            CommentFrontVo commentFrontVo = new CommentFrontVo();
            BeanUtils.copyProperties(comment, commentFrontVo);
            commentFrontList.add(commentFrontVo);
        }
        List<CommentFrontVo> commentList = CommentHelper.build(commentFrontList);
        map.put("commentList", commentList);
        // 5. 返回查询结果
        return map;
    }

    /**
     * 添加评论
     * @param commentFormVo 评论信息对象
     * @return 评论结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public boolean addComment(CommentFormVo commentFormVo) {

        // 1. 构建评论对象
        Comments comment = new Comments();
        BeanUtils.copyProperties(commentFormVo,comment);

        // 2. 判断是否为二级评论
        Comments replyComment = baseMapper.selectById(commentFormVo.getReplyId());
        if (replyComment != null) {
            comment.setReplyMemberId(replyComment.getMemberId());
            comment.setReplyNickname(replyComment.getNickname());
        }


        // 4. 添加评论
        int insert = baseMapper.insert(comment);
        // 5.项目评论数增加

        String sourceId = comment.getSourceId();
        Project project = projectService.getById(sourceId);

        UpdateWrapper<Project> projectUpdateWrapper = new UpdateWrapper<>();
        projectUpdateWrapper.eq("id",sourceId);
        projectUpdateWrapper.set("comment_number",project.getCommentNumber()+1);
        boolean updateResult = projectService.update(projectUpdateWrapper);

        return insert > 0&&updateResult;
    }

    /**
     * 根据评论编号删除评论信息
     * @param commentId 评论编号
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCommentById(String commentId) {
        try {
            // 1. 根据评论编号获取评论信息
            Comments comment = baseMapper.selectById(commentId);
            // 2. 创建需要删除的评论编号列表
            List<Object> ids = new ArrayList<>();
            // 3. 判断评论是否为一级评论
            if (comment.getParentId().equals("0")) {
                // 获取此评论下的所有二级评论
                ids = baseMapper.selectObjs(new QueryWrapper<Comments>().eq("parent_id", commentId).select("id"));
            }
            ids.add(commentId);
            // 4. 根据评论编号列表删除评论信息
            baseMapper.deleteBatchIds(ids);
            // 5.项目评论数减少

            String sourceId = comment.getSourceId();
            Project project = projectService.getById(sourceId);
            UpdateWrapper<Project> projectUpdateWrapper = new UpdateWrapper<>();
            projectUpdateWrapper.eq("id",sourceId);
            projectUpdateWrapper.set("comment",project.getCommentNumber()-ids.size());
            boolean updateResult = projectService.update(projectUpdateWrapper);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    /**
     * 根据评论编号删除评论信息
     * @param commentId 评论编号
     * @return 删除结果
     */
    @Override
    public boolean removeComment( String commentId,String uid) {
        // 1. 通过评论编号查询评论信息
        Comments comment = baseMapper.selectById(commentId);

        if(!uid.equals(comment.getMemberId())){
            return false;
        }
        // 2. 根据评论编号删除评论信息
        return removeCommentById(commentId);
    }

    /**
     * 根据查询对象获取评论分页列表
     * @param pageParam    分页对象
     * @param commentQuery 查询对象
     */
    @Override
    public void pageCommentCondition(Page<Comments> pageParam, CommentQuery commentQuery) {
        // 1. 根据查询条件构建条件构造器
        QueryWrapper<Comments> wrapper = new QueryWrapper<>();
        // 2. 时间降序
        wrapper.orderByDesc("create_time");
        // 3. 判断评论发布起始时间与结束时间是否为空，如不为空查询评论发布时间在起始时间与结束时间之间的评论
        if (!StringUtils.isEmpty(commentQuery.getBegin()) && !StringUtils.isEmpty(commentQuery.getEnd())) {
            wrapper.between("update_time", commentQuery.getBegin(),commentQuery.getEnd());
        }
        // 4. 判断来源是否为空，如不为空根据评论来源进行查询
        if (!StringUtils.isEmpty(commentQuery.getSource())) {
            wrapper.eq("source_type", commentQuery.getSource());
        }
//        // 5. 判断评论状态是否为空，如不为空根据评论状态进行查询
//        if (!StringUtils.isEmpty(commentQuery.getState())) {
//            if (commentQuery.getState().equals("normal")) {
//                wrapper.eq("state", CommentConstants.CommentState.NORMAL.getCode());
//            } else if (commentQuery.getState().equals("report")) {
//                wrapper.eq("state", CommentConstants.CommentState.REPORT.getCode());
//            }
//        }
        // 6. 判断评论内容是否为空，如不为空根据评论内容进行模糊查询
        if (!StringUtils.isEmpty(commentQuery.getContent())) {
            wrapper.like("content",commentQuery.getContent());
        }
        baseMapper.selectPage(pageParam, wrapper);
    }

    /**
     * 根据评论编号查询评论详细信息
     * @param commentId 评论编号
     * @return 评论列表
     */
    @Override
    public List<CommentFrontVo> getDetailById(String commentId) {
        // 1. 初始化返回列表
        List<Comments> commentList;
        // 2. 通过评论编号查询评论信息
        Comments selectComment = baseMapper.selectById(commentId);
        // 3. 判断当前评论是否为一级评论
        if (selectComment.getParentId().equals("0")) {
            // 4. 通过评论编号获取当前评论的所有二级评论列表
            commentList = baseMapper.selectList(new QueryWrapper<Comments>().eq("parent_id", commentId));
            // 5. 添加当前评论到评论列表
            commentList.add(selectComment);
        } else {
            // 4. 通过当前评论的父评论编号获取所有二级评论列表
            commentList = baseMapper.selectList(new QueryWrapper<Comments>().eq("parent_id", selectComment.getParentId()));
            // 5. 添加当前评论的父评论到评论列表
            commentList.add(baseMapper.selectById(selectComment.getParentId()));
        }
        // 6. 构建评论列表
        List<CommentFrontVo> commentFrontList = new ArrayList<>();
        for (Comments comment : commentList) {
            CommentFrontVo commentFrontVo = new CommentFrontVo();
            BeanUtils.copyProperties(comment, commentFrontVo);
            commentFrontList.add(commentFrontVo);
        }
        return CommentHelper.build(commentFrontList);
    }




}
