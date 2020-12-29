package com.yijiinfo.system.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.yijiinfo.common.util.HttpClientUtil;
import com.yijiinfo.system.mapper.db1.SmsMapper;
import com.yijiinfo.system.model.Sms;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class SmsService {

    @Resource
    private SmsMapper smsMapper;

    public Sms insert(Sms sms){
        sms.setSendTime(new Date(System.currentTimeMillis()));
        smsMapper.insert(sms);
        return sms;
    }

    public int sendCode(String mobile, String code){
        //用户名
        String Uid = "shengxuesun";
        //接口安全秘钥(登录密码：111111)
        String Key = "c41d8cd98f00b204d980";
        String smsText = "验证码为："+code+"，您正在进行民航学院人脸采集，注意信息安全。";
        HttpClientUtil client = HttpClientUtil.getInstance();
        //UTF发送
        int result = client.sendMsgUtf8(Uid, Key, smsText, mobile);
        return result;
    }
    public Sms selectLastOneByMobile(String mobile){
        return smsMapper.selectLastOneByMobile(mobile);
    }

    public List<Sms> selectSmsBysSmsQuery(Sms sms){
        return smsMapper.selectAllByQuery(sms);
    }
    public int updateVerifiedByMobileCode(Sms sms){
        sms.setVerified(1);
        sms.setVerifyTime(new Date(System.currentTimeMillis()));
        return smsMapper.updateVerifiedByMobileCode(sms);
    }

    public int count24Hours(String mobile){
        return smsMapper.count24Hours(mobile);
    }
}
