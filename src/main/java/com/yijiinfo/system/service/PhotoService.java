package com.yijiinfo.system.service;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.yijiinfo.system.mapper.db2.CustCardInfoMapper;
import com.yijiinfo.system.model.CustCardInfo;
import com.yijiinfo.system.model.Person;
import com.yijiinfo.system.model.Photo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.yijiinfo.MainActionApplication.ARTEMIS_PATH;

@Service
public class PhotoService {

    @Resource
    private CustCardInfoMapper custCardInfoMapper;


    @Transactional
    public JSONObject syncSinglePhoto(CustCardInfo custCardInfo) {
        String getCamsApi = ARTEMIS_PATH+"/api/resource/v1/face/single/add";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };
        Photo photo = new Photo();
        photo.setPersonId(custCardInfo.getCustId());
        photo.setFaceData(Base64.getEncoder().encodeToString(custCardInfo.getPhoto()));
        String body = JSONObject.toJSON(photo).toString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
        return JSONObject.parseObject(result);
    }
    @Transactional
    public void syncPhoto() {

        String getCamsApi = ARTEMIS_PATH+"/api/resource/v1/face/single/add";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };
        List<CustCardInfo> custCardInfoList = custCardInfoMapper.photoList();
        AtomicReference<Integer> count = new AtomicReference<>(0);
        custCardInfoList.stream().forEach(custCardInfo ->{
            count.getAndSet(count.get() + 1);

            Photo photo = new Photo();
            photo.setPersonId(custCardInfo.getCustId());
//            photo.setFaceData("data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(custCardInfo.getPhoto()));
            photo.setFaceData(Base64.getEncoder().encodeToString(custCardInfo.getPhoto()));
            String body = JSONObject.toJSON(photo).toString();
            String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
            System.out.println("API Result:"+result);
        });
    }

    public void listPhoto() {

        final String getCamsApi = ARTEMIS_PATH + "/api/resource/v2/person/personList";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 10);
        String body = jsonBody.toJSONString();

        String result =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
        System.out.println(result);
    }
}
