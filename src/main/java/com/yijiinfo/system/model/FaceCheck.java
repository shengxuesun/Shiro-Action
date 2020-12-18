package com.yijiinfo.system.model;

import lombok.Data;

import java.util.List;

@Data
public class FaceCheck {
    private String startDate;
    private String endDate;
    private List<SubCard> cardList;

}
