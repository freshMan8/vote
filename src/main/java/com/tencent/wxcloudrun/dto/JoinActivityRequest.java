package com.tencent.wxcloudrun.dto;

import com.tencent.wxcloudrun.model.ActivityContextDetail;
import com.tencent.wxcloudrun.model.ActivityDetail;
import lombok.Data;

import java.util.List;

/**
 * TO-DO
 *
 * @author 3832
 * @date 2023/8/17 9:29
 */
@Data
public class JoinActivityRequest extends ActivityDetail {

    private List<ActivityContextDetail> forms;
}
