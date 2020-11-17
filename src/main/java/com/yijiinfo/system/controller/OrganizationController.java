package com.yijiinfo.system.controller;

import com.yijiinfo.common.annotation.OperationLog;
import com.yijiinfo.common.util.ResultBean;
import com.yijiinfo.system.service.OrganizationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/custCardInfo")
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @GetMapping("/organization")
    public String index() {
        return "custCardInfo/organization-operation";
    }


    @OperationLog("同步组织信息")
    @PostMapping("/syncOrganization")
    @ResponseBody
    public ResultBean syncOrganization() {
        organizationService.syncOrganization();
        return ResultBean.success();
    }
    @OperationLog("列表组织信息")
    @PostMapping("/listOrganization")
    @ResponseBody
    public ResultBean listOrganization() {
        organizationService.listOrganization();
        return ResultBean.success();
    }
}
