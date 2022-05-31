package com.wj.crowd.mysql.mapper;

import com.wj.crowd.entity.Do.Dynamic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author w
 * @since 2022-05-24
 */
public interface DynamicMapper extends BaseMapper<Dynamic> {

    List<Dynamic> getDynamicByUserId(String userId);
}
