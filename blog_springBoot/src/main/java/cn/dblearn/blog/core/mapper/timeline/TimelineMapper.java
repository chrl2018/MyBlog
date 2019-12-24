package cn.dblearn.blog.core.mapper.timeline;

import cn.dblearn.blog.core.entity.timeline.Timeline;
import cn.dblearn.blog.core.entity.timeline.TimelinePost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TimeLineMapper
 *
 * @author bobbi
 * @date 2019/02/24 20:53
 * @email 571002217@qq.com
 * @description
 */
@Mapper
public interface TimelineMapper {

    List<TimelinePost> listTimelinePost(@Param("year") Integer year, @Param("month") Integer month);

    List<Timeline> listTimeline();
}
