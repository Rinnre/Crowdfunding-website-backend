package com.wj.crowd.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.entity.Do.Dynamic;
import com.wj.crowd.entity.Do.Picture;
import com.wj.crowd.mysql.mapper.DynamicMapper;
import com.wj.crowd.mysql.service.api.PictureService;
import com.wj.crowd.mysql.service.api.DynamicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author w
 * @since 2022-05-24
 */
@Service
@Transactional(readOnly = true)
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements DynamicService {

    @Autowired
    private PictureService pictureService;

    /**
     * 用户删除自己的动态
     *
     * @param uid       用户id
     * @param dynamicId 动态id
     * @return 删除结果
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public boolean removeDynamic(String uid, String dynamicId) {

        // 删除动态
        QueryWrapper<Dynamic> dynamicQueryWrapper = new QueryWrapper<>();
        dynamicQueryWrapper.eq("uid", uid);
        dynamicQueryWrapper.eq("id", dynamicId);
        int delete = baseMapper.delete(dynamicQueryWrapper);

        // 删除动态图片
        QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper.eq("foreign_id", dynamicId);
        boolean remove = pictureService.remove(pictureQueryWrapper);
        return  true;
    }

    /**
     * @param dynamic     动态实体
     * @param pictureList 图片实体
     * @return 发布动态结果
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public boolean saveDynamic(Dynamic dynamic, List<Picture> pictureList) {

        // 保存动态
        int insert = baseMapper.insert(dynamic);
        if (insert <= 0) {
            return false;
        }

        // 封装图片所需要的数据、图片来源和外键
        String id = dynamic.getId();

        pictureList.forEach(picture -> {
            picture.setForeignId(id);
            picture.setType(CrowdConstant.PICTURE_TYPE_DYNAMIC);
        });

        if (pictureList.size() == 0 || pictureList == null) {
            return true;
        }

        return pictureService.saveBatch(pictureList);
    }

    /**
     * 查询用户发布的动态
     *
     * @param userId 用户id
     * @return 动态集合
     */
    @Override
    public List<Dynamic> getDynamicByUserId(String userId) {
        return baseMapper.getDynamicByUserId(userId);
    }
}
