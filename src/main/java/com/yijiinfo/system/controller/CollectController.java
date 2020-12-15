package com.yijiinfo.system.controller;

import com.yijiinfo.common.annotation.OperationLog;
import com.yijiinfo.common.util.ResultBean;
import com.yijiinfo.system.model.Dept;
import com.yijiinfo.system.model.Menu;
import com.yijiinfo.system.model.UserInfo;
import com.yijiinfo.system.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class CollectController {

    @Resource
    private MenuService menuService;

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private SysLogService sysLogService;

    @Resource
    private UserOnlineService userOnlineService;

    @Resource
    private CustCardInfoService custCardInfoService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private PersonService personService;

    @Resource
    private UserInfoService userInfoService;

    @OperationLog("人员信息采集")
    @GetMapping("/collect")
    public String collect() {
        return "collect";
    }
    @OperationLog("新增人员")
    @PostMapping
    @ResponseBody
    public ResultBean add(UserInfo userInfo) {
        return ResultBean.success(userInfoService.insert(userInfo));
    }
}
