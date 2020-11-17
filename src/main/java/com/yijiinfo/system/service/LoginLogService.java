package com.yijiinfo.system.service;

import com.github.pagehelper.PageHelper;
import com.yijiinfo.common.util.ShiroUtil;
import com.yijiinfo.system.mapper.db1.LoginLogMapper;
import com.yijiinfo.system.model.LoginLog;
import com.yijiinfo.system.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 登陆日志 Service
 */
@Service
public class LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    public void addLog(String username, boolean isAuthenticated, String ip) {
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginTime(new Date());
        loginLog.setUsername(username);
        loginLog.setLoginStatus(isAuthenticated ? "1" : "0");
        loginLog.setIp(ip);
        loginLogMapper.insert(loginLog);
    }

    /**
     * 最近一周登陆次数
     */
    public List<Integer> recentlyWeekLoginCount() {
        User user = ShiroUtil.getCurrentUser();
        return loginLogMapper.recentlyWeekLoginCount(user.getUsername());
    }

    public List<LoginLog> selectAll(int page, int limit) {
        PageHelper.startPage(page, limit);
        return loginLogMapper.selectAll();
    }

    public int count() {
        return loginLogMapper.count();
    }
}