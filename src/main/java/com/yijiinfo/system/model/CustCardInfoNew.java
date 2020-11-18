package com.yijiinfo.system.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustCardInfoNew implements Serializable {

    private static final long serialVersionUID =1L;

    @JsonProperty("stuEmpNo")
    private String stuEmpNo;

    private String custName;

    private String cardNo;

    private Integer cardStatus;

    private String statusName;

    private String showCardNo;

    private String cardPhyId;
    private String expireDate;
    private String openDate;
    private String cardVerNo;
    private String cardType;
    private String cardTypeName;
    private String custId;
    private String custType;
    private String custTypeName;
    private String deptCode;
    private String deptName;
    private String specialtyCode;
    private String specialtyName;
    private String sex;
    private String idType;
    private String idTypeName;
    private String idNo;
    private String areaCode;
    private String areaName;
    private String classCode;
    private String className;
    private String countryCode;
    private String country;
    private String email;
    private String nationCode;
    private String nation;
    private String inDate;
    private String outDate;
    private String tel;
    private String mobile;
    private String addr;
    private String zipCode;
    private String cardUpdTime;
    private String custUpdTime;
    private String photo;
    private String photoExactTime;
    private String id;

    @Override
    public String toString() {
        return "CustCardInfo{" +
                "STUEMPNO  =" + stuEmpNo +
                ", CUSTNAME='" + custName + '\'' +
                ", CARDNO='" + cardNo + '\'' +
                ", CARDSTATUS='" + cardStatus + '\'' +
                ", STATUSNAME='" + statusName + '\'' +
                ", SHOWCARDNO='" + showCardNo + '\'' +
                '}';
    }
}
