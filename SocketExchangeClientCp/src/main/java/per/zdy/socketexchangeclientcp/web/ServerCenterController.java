package per.zdy.socketexchangeclientcp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.share.Result;
import per.zdy.socketexchangeclientcp.share.ResultGenerator;

import java.util.List;


/**
 * web管理界面controller
 * @author ZDY
 * */
@RequestMapping("/")
@RestController
public class ServerCenterController {

    @Autowired
    ServerCenterService serverCenterService;

    /**
     * 启动监听服务
     * */
    @PostMapping("/startServer")
    @CrossOrigin
    public Result startServer() {
        try{
            //serverCenterService.server(10087);
            return ResultGenerator.genSuccessResult();
        }catch (Exception ex){
            return ResultGenerator.genFailResult(ex.getMessage());
        }
    }

    /**
     * 关闭监听服务
     * */
    @PostMapping("/offServer")
    @CrossOrigin
    public Result offServer() {
        //serverCenterService.server(10087);
        try{
            return ResultGenerator.genSuccessResult();
        }catch (Exception ex){
            return ResultGenerator.genFailResult(ex.getMessage());
        }

    }

    /**
     * 上传通道
     * */
    @PostMapping("/uploadPass")
    @CrossOrigin
    public Result uploadPass(@RequestBody List<PassList> passPojo) {
        try{
            for (PassList passList:passPojo){
                serverCenterService.save(passList);
            }
            return ResultGenerator.genSuccessResult();
        }catch (Exception ex){
            return ResultGenerator.genFailResult(ex.getMessage());
        }

    }

}
