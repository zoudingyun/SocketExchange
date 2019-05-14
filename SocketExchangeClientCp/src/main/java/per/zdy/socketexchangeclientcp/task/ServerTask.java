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

            //恢复原来的System.out
            //System.setOut(oldPrintStream);
            //将bos中保存的信息输出,这就是我们上面准备要输出的内容
            //System.out.println(bos.toString());
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }

    }
}
