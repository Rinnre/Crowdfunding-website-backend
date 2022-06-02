package com.wj.crowd.mysql.controller;


import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Vo.comment.CommentFormVo;
import com.wj.crowd.mysql.service.api.CommentsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author w
 * @since 2022-06-01
 */
@RestController
@RequestMapping("/mysql/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentService;

    /**
     * 根据查询对象获取评论列表
     *
     * @return 评论列表
     */
    @ApiOperation(value = "根据查询对象获取评论列表")
    @GetMapping("/getCommentList/{sourceId}/{sourceType}")
    public ResultEntity<Map<String, Object>> getCommentList(
            @PathVariable String sourceId,
            @PathVariable String sourceType) {

        Map<String, Object> map = commentService.getCommentList(sourceId,sourceType);
        return ResultEntity.success(map);
    }

    /**
     * 添加评论
     *
     * @param commentFormVo 评论信息对象
     * @return 评论结果
     */
    @ApiOperation(value = "添加评论")
    @PostMapping("/addComment")
    public ResultEntity<String> addComment(
            @RequestBody CommentFormVo commentFormVo
    ) {
        boolean save = commentService.addComment(commentFormVo);
        return save ? ResultEntity.success("评论成功") : ResultEntity.fail("评论失败");
    }

    /**
     * 根据评论编号删除评论信息
     *
     * @param commentId 评论编号
     * @return 删除结果
     */
    @ApiOperation(value = "根据评论编号删除评论信息")
    @DeleteMapping("/removeComment/{commentId}/{uid}")
    public ResultEntity<String> removeComment(
            @PathVariable String commentId,
            @PathVariable String uid
    ) {
        try {
            boolean result = commentService.removeComment(commentId, uid);
            return (result) ? ResultEntity.success("删除评论成功") : ResultEntity.fail("删除评论失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }


}

