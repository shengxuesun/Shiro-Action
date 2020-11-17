package im.zhaojun.system.service;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import im.zhaojun.system.mapper.db2.CustCardInfoMapper;
import im.zhaojun.system.model.CustCardInfo;
import im.zhaojun.system.model.Photo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static im.zhaojun.MainActionApplication.ARTEMIS_PATH;

@Service
public class PhotoService {

    @Resource
    private CustCardInfoMapper custCardInfoMapper;

    @Transactional
    public void syncPhoto() {

        System.out.println("测试获取的config数值："+ArtemisConfig.host);
        final String getCamsApi = ARTEMIS_PATH+"/api/resource/v1/face/single/add";
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
