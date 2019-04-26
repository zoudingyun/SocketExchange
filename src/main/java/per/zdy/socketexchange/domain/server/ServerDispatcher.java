package per.zdy.socketexchange.domain.server;

import java.net.Socket;

/**
 * 负责处理用户请求，并根据请求类型新建
 * @author ZDY
 * */
public class ServerDispatcher implements Runnable {

    Socket requestSocket;
    Socket targetSocket;

    public ServerDispatcher(Socket requestSocket,Socket targetSocket) {
        this.requestSocket = requestSocket;
        this.targetSocket = targetSocket;
    }

    @Override
    public void run() {
        try{
            System.out.println("Socket is running!");
            Thread.sleep(5000);

        }catch (Exception ex){

        }


    }

}
