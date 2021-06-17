package com.yijiinfo.system.service;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.yijiinfo.system.mapper.db2.CustCardInfoMapper;
import com.yijiinfo.system.model.Card;
import com.yijiinfo.system.model.CustCardInfo;
import com.yijiinfo.system.model.SubCard;
import me.zhyd.oauth.utils.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.yijiinfo.MainActionApplication.ARTEMIS_PATH;

@Configuration
@EnableScheduling
@Service
public class CardService {

    @Resource
    private CustCardInfoMapper custCardInfoMapper;

    @Transactional
    public JSONObject syncSingleCard(CustCardInfo custCardInfo) {
        String getCamsApi = ARTEMIS_PATH+"/api/cis/v1/card/bindings";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };
        String body = JSONObject.toJSON(transFromCustCardInfo(custCardInfo)).toString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
        return JSONObject.parseObject(result);
    }

    @Transactional
    public void syncCard() {

        final String getCamsApi = ARTEMIS_PATH+"/api/cis/v1/card/bindings";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };
        List<CustCardInfo> custCardInfoList = custCardInfoMapper.cardList();
        AtomicReference<Integer> count = new AtomicReference<>(0);
        custCardInfoList.stream().forEach(custCardInfo ->{
            count.getAndSet(count.get() + 1);
            String body = JSONObject.toJSON(transFromCustCardInfo(custCardInfo)).toString();
            String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
            System.out.println("API Result:"+result);
            if(result.indexOf("0x04a12030")!=-1){
                System.out.println("the body of the request:"+body);
            }
        });
    }

    /***
     * 1.判断当前要更新的卡号是否跟已经存在的卡号一致，如果一致，则不更新。
     * 2.如果不一致，果断将第一张卡退卡。
     * 3.退卡后，再绑这个卡。
     ***/
    @Scheduled(cron = "0 0 23 * * *")
    @Transactional
    public void syncCardSchedule() {
        syncCardFunction("ALL");
    }

    @Scheduled(cron = "0 0/5 7-18 * * *")
    @Transactional
    public void syncCardScheduleLate5() {
        syncCardFunction("5Min");
    }
    private void syncCardFunction(String frequency){
        //人员绑卡接口
        final String getCardBindingApi = ARTEMIS_PATH+"/api/cis/v1/card/bindings";
        Map<String, String> cardBindingPath = new HashMap<String, String>(2) {
            {
                put("https://", getCardBindingApi);//根据现场环境部署确认是http还是https
            }
        };
        //查询已有卡片接口
        final String getCardListApi = ARTEMIS_PATH+"/api/irds/v1/card/advance/cardList";
        Map<String, String> cardListPath = new HashMap<String, String>(2) {
            {
                put("https://", getCardListApi);//根据现场环境部署确认是http还是https
            }
        };

        //退卡接口
        final String deleteCardApi = ARTEMIS_PATH+"/api/cis/v1/card/deletion";
        Map<String, String> deleteCardPath = new HashMap<String, String>(2) {
            {
                put("https://", deleteCardApi);//根据现场环境部署确认是http还是https
            }
        };

        List<CustCardInfo> custCardInfoList = "ALL".equals(frequency)?custCardInfoMapper.cardList(): custCardInfoMapper.cardLate5List();
        AtomicReference<Integer> count = new AtomicReference<>(0);
        custCardInfoList.stream().forEach(custCardInfo ->{
            count.getAndSet(count.get() + 1);

            //查询已有卡
            Card card = transFromCustCardInfo(custCardInfo);
            List<SubCard> subCardList = card.getCardList();
            SubCard subCard = subCardList.get(0);
            String personId = subCard.getPersonId();
            String toBindCardNo = subCard.getCardNo();
            String existCardFindBody = "{\"personIds\":\""+personId+"\",\"pageNo\":1,\"pageSize\":100}";
            System.out.println("........"+existCardFindBody);
            String findResult = ArtemisHttpUtil.doPostStringArtemis(cardListPath,existCardFindBody,null,null,"application/json",null);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = JSONObject.parseObject(findResult);
            if(jsonObject.getJSONObject("data") != null) {
                int num = Integer.parseInt(jsonObject.getJSONObject("data").get("total").toString());
                List<String> existCardNos = new ArrayList<>();
                if (num > 0) {
                    List<Object> lists = jsonObject.getJSONObject("data").getObject("list", List.class);
                    lists.forEach(object->{
                        existCardNos.add(JSONObject.parseObject(object.toString()).get("cardNo").toString());
                    });
                }
                if(existCardNos.contains(toBindCardNo)){
                    System.out.println("卡号已存在，无需更新");
                    return;
                }else{
                    //退卡
                    existCardNos.forEach(cardNo->{
                        String deleteCardBody = "{\"personId\":\""+personId+"\",\"cardNumber\":\""+cardNo+"\"}";
                        String deleteResult = ArtemisHttpUtil.doPostStringArtemis(deleteCardPath,deleteCardBody,null,null,"application/json",null);
                        System.out.println("personId:"+personId+"cardNo:"+cardNo+"退卡成功。"+deleteResult+"");
                    });

                    //绑定新卡
                    String body = JSONObject.toJSON(card).toString();
                    String result = ArtemisHttpUtil.doPostStringArtemis(cardBindingPath,JSONObject.toJSON(card).toString(),null,null,"application/json",null);
                    System.out.println("API Result:"+result);
                    if(result.indexOf("0x04a12030")!=-1){
                        System.out.println("the body of the request:"+body);
                    }
                }
            }
        });
    }
    private Card transFromCustCardInfo(CustCardInfo custCardInfo){
        Card card = new Card();
        card.setStartDate(custCardInfo.getOpenDate().substring(0, 4) + "-" + custCardInfo.getOpenDate().substring(4, 6) + "-" + custCardInfo.getOpenDate().substring(6, 8));
        if(Integer.parseInt(custCardInfo.getExpireDate()) >= 20371231){
            card.setEndDate("2037-12-31");
        }else {
            card.setEndDate(custCardInfo.getExpireDate().substring(0, 4) + "-" + custCardInfo.getExpireDate().substring(4, 6) + "-" + custCardInfo.getExpireDate().substring(6, 8));
        }
        SubCard subCard = new SubCard();
        String cardNo = Long.toString(Long.parseLong(custCardInfo.getCardPhyId(),16));
        int cardNoLength = cardNo.length();
        if(cardNoLength == 9){
            cardNo = "0"+cardNo;
        }else if(cardNoLength == 8){
            cardNo = "00"+cardNo;
        }
        subCard.setCardNo(cardNo);
        subCard.setCardType(Integer.parseInt(custCardInfo.getCardType()));
        String[] groups = {"2","3","4"};
        List<String> studentGroups = Arrays.asList(groups);
        if(!studentGroups.contains(custCardInfo.getCustType()) ){
            if (!StringUtils.isEmpty(custCardInfo.getDeptName())) {
                subCard.setOrgIndexCode(custCardInfo.getDeptCode());
            }else{
                subCard.setOrgIndexCode("1");
            }
        }else{
            if (!StringUtils.isEmpty(custCardInfo.getSpecialtyName())) {
                String gender = "1";
                if (!StringUtils.isEmpty(custCardInfo.getSex()) && "2".equals(custCardInfo.getSex())) {
                    gender = "2";
                }
                subCard.setOrgIndexCode(custCardInfo.getSpecialtyCode() + "-" + gender);
//                System.out.print(subCard.getOrgIndexCode()+"-=-"+custCardInfo.getSex());
            } else {
                if(!StringUtils.isEmpty(custCardInfo.getDeptName())) {
                    subCard.setOrgIndexCode(custCardInfo.getDeptCode());
                }else{
                    subCard.setOrgIndexCode("1");
                }
            }
        }
        subCard.setPersonId(custCardInfo.getCustId());
        List<SubCard> subCards = new ArrayList<>();
        subCards.add(subCard);

        card.setCardList(subCards);
        return card;
    }
}
