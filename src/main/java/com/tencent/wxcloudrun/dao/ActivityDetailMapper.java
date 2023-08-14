package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.ActivityDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 活动参与明细表
 * @author lf
 * @date 2023-08-09
 */
@Mapper
@Repository
public interface ActivityDetailMapper {

    /**
     * 新增
     * @author lf
     * @date 2023/08/09
     **/
    int insert(ActivityDetail activityDetail);

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
    int update(ActivityDetail activityDetail);

    /**
     * 查询 根据主键 id 查询
     * @author lf
     * @date 2023/08/09
     **/
    ActivityDetail load(Long id);

    /**
     * 查询 分页查询
     * @author lf
     * @date 2023/08/09
     **/
    List<ActivityDetail> pageList(ActivityDetail activityDetail);

    /**
     * 获取未结束可以投票的明细
     *
     * @author 3832
     * @date 2023/8/14 9:12
     * @return 明细
     **/
    List<ActivityDetail> getAvaList();
}
