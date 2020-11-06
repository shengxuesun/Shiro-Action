package im.zhaojun.system.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustCardInfo {

    @JsonProperty("stuEmpNo")
    private String stuEmpNo;

    private String custName;

    private String cardNo;

    private Integer cardStatus;

    private String statusName;

    private String showCardNo;
}
