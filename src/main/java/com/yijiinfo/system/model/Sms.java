package com.yijiinfo.system.model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Sms implements Serializable {

    private static final long serialVersionUID =1L;
    private Integer id;
    private String mobile;
    private String code;
    private Date sendTime;
    private Integer verified;
    private Date verifyTime;
    @Override
    public String toString() {
        return "CustCardInfo{" +
                "mobile  =" + mobile +
                ", code='" + code + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", verified='" + verified + '\'' +
                ", verifyTime='" + verifyTime + '\'' +
                '}';
    }
}
