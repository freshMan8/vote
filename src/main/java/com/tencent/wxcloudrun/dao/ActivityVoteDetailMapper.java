package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.ActivityVoteDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 活动投票详情
 * @author lf
 * @date 2023-08-09
 */
@Mapper
@Repository
public interface ActivityVoteDetailMapper {

    /**
     * 新增
     * @author lf
     * @date 2023/08/09
     **/
    int insert(ActivityVoteDetail activityVoteDetail);

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
    int update(ActivityVoteDetail activityVoteDetail);

    /**
     * 查询 根据主键 id 查询
     * @author lf
     * @date 2023/08/09
     **/
    ActivityVoteDetail load(int id);

    /**
     * 查询 分页查询
     * @author lf
     * @date 2023/08/09
     **/
    List<ActivityVoteDetail> pageList(ActivityVoteDetail param);

    /**
     * 根据活动ID查询投票详情
     * @author 3832
     * @date 2023/8/14 15:00
     * @param id
     * @return
     **/
    List<ActivityVoteDetail> getListByActiveId(Long id);
}
