package im.zhaojun.system.model;

import lombok.Data;

@Data
public class Person {
    private Integer clientId;
    private String personId;
    private String personName;
    private String orgIndexCode;
    private Integer gender;
    private String birthday; //1992-09-12
    private String phoneNo;
    private String email;
    private String certificateType;
    private String certificateNo;
    private String jobNo;
    /* idtype:1 身份证 2 护照
    select custid,custname,deptcode,sex,mobile,tel,email,idtype,idtypename,idno,stuempno from v_custcardinfo
     */
}
