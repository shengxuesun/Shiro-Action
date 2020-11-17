package com.yijiinfo.system.service;

import com.yijiinfo.system.mapper.db1.RoleOperatorMapper;
import com.yijiinfo.system.model.RoleOperator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoleOperatorService {

    @Resource
    private RoleOperatorMapper roleOperatorMapper;

    public int insert(RoleOperator roleOperator) {
        return roleOperatorMapper.insert(roleOperator);
    }

}
