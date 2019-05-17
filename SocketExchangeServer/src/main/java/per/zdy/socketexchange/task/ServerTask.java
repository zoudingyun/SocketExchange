package per.zdy.socketexchange.task;

import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import per.zdy.socketexchange.domain.dao.UserInfoDao;
import per.zdy.socketexchange.domain.pojo.AllUserPass;
import per.zdy.socketexchange.domain.pojo.UserPass;
import per.zdy.socketexchange.service.ServerCenterService;
import per.zdy.socketexchange.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchange.threadPool.WorkerThreadPoolCenter;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

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

            List<AllUserPass> userPasses = serverCenterService.queryAllUserPass();

            //启动监听服务
            serverCenterService.server(port);

        }catch (Exception ex){
            LogFactory.get().error(ex);
        }

    }
}
