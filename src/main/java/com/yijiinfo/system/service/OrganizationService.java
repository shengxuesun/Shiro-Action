package com.yijiinfo.system.service;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.yijiinfo.system.mapper.db2.CustCardInfoMapper;
import com.yijiinfo.system.model.CustCardInfo;
import com.yijiinfo.system.model.Organization;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yijiinfo.MainActionApplication.ARTEMIS_PATH;

@Service
public class OrganizationService {

    @Resource
    private CustCardInfoMapper custCardInfoMapper;

    @Transactional
    public void syncOrganization() {

        System.out.println("测试获取的config数值："+ArtemisConfig.host);

        List<CustCardInfo> custCardInfoList = custCardInfoMapper.organizationList();
        List<Organization> organizationList = new ArrayList<>();
        custCardInfoList.forEach(custCardInfo ->{
            Organization organization = new Organization(custCardInfo.getDeptCode(),custCardInfo.getDeptName());
            organizationList.add(organization);
        });
        System.out.println("测试config只"+ArtemisConfig.host);
        final String getCamsApi = ARTEMIS_PATH+"/api/resource/v1/org/batch/add";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };

        String body = JSONObject.toJSON(organizationList).toString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
        System.out.println("api result："+result);
    }

    public void listOrganization() {
        System.out.println("测试获取的config数值："+ArtemisConfig.host+" "+ArtemisConfig.appKey+" "+ArtemisConfig.appSecret);
        final String getCamsApi = ARTEMIS_PATH+"/api/resource/v2/org/advance/orgList";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 3);
        String body = jsonBody.toJSONString();

        String result =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
        System.out.println(result) ;
    }
}
