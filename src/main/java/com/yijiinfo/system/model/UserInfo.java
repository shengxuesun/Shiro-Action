package com.yijiinfo.system.model;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID =1L;
    private Integer id;
    private Integer personType;
    private String username;
    private Integer gender;
    private String sno;
    private String classes;
    private String department;
    private String mobile;
    private Integer ethnic;
    private String dorm;
    private String startDate;
    private Integer idType;
    private String idNo;
    private byte[] photo;
    private Date createTime;
    private Integer auditStatus;
    private Time auditTime;
    private Integer updateHik;
    private Integer isNew;
    @Override
    public String toString() {
        return "CustCardInfo{" +
                "personType  =" + personType +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", sno='" + sno + '\'' +
                ", classes='" + classes + '\'' +
                ", department='" + department + '\'' +
                ", mobile='" + mobile + '\'' +
                ", ethnic='" + ethnic + '\'' +
                ", dorm='" + dorm + '\'' +
                ", startDate='" + startDate + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", updateHik='" + updateHik + '\'' +
                '}';
    }
}
