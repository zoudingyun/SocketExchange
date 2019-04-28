package per.zdy.socketexchange.domain.server;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import per.zdy.socketexchange.domain.worker.Socket2SocketWorker;
import per.zdy.socketexchange.threadPool.WorkerThreadPoolCenter;

import java.net.Socket;

/**
 * 负责处理用户请求，并根据请求类型新建
 * @author ZDY
 * */
public class ServerDispatcher implements Runnable {

    WorkerThreadPoolCenter workerThreadPoolCenter;

    Socket requestSocket;

    public ServerDispatcher(Socket requestSocket,WorkerThreadPoolCenter workerThreadPoolCenter) {
        this.requestSocket = requestSocket;
        this.workerThreadPoolCenter = workerThreadPoolCenter;
    }

    @Override
    public void run() {
        try{
            Socket targetSocket = new Socket("10.80.48.144",61616);

            Socket2SocketWorker socket2SocketWorker = new Socket2SocketWorker(requestSocket,targetSocket);
            workerThreadPoolCenter.newThread(socket2SocketWorker);
            Socket2SocketWorker socket2SocketWorker2 = new Socket2SocketWorker(targetSocket,requestSocket);
            workerThreadPoolCenter.newThread(socket2SocketWorker2);
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }
    }
}
