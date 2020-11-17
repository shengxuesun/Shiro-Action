package im.zhaojun.system.controller;

import im.zhaojun.common.annotation.OperationLog;
import im.zhaojun.common.util.ResultBean;
import im.zhaojun.system.service.CardService;
import im.zhaojun.system.service.PhotoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/custCardInfo")
public class CardController {
    @Resource
    private CardService cardService;

    @GetMapping("/card")
    public String index() {
        return "custCardInfo/card-operation";
    }


    @OperationLog("同步开卡信息")
    @PostMapping("/syncCard")
    @ResponseBody
    public ResultBean syncCard() {
        cardService.syncCard();
        return ResultBean.success();
    }

}
