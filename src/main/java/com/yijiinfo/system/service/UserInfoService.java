package com.yijiinfo.system.service;

import com.yijiinfo.system.mapper.db1.DeptMapper;
import com.yijiinfo.system.mapper.db1.UserInfoMapper;
import com.yijiinfo.system.model.Dept;
import com.yijiinfo.system.model.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    public UserInfo insert(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
        return userInfo;
    }

    public int deleteByPrimaryKey(Integer id) {
        return userInfoMapper.deleteByPrimaryKey(id);
    }

    public UserInfo updateAuditByPrimaryKey(UserInfo userInfo) {
        userInfoMapper.updateAuditByPrimaryKey(userInfo);
        return userInfo;
    }

    public UserInfo updateHikByPrimaryKey(UserInfo userInfo) {
        userInfoMapper.updateHikByPrimaryKey(userInfo);
        return userInfo;
    }

    public UserInfo updateIsNewByPrimaryKey(UserInfo userInfo) {
        userInfoMapper.updateIsNewByPrimaryKey(userInfo);
        return userInfo;
    }

    public UserInfo selectByPrimaryKey(Integer id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }
    public int count() {
        return userInfoMapper.count();
    }

}
