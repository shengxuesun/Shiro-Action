package im.zhaojun.system.service;

import com.github.pagehelper.PageHelper;
import im.zhaojun.system.mapper.db2.CustCardInfoMapper;
import im.zhaojun.system.model.CustCardInfo;
import im.zhaojun.system.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustCardInfoService {

    @Resource
    private CustCardInfoMapper custCardInfoMapper;

    public CustCardInfo selectByPrimaryKey(Integer deptId) {
        return custCardInfoMapper.selectByPrimaryKey(deptId);
    }

    public List<CustCardInfo> selectAllInfo(int page, int rows, CustCardInfo custCardInfoQuery) {
        PageHelper.startPage(page, rows);
        return custCardInfoMapper.selectAllInfo(custCardInfoQuery);
    }


}
