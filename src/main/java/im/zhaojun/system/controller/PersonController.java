package im.zhaojun.system.controller;

import im.zhaojun.common.annotation.OperationLog;
import im.zhaojun.common.util.ResultBean;
import im.zhaojun.system.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/custCardInfo")
public class PersonController {
    @Resource
    private PersonService personService;

    @GetMapping("/person")
    public String index() {
        return "custCardInfo/person-operation";
    }


    @OperationLog("同步组织信息")
    @PostMapping("/syncPerson")
    @ResponseBody
    public ResultBean syncPerson() {
        personService.syncPerson();
        return ResultBean.success();
    }
    @OperationLog("列表人员信息")
    @PostMapping("/listPerson")
    @ResponseBody
    public ResultBean listPerson() {
        personService.listPerson();
        return ResultBean.success();
    }
}
