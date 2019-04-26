package per.zdy.socketexchange.domain.server;

import java.net.Socket;

/**
 * 负责处理用户请求，并根据请求类型新建
 * @author ZDY
 * */
public class ServerDispatcher implements Runnable {

    Socket requestSocket;
    int num=0;

    public ServerDispatcher(int num,Socket requestSocket) {
        this.requestSocket = requestSocket;
        this.num= num;
    }

    @Override
    public void run() {
        try{
            System.out.println("Socket is running!"+num);
            Thread.sleep(5000);

        }catch (Exception ex){

        }


    }

}
