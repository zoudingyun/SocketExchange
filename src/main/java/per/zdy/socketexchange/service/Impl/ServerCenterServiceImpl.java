package per.zdy.socketexchange.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.zdy.socketexchange.domain.server.ServerDispatcher;
import per.zdy.socketexchange.domain.server.ServerRequestMonitor;
import per.zdy.socketexchange.service.ServerCenterService;
import per.zdy.socketexchange.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchange.threadPool.WorkerThreadPoolCenter;

import java.net.ServerSocket;
import java.net.Socket;

@Service
public class ServerCenterServiceImpl implements ServerCenterService {

    @Autowired
    ServerThreadPoolCenter serverThreadPoolCenter;

    @Override
    public void server(int port){
        try {
            ServerRequestMonitor serverRequestMonitor = new ServerRequestMonitor(port,serverThreadPoolCenter);
            serverThreadPoolCenter.newThread(serverRequestMonitor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int queryActiveThreadCount(){
        return serverThreadPoolCenter.queryActiveThreadCount();
    }
}
