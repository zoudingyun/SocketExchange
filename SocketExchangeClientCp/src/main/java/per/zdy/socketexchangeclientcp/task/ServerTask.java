package per.zdy.socketexchangeclientcp.task;

import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;


import javax.annotation.PostConstruct;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.remoteAddress;
import static per.zdy.socketexchangeclientcp.share.PublicVariable.remotePort;

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
            remoteAddress = address;
            remotePort = port;
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }

    }
}
