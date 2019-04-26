package per.zdy.socketexchange.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import per.zdy.socketexchange.service.ServerCenterService;
import per.zdy.socketexchange.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchange.threadPool.WorkerThreadPoolCenter;

import javax.annotation.PostConstruct;

/**
 * 作为服务端使用时的监听服务
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

        }

    }
}
