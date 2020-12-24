package com.yijiinfo.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.yijiinfo.common.annotation.OperationLog;
import com.yijiinfo.common.util.ResultBean;
import com.yijiinfo.system.model.CustCardInfo;
import com.yijiinfo.system.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/custCardInfo")
public class CardController {
    @Resource
    private CardService cardService;

    @GetMapping("/card")
    public String index() {
        return "custCardInfo/card-operation";
    }


    @OperationLog("同步单个开卡信息")
    @PostMapping("/syncSingleCard")
    @ResponseBody
    public ResultBean syncSingleCard(CustCardInfo custCardInfo) {
        JSONObject jsonObject = cardService.syncSingleCard(custCardInfo);
        return ResultBean.successData(jsonObject);
    }


    @OperationLog("同步开卡信息")
    @PostMapping("/syncCard")
    @ResponseBody
    public ResultBean syncCard() {
//        cardService.syncCard();
        cardService.syncCardSchedule();
        return ResultBean.success();
    }

}
