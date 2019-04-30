package per.zdy.socketexchangeclientcp.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.domain.dao.PassListDao;
import per.zdy.socketexchangeclientcp.domain.server.ServerRequestMonitor;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;

@Service
public class ServerCenterServiceImpl implements ServerCenterService {

    @Autowired
    ServerThreadPoolCenter serverThreadPoolCenter;

    @Autowired
    WorkerThreadPoolCenter workerThreadPoolCenter;

    @Autowired
    PassListDao passListDao;

    @Override
    public void server(int port){
        try {
            ServerRequestMonitor serverRequestMonitor = new ServerRequestMonitor(port,serverThreadPoolCenter,workerThreadPoolCenter);
            serverThreadPoolCenter.newThread(serverRequestMonitor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int queryServerActiveThreadCount(){
        return serverThreadPoolCenter.queryActiveThreadCount();
    }

    @Override
    public int queryWorkerActiveThreadCount(){
        return workerThreadPoolCenter.queryActiveThreadCount();
    }

    @Override
    public PassList save(PassList passList){
        return passListDao.save(passList);
    }
}
