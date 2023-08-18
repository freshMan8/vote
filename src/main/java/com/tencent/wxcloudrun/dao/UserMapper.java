package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息
 *
 * @author 3832
 * @date 2023/8/2 9:58
 */
@Mapper
public interface UserMapper {

    List<User> getEntity(User user);

    List<User> getEntityType(@Param("user") User user, @Param("list")
            List<Integer> userType);

    Integer updateEntity(User user);

    Integer deleteEntity(User user);

    Integer insertEntity(User user);
}
