package im.zhaojun.system.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustCardInfo implements Serializable {

    private static final long serialVersionUID =1L;

    @JsonProperty("stuEmpNo")
    private String stuEmpNo;

    private String custName;

    private String cardNo;

    private Integer cardStatus;

    private String statusName;

    private String showCardNo;

    private Integer pageNo = 1;

    private Integer pageSize = 10;
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
