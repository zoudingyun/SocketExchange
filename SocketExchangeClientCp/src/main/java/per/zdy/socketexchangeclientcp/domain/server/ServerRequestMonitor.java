package per.zdy.socketexchangeclientcp.domain.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.LogFactory;
import per.zdy.socketexchangeclientcp.domain.Pojo.RequestInfoPojo;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.*;


/**
 * 负责监听新用户连接并分发用户请求至子线程
 * @author ZDY
 * */
public class ServerRequestMonitor implements Runnable {


    int port=0;
    ServerThreadPoolCenter serverThreadPoolCenter;
    WorkerThreadPoolCenter workerThreadPoolCenter;
    String remoteAdd;
    int remotePort;

    public ServerRequestMonitor(int port,String remoteAdd,int remotePort,ServerThreadPoolCenter serverThreadPoolCenter,WorkerThreadPoolCenter workerThreadPoolCenter){
        this.port=port;
        this.serverThreadPoolCenter = serverThreadPoolCenter;
        this.workerThreadPoolCenter = workerThreadPoolCenter;
        this.remoteAdd = remoteAdd;
        this.remotePort = remotePort;
    }

    @Override
    public void run(){
        try {
            //监听服务端口
            ServerSocket serverSocket=new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
            socketServerOnline=true;
            Socket ssocket = new Socket();
            while (true) {
                ssocket.setReuseAddress(true);
                ssocket = serverSocket.accept();
                if (serverState){
                    try{
                        ServerDispatcher myTask = new ServerDispatcher(ssocket,remoteAdd,remotePort,workerThreadPoolCenter);
                        serverThreadPoolCenter.newThread(myTask);
                    }catch (Exception ex){
                        LogFactory.get().error(ex);
                    }
                }else {
                    serverSocket.close();
                    return;
                }
            }
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }
    }

}
