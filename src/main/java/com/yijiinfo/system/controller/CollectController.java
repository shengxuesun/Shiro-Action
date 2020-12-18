package com.yijiinfo.system.controller;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yijiinfo.common.annotation.OperationLog;
import com.yijiinfo.common.shiro.realm.UserNameRealm;
import com.yijiinfo.common.util.PageResultBean;
import com.yijiinfo.common.util.ResultBean;
import com.yijiinfo.system.model.UserInfo;
import com.yijiinfo.system.service.*;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CollectController {

    private static final Logger log = LoggerFactory.getLogger(UserNameRealm.class);
    @Resource
    private UserInfoService userInfoService;

    @OperationLog("人员信息采集")
    @GetMapping("/collect")
    public String collect() {
        return "collect";
    }

    @OperationLog("采集人员")
    @PostMapping("/collect/add")
    @ResponseBody
    public ResultBean add(UserInfo userInfo) {
        userInfo.setCreateTime(new Date(System.currentTimeMillis()));
        /**
         * TODO: 查询是否已经存在于海康系统。已经存在的话更新必要信息，包括人脸图片
         * 如果不存在，则调用添加人员的接口，并且更新人脸图片。
         */
        return ResultBean.success(userInfoService.insert(userInfo));
    }

    @OperationLog("上传图片")
    @PostMapping("/collect/upload")
    @ResponseBody
    public JSONObject upload(@RequestParam("file") MultipartFile file) throws IOException {
        String newFileName = "D:/temp/thumbnail"+file.getOriginalFilename();
        File tempFile = new File(newFileName);
        Thumbnails.of(file.getInputStream())
                .scale(0.25f)
                .outputQuality(0.7f)
                .toFile(tempFile);

        String base64Str = null;
        try{
            byte[] imgData=null;
            FileInputStream inputstream = new FileInputStream(tempFile);
            imgData = new byte[inputstream.available()];
            inputstream.read(imgData);
            inputstream.close();
            BASE64Encoder encoder = new BASE64Encoder();
            base64Str = encoder.encode(imgData);
        }catch(Exception e){
            log.error("读取流文件失败");
        }
        JSONObject jsonObject = userInfoService.checkPhoto(base64Str.replaceAll("\r|\n", ""));
        jsonObject.put("photoSize",tempFile.length());
        return jsonObject;
    }
//    public JSONObject upload(@RequestParam("file") MultipartFile file) throws IOException {
//
//        BufferedImage thumbnail = Thumbnails.of(file.getInputStream())
//                .scale(0.25f)
//                .outputQuality(0.7f)
//                .asBufferedImage();
//
//        String base64Str = null;
//        try{
//            byte[] imgData=null;
//            InputStream inputstream=file.getInputStream();
//            imgData = new byte[inputstream.available()];
//            inputstream.read(imgData);
//            inputstream.close();
//            BASE64Encoder encoder = new BASE64Encoder();
//            base64Str = encoder.encode(imgData);
//        }catch(Exception e){
//            log.error("读取流文件失败");
//        }
//
//        JSONObject jsonObject = userInfoService.checkPhoto(base64Str);
//        return jsonObject;
//    }
    @GetMapping("/userInfo/index")
    public String index() {
        return "userInfo/user-list";
    }

    @OperationLog("获取用户列表")
    @GetMapping("/userInfo/list")
    @ResponseBody
    public PageResultBean<UserInfo> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "10") int limit,
                                        UserInfo userInfoQuery) {
        List<UserInfo> userInfos = userInfoService.selectAllByQuery(page, limit, userInfoQuery);
        PageInfo<UserInfo> userInfoPageInfo = new PageInfo<>(userInfos);
        return new PageResultBean<>(userInfoPageInfo.getTotal(), userInfoPageInfo.getList());
    }
}
