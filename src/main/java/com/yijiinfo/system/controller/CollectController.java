package com.yijiinfo.system.controller;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yijiinfo.common.annotation.OperationLog;
import com.yijiinfo.common.exception.CaptchaIncorrectException;
import com.yijiinfo.common.shiro.realm.UserNameRealm;
import com.yijiinfo.common.util.PageResultBean;
import com.yijiinfo.common.util.ResultBean;
import com.yijiinfo.system.model.CustCardInfoNew;
import com.yijiinfo.system.model.Sms;
import com.yijiinfo.system.model.User;
import com.yijiinfo.system.model.UserInfo;
import com.yijiinfo.system.service.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CollectController {


    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期 注意这里的转化要和传进来的字符串的格式一直 如2015-9-9 就应该为yyyy-MM-dd
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
    }
    private static final Logger log = LoggerFactory.getLogger(UserNameRealm.class);
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private PhotoService photoService;

    @Resource
    private SmsService smsService;

    @OperationLog("人员信息采集")
    @GetMapping("/collect")
    public String collect() {
        return "collect";
    }

    @OperationLog("人员信息采集v2")
    @GetMapping("/collect_v2")
    public String collectV2() {

        return "collect_v2";
    }

    @OperationLog("采集成功")
    @GetMapping("/collectSuccess")
    public String collectSuccess() {
        return "collectSuccess";
    }

    @OperationLog("采集成功")
    @GetMapping("/collectSuccessV2")
    public String collectSuccessV2() {
        return "collectSuccessV2";
    }

    @OperationLog("采集人员")
    @PostMapping("/collect/add")
    @ResponseBody
    public ResultBean add(UserInfo userInfo) {

        List<UserInfo> userInfoList = userInfoService.selectByQuery(userInfo);
        if(userInfoList.size()>0){
            return ResultBean.error("您的信息已经提交过");
        }
        JSONObject checkUserRes = userInfoService.checkUser(userInfo.getUsername(),userInfo.getIdNo());
        if (Integer.parseInt(checkUserRes.get("number").toString()) == 0){
            return ResultBean.error("系统不存在相应的用户");
        }
        userInfo.setPersonId(checkUserRes.get("personId").toString());
        userInfo.setCreateTime(new Date(System.currentTimeMillis()));
        /**
         * TODO: 查询是否已经存在于海康系统。已经存在的话更新必要信息，包括人脸图片
         * 如果不存在，则调用添加人员的接口，并且更新人脸图片。
         */
        return ResultBean.success(userInfoService.insert(userInfo));
    }

    @OperationLog("采集人员")
    @PostMapping("/collect/add3")
    @ResponseBody
    public ResultBean add3(UserInfo userInfo, @RequestParam(value = "captcha", required = true) String captcha,@RequestParam(value = "mobileCode",required = true) String mobileCode) {


        Sms sms2 = smsService.selectLastOneByMobile("13661509696");
        System.out.println(sms2.toString());
        return ResultBean.success("sss");
    }
    @OperationLog("采集人员")
    @PostMapping("/collect/add2")
    @ResponseBody
    public ResultBean add2(UserInfo userInfo, @RequestParam(value = "captcha", required = true) String captcha,@RequestParam(value = "mobileCode",required = true) String mobileCode) {

        //校验图形验证码
        String realCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");
        // session 中的验证码过期了
        if (realCaptcha == null || !realCaptcha.equals(captcha.toLowerCase())) {
            throw new CaptchaIncorrectException();
        }
        //校验手机验证码
        Sms sms = smsService.selectLastOneByMobile(userInfo.getMobile());
        if(sms == null || !mobileCode.equals(sms.getCode())){
            return ResultBean.error("手机验证码校验失败，请重试");
        }else{
            smsService.updateVerifiedByMobileCode(sms);
        }

        List<UserInfo> userInfoList = userInfoService.selectByQuery(userInfo);
        if(userInfoList.size()>0){
            return ResultBean.error("您的信息已经提交过");
        }
        JSONObject checkUserRes = userInfoService.checkUser(userInfo.getUsername(),userInfo.getIdNo());
        if (Integer.parseInt(checkUserRes.get("number").toString()) == 0){
            return ResultBean.error("系统不存在相应的用户");
        }
        userInfo.setPersonId(checkUserRes.get("personId").toString());
        userInfo.setCreateTime(new Date(System.currentTimeMillis()));
        /**
         * TODO: 查询是否已经存在于海康系统。已经存在的话更新必要信息，包括人脸图片
         * 如果不存在，则调用添加人员的接口，并且更新人脸图片。
         */
        return ResultBean.success(userInfoService.insert(userInfo));
    }

    @PostMapping("/collect/checkVerify")
    @ResponseBody
    public ResultBean checkVerify( @RequestParam(value = "code", required = false) String captcha, @RequestParam(value = "mobile", required = false) String mobile) {
        String realCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");
        // session 中的验证码过期了
        if (realCaptcha == null || !realCaptcha.equals(captcha.toLowerCase())) {
            throw new CaptchaIncorrectException();
        }
        //TODO: 如果24小时内一个手机号发送超过5条，则不能再发送。
        if(smsService.count24Hours(mobile) > 4){
            return ResultBean.error("24小时内最多发送5条信息！");
        }
        String code = String.valueOf((int) (Math.random() * 9000) + 1000);
        int res = smsService.sendCode(mobile,code);
        if(res>0){
            Sms sms = new Sms();
            sms.setMobile(mobile);
            sms.setCode(code);
            sms.setVerified(0);
            smsService.insert(sms);
        }else{
            System.out.println("发送问题代码："+res);
            return ResultBean.error("发送失败");
        }
        return ResultBean.success("已经发送");
    }

    public int checkSendTimes(){
        return smsService.count24Hours("13661509696");
    }

    @OperationLog("上传图片")
    @PostMapping("/collect/upload")
    @ResponseBody
    public JSONObject upload(@RequestParam("file") MultipartFile file) throws IOException {
        String newFileName = "D:/temp/thumbnail"+file.getOriginalFilename();
        File tempFile = new File(newFileName);
        Thumbnails.of(file.getInputStream())
                .scale(0.3f)
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

    @OperationLog("获取用户列表")
    @PostMapping("/userInfo/syncSinglePhoto")
    @ResponseBody
    public ResultBean syncSinglePhoto(UserInfo userInfo) {
        CustCardInfoNew custCardInfo = new CustCardInfoNew();
        custCardInfo.setCustId(userInfo.getPersonId());
        custCardInfo.setPhoto(userInfo.getPhoto());
        JSONObject jsonObject = photoService.syncSinglePhoto(custCardInfo);
        userInfo.setAuditTime(new Date(System.currentTimeMillis()));
        userInfo.setAuditStatus(1);
        userInfoService.updateAuditByPersonId(userInfo);
        return ResultBean.successData(jsonObject);
    }
}
