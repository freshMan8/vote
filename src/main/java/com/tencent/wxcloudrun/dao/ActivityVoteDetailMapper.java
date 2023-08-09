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
    List<ActivityVoteDetail> pageList(int offset,int pagesize);

    /**
     * 查询 分页查询 count
     * @author lf
     * @date 2023/08/09
     **/
    int pageListCount(int offset,int pagesize);

}
