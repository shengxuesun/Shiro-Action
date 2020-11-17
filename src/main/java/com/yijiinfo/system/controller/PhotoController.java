package com.yijiinfo.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.yijiinfo.common.annotation.OperationLog;
import com.yijiinfo.common.util.ResultBean;
import com.yijiinfo.system.model.CustCardInfo;
import com.yijiinfo.system.service.PhotoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/custCardInfo")
public class PhotoController {
    @Resource
    private PhotoService photoService;

    @GetMapping("/photo")
    public String index() {
        return "custCardInfo/photo-operation";
    }

    @OperationLog("同步人脸信息")
    @PostMapping("/syncSinglePhoto")
    @ResponseBody
    public ResultBean syncSinglePhoto(CustCardInfo custCardInfo) {
        JSONObject jsonObject = photoService.syncSinglePhoto(custCardInfo);
        return ResultBean.successData(jsonObject);
    }

    @OperationLog("同步人脸信息")
    @PostMapping("/syncPhoto")
    @ResponseBody
    public ResultBean syncPhoto() {
        photoService.syncPhoto();
        return ResultBean.success();
    }
    @OperationLog("列表人员信息")
    @PostMapping("/listPhoto")
    @ResponseBody
    public ResultBean listPhoto() {
        photoService.listPhoto();
        return ResultBean.success();
    }
}
