package per.zdy.socketexchangeclientcp.task;

import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import per.zdy.socketexchangeclientcp.core.ConsoleStream;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;


import javax.annotation.PostConstruct;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
}
