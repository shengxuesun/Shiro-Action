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

            Card card = new Card();
            card.setStartDate(custCardInfo.getOpenDate().substring(0, 4) + "-" + custCardInfo.getOpenDate().substring(4, 6) + "-" + custCardInfo.getOpenDate().substring(6, 8));
            if(Integer.parseInt(custCardInfo.getExpireDate()) >= 20371231){
                card.setEndDate("2037-12-31");
            }else {
                card.setEndDate(custCardInfo.getExpireDate().substring(0, 4) + "-" + custCardInfo.getExpireDate().substring(4, 6) + "-" + custCardInfo.getExpireDate().substring(6, 8));
            }
            SubCard subCard = new SubCard();
            subCard.setCardNo(custCardInfo.getCardPhyId());
            subCard.setCardType(Integer.parseInt(custCardInfo.getCardType()));
            if(!StringUtils.isEmpty(custCardInfo.getDeptName()) && !StringUtils.isEmpty(custCardInfo.getSpecialtyName())){
                if(!"1".equals(custCardInfo.getCustType())){//教职工原custtype是1
                    subCard.setOrgIndexCode(custCardInfo.getSpecialtyCode());
                }else{
                    subCard.setOrgIndexCode("26");//默认是教职工所属部门“上海民航职业技术学院”
                }
            }else {
                if (StringUtils.isEmpty(custCardInfo.getDeptName()) && StringUtils.isEmpty(custCardInfo.getSpecialtyName())) {
                    subCard.setOrgIndexCode("27");//没有任何部门, 取名“其他”
                }else if(StringUtils.isEmpty(custCardInfo.getDeptName())){
                    subCard.setOrgIndexCode(custCardInfo.getSpecialtyCode());
                }else{
                    subCard.setOrgIndexCode(custCardInfo.getDeptCode());
                }
            }
            subCard.setPersonId(custCardInfo.getCustId());
            List<SubCard> subCards = new ArrayList<>();
            subCards.add(subCard);

            card.setCardList(subCards);
            String body = JSONObject.toJSON(card).toString();
            String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
            System.out.println("API Result:"+result);
        });
    }

}
