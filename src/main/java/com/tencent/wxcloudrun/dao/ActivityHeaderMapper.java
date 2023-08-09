package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.ActivityHeader;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 活动主表
 * @author lf
 * @date 2023-08-09
 */
@Mapper
@Repository
public interface ActivityHeaderMapper {

    /**
     * 新增
     * @author lf
     * @date 2023/08/09
     **/
    int insert(ActivityHeader activityHeader);

    /**
     * 刪除
     * @author lf
     * @date 2023/08/09
     **/
    int delete(int id);

    /**
     * 更新
     * @author lf
     * @date 2023/08/09
     **/
    int update(ActivityHeader activityHeader);

    /**
     * 查询 根据主键 id 查询
     * @author lf
     * @date 2023/08/09
     **/
    ActivityHeader load(int id);

    /**
     * 查询 分页查询
     * @author lf
     * @date 2023/08/09
     **/
    List<ActivityHeader> pageList(ActivityHeader activityHeader);
}
