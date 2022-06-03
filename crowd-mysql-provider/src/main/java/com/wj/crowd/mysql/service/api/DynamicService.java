package com.wj.crowd.mysql.service.api;

import com.wj.crowd.entity.Do.Dynamic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.crowd.entity.Do.Picture;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author w
 * @since 2022-05-24
 */
public interface DynamicService extends IService<Dynamic> {

    boolean removeDynamic(String uid, String dynamicId);

    boolean saveDynamic(Dynamic dynamic, List<Picture> picture);

    List<Dynamic> getDynamicByUserId(String userId);

    List<Dynamic> getAllDynamic();

}
