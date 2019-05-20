package per.zdy.socketexchangeclientcp.web;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.domain.Pojo.RequestInfoPojo;
import per.zdy.socketexchangeclientcp.domain.Pojo.UserInfo;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.share.Result;
import per.zdy.socketexchangeclientcp.share.ResultGenerator;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.serverAddress;
import static per.zdy.socketexchangeclientcp.share.PublicVariable.serverPort;


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
            String name = signIn(userInfo);
            userInfo.setUserName(name);
            serverCenterService.saveUser(userInfo);

            return ResultGenerator.genSuccessResult();
        }catch (Exception ex){
            return ResultGenerator.genFailResult(ex.getMessage());
        }
    }

    private String signIn(UserInfo userInfo) throws Exception{

        Socket targetSocket = new Socket(serverAddress,serverPort);
        RequestInfoPojo requestInfoPojo = new RequestInfoPojo();
        requestInfoPojo.setType("signIn");
        requestInfoPojo.setUserId(userInfo.getUserId());
        requestInfoPojo.setUserPwd(userInfo.getUserPwd());
        JSONObject jsonObject = JSONUtil.parseObj(requestInfoPojo);

        //向服务器发送定向请求
        OutputStream outputStream = targetSocket.getOutputStream();
        byte[] send = (JSONUtil.toJsonStr(jsonObject)+'\n').getBytes("UTF-8");
        outputStream.write(send,0,send.length);
        outputStream.flush();

        byte[] bytes = new byte[10240];
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = targetSocket.getInputStream();
        int ret = inputStream.read(bytes);
        sb.append(new String(bytes, 0, ret,"UTF-8"));
        //接受服务器的返回
        JSONObject reJsonObject = JSONUtil.parseObj(sb);
        if (!reJsonObject.get("userName").toString().equals("")){
            userInfo.setUserName(reJsonObject.get("userName").toString());
            return reJsonObject.get("userName").toString();
        }else {
            userInfo.setUserName("null");
            return "null";
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
