package com.yijiinfo.system.mapper.db2;

import com.yijiinfo.system.model.CustCardInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustCardInfoMapper {

    CustCardInfo selectByPrimaryKey(Integer stuempno);
    /**
     * 获取所有一卡通信息
     */
    List<CustCardInfo> selectAllInfo(CustCardInfo custCardInfoQuery);

    List<CustCardInfo> organizationList();

    List<CustCardInfo> personList();

    List<CustCardInfo> photoList();

    List<CustCardInfo> photoLate8List();

    List<CustCardInfo> cardList();

    List<CustCardInfo> cardLate5List();


}