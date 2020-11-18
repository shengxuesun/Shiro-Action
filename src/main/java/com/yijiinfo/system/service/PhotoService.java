package com.yijiinfo.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.yijiinfo.system.mapper.db2.CustCardInfoMapper;
import com.yijiinfo.system.model.CustCardInfo;
import com.yijiinfo.system.model.CustCardInfoNew;
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
    public JSONObject syncSinglePhoto(CustCardInfoNew custCardInfo) {
        //第一步：添加，如果原先没有的话，添加成功，否则返回已经存在。
        String getCamsApi = ARTEMIS_PATH+"/api/resource/v1/face/single/add";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };
        Photo photo = new Photo();
        photo.setPersonId(custCardInfo.getCustId());
        photo.setFaceData(custCardInfo.getPhoto());
        String body = JSONObject.toJSON(photo).toString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数

        if(JSONObject.parseObject(result).getString("msg").equals("PersonFace Exists")) {
            String personInfoApi = ARTEMIS_PATH+"/api/resource/v1/person/condition/personInfo";
            Map<String, String> path0 = new HashMap<String, String>(2) {
                {
                    put("https://", personInfoApi);//根据现场环境部署确认是http还是https
                }
            };
            String body0 = "{\"paramName\":\"personId\",\"paramValue\":[\""+custCardInfo.getCustId()+"\"]}";
            String result0 = ArtemisHttpUtil.doPostStringArtemis(path0,body0,null,null,"application/json",null);// post请求application/json类型参数
            JSONObject personData = JSONObject.parseObject(result0);
            JSONArray jsonArray = JSONObject.parseObject(personData.get("data").toString()).getJSONArray("list");
            String faceId = jsonArray.getJSONObject(0).getJSONArray("personPhoto").getJSONObject(0).getString("personPhotoIndexCode");
            System.out.println("faceId............"+faceId);

            String updatePhotoApi = ARTEMIS_PATH+"/api/resource/v1/face/single/update";
            Map<String, String> path1 = new HashMap<String, String>(2) {
                {
                    put("https://", updatePhotoApi);//根据现场环境部署确认是http还是https
                }
            };
            String body1 = "{\"faceId\":\""+faceId+"\",\"faceData\":\""+custCardInfo.getPhoto()+"\"}";
            System.out.println("request body of update photo:"+body1);
            String result1 = ArtemisHttpUtil.doPostStringArtemis(path1,body1,null,null,"application/json",null);// post请求application/json类型参数
            return JSONObject.parseObject(result1);
        }else{
            return JSONObject.parseObject(result);
        }
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
