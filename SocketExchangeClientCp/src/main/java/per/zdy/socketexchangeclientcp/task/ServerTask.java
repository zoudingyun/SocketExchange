package per.zdy.socketexchangeclientcp.task;

import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;


import javax.annotation.PostConstruct;

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


    @Value("${socketPort}")
    int port;

    @PostConstruct
    public void run(){

        try {
            //初始化线程池
            serverThreadPoolCenter.threadPoolCreate();
            workerThreadPoolCenter.threadPoolCreate();

            //启动监听服务
            serverCenterService.server(port);
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }

    }
}
