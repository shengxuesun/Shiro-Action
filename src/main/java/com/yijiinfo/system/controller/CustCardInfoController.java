package com.yijiinfo.system.controller;

import com.github.pagehelper.PageInfo;
import com.yijiinfo.common.annotation.OperationLog;
import com.yijiinfo.common.util.PageResultBean;
import com.yijiinfo.system.model.CustCardInfo;
import com.yijiinfo.system.service.CustCardInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/custCardInfo")
public class CustCardInfoController {

    @Resource
    private CustCardInfoService custCardInfoService;

    @GetMapping("/index")
    public String index() {
        return "custCardInfo/custcardinfo-list";
    }

    @OperationLog("获取一卡通信息列表")
    @GetMapping("/list")
    @ResponseBody
    public PageResultBean<CustCardInfo> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "10") int limit,
                                        CustCardInfo custCardInfoQuery) {
        List<CustCardInfo> custCardInfoList = custCardInfoService.selectAllInfo(page, limit, custCardInfoQuery);
        PageInfo<CustCardInfo> custCardInfoPageInfo = new PageInfo<>(custCardInfoList);
        return new PageResultBean<>(custCardInfoPageInfo.getTotal(), custCardInfoPageInfo.getList());
    }
//    public ResultBean getList(@RequestParam(value = "page", defaultValue = "1") int page,
//                                        @RequestParam(value = "limit", defaultValue = "10") int limit,
//                                        CustCardInfo custCardInfoQuery) {
//        List<CustCardInfo> custCardInfoList = custCardInfoService.selectAllInfo(page, limit, custCardInfoQuery);
//        return ResultBean.success(custCardInfoList);
//    }



}
