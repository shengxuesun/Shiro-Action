package im.zhaojun.system.mapper.db2;

import im.zhaojun.system.model.CustCardInfo;
import im.zhaojun.system.model.Organization;
import im.zhaojun.system.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

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


}