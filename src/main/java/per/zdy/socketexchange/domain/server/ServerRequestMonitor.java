package per.zdy.socketexchange.domain.server;

import cn.hutool.log.LogFactory;
import per.zdy.socketexchange.threadPool.ServerThreadPoolCenter;
import java.net.ServerSocket;
import java.net.Socket;

import static per.zdy.socketexchange.share.PublicVariable.socketServerOnline;

/**
 * 负责监听新用户连接并分发用户请求至子线程
 * @author ZDY
 * */
public class ServerRequestMonitor implements Runnable {


    int port=0;
    ServerThreadPoolCenter serverThreadPoolCenter;

    public ServerRequestMonitor(int port,ServerThreadPoolCenter serverThreadPoolCenter){
        this.port=port;
        this.serverThreadPoolCenter = serverThreadPoolCenter;
    }

    @Override
    public void run(){
        try {
            //监听服务端口
            ServerSocket serverSocket=new ServerSocket(port);
            socketServerOnline=true;
            while (true) {
                Socket ssocket = serverSocket.accept();
                if (!socketServerOnline){
                    break;
                }

                ServerDispatcher myTask = new ServerDispatcher(ssocket,new Socket("172.30.200.50",3389));
                serverThreadPoolCenter.newThread(myTask);
            }
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }
    }

}
