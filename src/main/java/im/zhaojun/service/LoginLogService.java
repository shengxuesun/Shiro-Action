package im.zhaojun.service;

import com.github.pagehelper.PageHelper;
import im.zhaojun.mapper.LoginLogMapper;
import im.zhaojun.model.LoginLog;
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

    public List<LoginLog> list(int page) {
        PageHelper.startPage(page, 20);
        return loginLogMapper.selectAll();
    }

    /**
     * 最近一周登陆次数
     */
    public List<Integer> recentlyWeekLoginCount() {
        return loginLogMapper.recentlyWeekLoginCount();
    }

    public List<LoginLog> selectAll(int page, int limit) {
        PageHelper.startPage(page, limit);
        return loginLogMapper.selectAll();
    }
}