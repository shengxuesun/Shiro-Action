package com.yijiinfo.system.mapper.db1;

import com.yijiinfo.system.model.SysLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLog sysLog);

    SysLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(SysLog sysLog);

    List<SysLog> selectAll();

    int count();
}