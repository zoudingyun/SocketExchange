package per.zdy.socketexchangeclientcp.web;

import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.domain.Pojo.UserInfo;
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
            serverCenterService.server();
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
        try{
            serverCenterService.closeServer();

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
            serverCenterService.save(passPojo);
            return ResultGenerator.genSuccessResult();
        }catch (Exception ex){
            return ResultGenerator.genFailResult(ex.getMessage());
        }

    }

    /**
     * 查询通道
     * */
    @PostMapping("/queryPass")
    @CrossOrigin
    public Result queryPass() {
        try{
            return ResultGenerator.genSuccessResult(serverCenterService.queryPass());
        }catch (Exception ex){
            return ResultGenerator.genFailResult(ex.getMessage());
        }

    }

    /**
     * 查询用户
     * */
    @PostMapping("/queryUser")
    @CrossOrigin
    public Result queryUser() {
        try{
            return ResultGenerator.genSuccessResult(serverCenterService.queryUser());
        }catch (Exception ex){
            return ResultGenerator.genFailResult(ex.getMessage());
        }

    }

    /**
     * 保存用户
     * */
    @PostMapping("/saveUser")
    @CrossOrigin
    public Result saveUser(@RequestBody UserInfo userInfo) {
        try{
            serverCenterService.saveUser(userInfo);
            return ResultGenerator.genSuccessResult();
        }catch (Exception ex){
            return ResultGenerator.genFailResult(ex.getMessage());
        }

    }

    /**
     * 退出
     * */
    @PostMapping("/exit")
    @CrossOrigin
    public void exit() {
        try{
            System.exit(0);
        }catch (Exception ex){
            LogFactory.get().error(ex);
            System.exit(-1);
        }

    }

}
