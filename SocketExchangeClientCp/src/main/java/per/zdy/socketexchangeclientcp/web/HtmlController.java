package per.zdy.socketexchangeclientcp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.share.Result;
import per.zdy.socketexchangeclientcp.share.ResultGenerator;

/**
 * web管理界面controller
 * @author ZDY
 * */
@RequestMapping("/")
@Controller
public class HtmlController {

    @Autowired
    ServerCenterService serverCenterService;

    //用户界面
    @RequestMapping("/")
    @CrossOrigin
    public String indexHtml() {
        return "/index.html";
    }

}
