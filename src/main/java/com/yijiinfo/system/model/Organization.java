package com.yijiinfo.system.model;

import lombok.Data;

@Data
public class Organization {

    public Organization(String orgIndexCode,String orgName){
        this.orgIndexCode = orgIndexCode;
        this.orgName = orgName;
        this.parentIndexCode = "root000000";
    }

    private String parentIndexCode;
    private String orgName;
    private String orgIndexCode;
}
