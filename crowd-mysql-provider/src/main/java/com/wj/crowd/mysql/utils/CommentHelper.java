package com.wj.crowd.mysql.utils;

import com.wj.crowd.entity.Vo.comment.CommentFrontVo;

import java.util.ArrayList;
import java.util.List;

public class CommentHelper {
    
    public static List<CommentFrontVo> build(List<CommentFrontVo> treeNodes) {
        ArrayList<CommentFrontVo> trees = new ArrayList<>();
        for (CommentFrontVo treeNode : treeNodes) {
            if ("0".equals(treeNode.getParentId())) {
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }
    
    private static CommentFrontVo findChildren(CommentFrontVo treeNode, List<CommentFrontVo> treeNodes) {
        treeNode.setChildren(new ArrayList<>());
        for (CommentFrontVo node : treeNodes) {
            if (treeNode.getId().equals(node.getParentId())) {
                node.setLevel(2);
                treeNode.getChildren().add(node);
            }
        }
        return treeNode;
    }
}
