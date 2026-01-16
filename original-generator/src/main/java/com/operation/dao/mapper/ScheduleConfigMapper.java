package com.operation.dao.mapper;

import com.operation.model.po.ScheduleConfig;
import com.operation.model.po.ScheduleConfigExample;
import com.softwareloop.mybatis.generator.plugins.MyMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ScheduleConfigMapper extends MyMapper<ScheduleConfig> {
    long countByExample(ScheduleConfigExample example);

    int deleteByExample(ScheduleConfigExample example);

    List<ScheduleConfig> selectByExample(ScheduleConfigExample example);

    int updateByExampleSelective(@Param("record") ScheduleConfig record, @Param("example") ScheduleConfigExample example);

    int updateByExample(@Param("record") ScheduleConfig record, @Param("example") ScheduleConfigExample example);
}