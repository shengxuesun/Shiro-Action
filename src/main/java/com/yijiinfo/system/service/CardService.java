package com.yijiinfo.system.service;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.yijiinfo.system.mapper.db2.CustCardInfoMapper;
import com.yijiinfo.system.model.Card;
import com.yijiinfo.system.model.CustCardInfo;
import com.yijiinfo.system.model.SubCard;
import me.zhyd.oauth.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.yijiinfo.MainActionApplication.ARTEMIS_PATH;

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
