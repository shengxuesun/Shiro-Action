package im.zhaojun.system.model;

import lombok.Data;

import java.util.List;

@Data
public class Card {
    private String startDate;
    private String endDate;
    private List<SubCard> cardList;

}
