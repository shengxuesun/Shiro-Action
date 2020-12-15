package com.yijiinfo.system.mapper.db1;

import com.yijiinfo.system.model.User;
import com.yijiinfo.system.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo userInfo);

    UserInfo selectByPrimaryKey(Integer deptId);

    int updateHikByPrimaryKey(UserInfo userInfo);
    int updateAuditByPrimaryKey(UserInfo userInfo);
    int updateIsNewByPrimaryKey(UserInfo userInfo);

    /**
     * 获取所有用户
     */
    List<UserInfo> selectAll();

    /**
     * 获取所有用户
     */
    List<UserInfo> selectAllByQuery(UserInfo userInfoQuery);
    /**
     * 统计系统中有多少个用户.
     */
    int count();

}