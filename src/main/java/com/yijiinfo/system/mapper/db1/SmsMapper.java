package com.yijiinfo.system.mapper.db1;

import com.yijiinfo.system.model.Sms;
import com.yijiinfo.system.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmsMapper {

    int insert(Sms sms);

    /**
     * 获取所有sms
     */
    List<Sms> selectAll();

    Sms selectLastOneByMobile(String mobile);

    int updateVerifiedByMobileCode(Sms sms);
    /**
     * 获取所有sms by query
     */
    List<Sms> selectAllByQuery(Sms smsQuery);
    /**
     * 统计系统中有多少个用户.
     */
    int count();

    int count24Hours(String mobile);

}