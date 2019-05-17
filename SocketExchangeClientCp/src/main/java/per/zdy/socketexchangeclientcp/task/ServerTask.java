package per.zdy.socketexchangeclientcp.task;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import per.zdy.socketexchangeclientcp.core.ConsoleStream;
import per.zdy.socketexchangeclientcp.domain.Pojo.RequestInfoPojo;
import per.zdy.socketexchangeclientcp.domain.Pojo.UserInfo;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;


import javax.annotation.PostConstruct;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.serverAddress;
import static per.zdy.socketexchangeclientcp.share.PublicVariable.serverPort;
import static per.zdy.socketexchangeclientcp.share.PublicVariable.osStr;


/**
 * 初始化服务
 * @author zdy
 * */
@Service
public class ServerTask {

    @Autowired
    ServerThreadPoolCenter serverThreadPoolCenter;

    @Autowired
    WorkerThreadPoolCenter workerThreadPoolCenter;

    @Autowired
    ServerCenterService serverCenterService;



    @Value("${server.remoteAddress}")
    public String address;

    @Value("${server.remotePort}")
    public int Port;



    @Value("${server.remotePort}")
    int port;

    @PostConstruct
    public void run(){

        try {
            //初始化线程池
            serverThreadPoolCenter.threadPoolCreate();
            workerThreadPoolCenter.threadPoolCreate();

            //初始化远端服务器配置
            serverAddress = address;
            serverPort = port;

            String name = signIn();



            //将原来的System.out交给printStream 对象保存
            PrintStream old = System.out;
            ConsoleStream newStream = new ConsoleStream(old);
            //设置新的out
            System.setOut(new PrintStream(newStream));

            //判断操作系统
            osStr = System.getProperty("os.name");
            LogFactory.get().info("System Os:"+osStr);

        }catch (Exception ex){
            LogFactory.get().error(ex);
        }

    }

    private String signIn() throws Exception{

        UserInfo userInfo = serverCenterService.queryUser();

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
            serverCenterService.saveUser(userInfo);
            return reJsonObject.get("userName").toString();
        }else {
            userInfo.setUserName("null");
            serverCenterService.saveUser(userInfo);
            return null;
        }

    }
}
