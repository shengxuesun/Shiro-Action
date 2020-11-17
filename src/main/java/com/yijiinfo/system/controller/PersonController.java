package com.yijiinfo.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.yijiinfo.common.annotation.OperationLog;
import com.yijiinfo.common.util.ResultBean;
import com.yijiinfo.system.model.CustCardInfo;
import com.yijiinfo.system.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/custCardInfo")
public class PersonController {
    @Resource
    private PersonService personService;

    @GetMapping("/person")
    public String index() {
        return "custCardInfo/person-operation";
    }


    @OperationLog("同步人员信息")
    @PostMapping("/syncPerson")
    @ResponseBody
    public ResultBean syncPerson() {
        personService.syncPerson();
        return ResultBean.success();
    }

    @OperationLog("同步人员信息")
    @PostMapping("/syncSinglePerson")
    @ResponseBody
    public ResultBean syncSinglePerson(CustCardInfo custCardInfo) {
        JSONObject jsonObject = personService.syncSinglePerson(custCardInfo);
        return ResultBean.successData(jsonObject);
    }
    @OperationLog("同步人员信息")
    @PostMapping("/updateSinglePerson")
    @ResponseBody
    public ResultBean updateSinglePerson(CustCardInfo custCardInfo) {
        JSONObject jsonObject = personService.updateSinglePerson(custCardInfo);
        return ResultBean.successData(jsonObject);
    }
    @OperationLog("列表人员信息")
    @PostMapping("/listPerson")
    @ResponseBody
    public ResultBean listPerson() {
        personService.listPerson();
        return ResultBean.success();
    }
}
