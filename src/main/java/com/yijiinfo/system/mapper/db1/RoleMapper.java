package com.yijiinfo.system.mapper.db1;

import com.yijiinfo.system.model.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(Role role);

    Role selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKey(Role role);

    List<Role> selectAll();

    List<Role> selectAllByQuery(Role roleQuery);

    int count();
}