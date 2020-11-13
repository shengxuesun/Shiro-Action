package im.zhaojun.system.service;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import im.zhaojun.system.mapper.db2.CustCardInfoMapper;
import im.zhaojun.system.model.CustCardInfo;
import im.zhaojun.system.model.Person;
import me.zhyd.oauth.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static im.zhaojun.MainActionApplication.ARTEMIS_PATH;

@Service
public class PersonService {

    @Resource
    private CustCardInfoMapper custCardInfoMapper;

    @Transactional
    public void syncPerson() {

        System.out.println("测试获取的config数值："+ArtemisConfig.host);
        final String getCamsApi = ARTEMIS_PATH+"/api/resource/v1/person/batch/add";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };
        List<CustCardInfo> custCardInfoList = custCardInfoMapper.personList();
        List<Person> personList = new ArrayList<>();
        AtomicReference<Integer> count = new AtomicReference<>(0);
        Integer totalCount = custCardInfoList.size();
        custCardInfoList.forEach(custCardInfo ->{
            Person person = new Person();
            person.setClientId(Integer.parseInt(custCardInfo.getCustId()));
            person.setPersonId(custCardInfo.getCustId());
            person.setPersonName(custCardInfo.getCustName());
            if(!StringUtils.isEmpty(custCardInfo.getDeptName()) && !StringUtils.isEmpty(custCardInfo.getSpecialtyName())){
                if(!"1".equals(custCardInfo.getCustType())){//教职工原custtype是1
                    person.setOrgIndexCode(custCardInfo.getSpecialtyCode());
                }else{
                    person.setOrgIndexCode("26");//默认是教职工所属部门“上海民航职业技术学院”
                }
            }else {
                if (StringUtils.isEmpty(custCardInfo.getDeptName()) && StringUtils.isEmpty(custCardInfo.getSpecialtyName())) {
                    person.setOrgIndexCode("27");//没有任何部门, 取名“其他”
                }else if(StringUtils.isEmpty(custCardInfo.getDeptName())){
                    person.setOrgIndexCode(custCardInfo.getSpecialtyCode());
                }else{
                    person.setOrgIndexCode(custCardInfo.getDeptCode());
                }
            }
            person.setEmail(StringUtils.isEmpty(custCardInfo.getEmail())?"123@qq.com":custCardInfo.getEmail());
            person.setGender(Integer.parseInt(StringUtils.isEmpty(custCardInfo.getSex())?"0":custCardInfo.getSex()));
            person.setJobNo(custCardInfo.getStuEmpNo());
            String idType = "111";
            if("2".equals(custCardInfo.getIdType())){
                idType = "414";
            }
            person.setCertificateType(idType);
            person.setCertificateNo(StringUtils.isEmpty(custCardInfo.getIdNo())?"123":custCardInfo.getIdNo());
            if(person.getCertificateNo().length() > 15){
                String year = person.getCertificateNo().substring(6, 10);//调用substring方法返回相关字段，注意索引从0开始
                String month =person.getCertificateNo().substring(10, 12);
                String day = person.getCertificateNo().substring(12, 14);
                String birthday = year + "-" + month + "-" + day;
                person.setBirthday(birthday);
            }else{
                person.setBirthday("1970-01-01");
            }
            person.setPhoneNo(StringUtils.isEmpty(custCardInfo.getMobile())?"110":custCardInfo.getMobile());
            count.getAndSet(count.get() + 1);

            personList.add(person);
            if(count.get() % 1000 == 0){
                String body = JSONObject.toJSON(personList).toString();
                String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
                System.out.println("api result："+result);
                personList.clear();
            }
            if(count.get().equals(totalCount)){
                String body = JSONObject.toJSON(personList).toString();
                System.out.println("JSON："+body);
                String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
                System.out.println("api result："+result);
                System.out.println("Person："+person.toString());
                personList.clear();
            }


        });



    }

    public void listPerson() {

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
        System.out.println(result) ;
    }
}
