package per.zdy.socketexchange.domain.server;

import per.zdy.socketexchange.threadPool.ServerThreadPoolCenter;
import java.net.ServerSocket;
import java.net.Socket;

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
            while (true) {
                Socket ssocket = serverSocket.accept();
                for (int i = 1; i <= 10; i++) {
                    ServerDispatcher myTask = new ServerDispatcher(i,ssocket);
                    serverThreadPoolCenter.newThread(myTask);
                }
            }
        }catch (Exception ex){

        }
    }

}
