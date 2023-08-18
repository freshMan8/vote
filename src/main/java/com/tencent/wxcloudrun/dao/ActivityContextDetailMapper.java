package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.ActivityContextDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 活动简介
 * @author lf
 * @date 2023-08-10
 */
@Mapper
@Repository
public interface ActivityContextDetailMapper {

    /**
     * 新增
     * @author lf
     * @date 2023/08/10
     **/
    int insert(ActivityContextDetail detail);

    /**
     * 刪除
     * @author lf
     * @date 2023/08/10
     **/
    int delete(int id);

    int deleteByActivityId(Long id);

    /**
     * 更新
     * @author lf
     * @date 2023/08/10
     **/
    int update(ActivityContextDetail activityContexdetail);

    /**
     * 查询 根据主键 id 查询
     * @author lf
     * @date 2023/08/10
     **/
    ActivityContextDetail load(int id);

    /**
     * 查询 分页查询
     * @author lf
     * @date 2023/08/10
     **/
    List<ActivityContextDetail> pageList(ActivityContextDetail detail);

    int deleteByActivityDetailId(Long id);
}
