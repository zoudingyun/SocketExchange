package per.zdy.socketexchangeclientcp.domain.server;

import cn.hutool.log.LogFactory;
import per.zdy.socketexchangeclientcp.domain.worker.Socket2SocketWorker;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;

import java.net.Socket;

/**
 * 负责处理用户请求，并根据请求类型新建
 * @author ZDY
 * */
public class ServerDispatcher implements Runnable {

    WorkerThreadPoolCenter workerThreadPoolCenter;

    Socket requestSocket;
    Socket targetSocket;

    public ServerDispatcher(Socket requestSocket,Socket targetSocket,WorkerThreadPoolCenter workerThreadPoolCenter) {
        this.requestSocket = requestSocket;
        this.targetSocket = targetSocket;
        this.workerThreadPoolCenter = workerThreadPoolCenter;
    }

    @Override
    public void run() {
        try{


            Socket2SocketWorker socket2SocketWorker = new Socket2SocketWorker(requestSocket,targetSocket);
            workerThreadPoolCenter.newThread(socket2SocketWorker);
            Socket2SocketWorker socket2SocketWorker2 = new Socket2SocketWorker(targetSocket,requestSocket);
            workerThreadPoolCenter.newThread(socket2SocketWorker2);
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }
    }
}
