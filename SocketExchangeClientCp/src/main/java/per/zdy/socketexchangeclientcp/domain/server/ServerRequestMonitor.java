package per.zdy.socketexchangeclientcp.domain.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.LogFactory;
import per.zdy.socketexchangeclientcp.domain.Pojo.RequestInfoPojo;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.byteConcat;
import static per.zdy.socketexchangeclientcp.share.PublicVariable.socketServerOnline;


/**
 * 负责监听新用户连接并分发用户请求至子线程
 * @author ZDY
 * */
public class ServerRequestMonitor implements Runnable {


    int port=0;
    ServerThreadPoolCenter serverThreadPoolCenter;
    WorkerThreadPoolCenter workerThreadPoolCenter;

    public ServerRequestMonitor(int port, ServerThreadPoolCenter serverThreadPoolCenter,WorkerThreadPoolCenter workerThreadPoolCenter){
        this.port=port;
        this.serverThreadPoolCenter = serverThreadPoolCenter;
        this.workerThreadPoolCenter = workerThreadPoolCenter;
    }

    @Override
    public void run(){
        try {
            //监听服务端口
            ServerSocket serverSocket=new ServerSocket(port);
            socketServerOnline=true;
            while (true) {
                Socket ssocket = serverSocket.accept();
                try{
                    ServerDispatcher myTask = new ServerDispatcher(ssocket,workerThreadPoolCenter);
                    serverThreadPoolCenter.newThread(myTask);
                }catch (Exception ex){
                    LogFactory.get().error(ex);
                }
            }
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }
    }

}
