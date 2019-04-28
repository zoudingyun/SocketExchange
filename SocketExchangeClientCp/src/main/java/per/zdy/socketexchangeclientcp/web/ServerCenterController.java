package per.zdy.socketexchangeclientcp.web;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.zdy.socketexchangeclientcp.domain.Pojo.ReturnPojo;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.share.Result;
import per.zdy.socketexchangeclientcp.share.ResultGenerator;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.getJsonSuccess;

/**
 * web管理界面controller
 * @author ZDY
 * */
@RequestMapping("/")
@RestController
public class ServerCenterController {

    @Autowired
    ServerCenterService serverCenterService;

    //启动服务
    @PostMapping("/startServer")
    @CrossOrigin
    public Result startServer() {
        //serverCenterService.server(10087);
        //ReturnPojo returnPojo = getJsonSuccess();
        return ResultGenerator.genSuccessResult();
    }
}
