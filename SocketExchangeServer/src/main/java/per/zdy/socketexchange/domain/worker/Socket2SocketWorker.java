package per.zdy.socketexchange.domain.worker;

import cn.hutool.log.LogFactory;
import per.zdy.socketexchange.domain.pojo.AllUserPass;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static per.zdy.socketexchange.web.ServerCenterWebSocketController.sendUserWorkInfo;

public class Socket2SocketWorker implements Runnable {

    Socket requestSocket;
    Socket targetSocket;
    AllUserPass userPass;
    Boolean sendMessage = false;

    public Socket2SocketWorker(Socket requestSocket, Socket targetSocket, AllUserPass userPass,Boolean sendMessage) {
        this.requestSocket = requestSocket;
        this.targetSocket = targetSocket;
        this.userPass = userPass;
        this.sendMessage = sendMessage;
    }

    public Socket2SocketWorker(Socket requestSocket, Socket targetSocket, AllUserPass userPass) {
        this.requestSocket = requestSocket;
        this.targetSocket = targetSocket;
        this.userPass = userPass;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            //获取输入流，接受客户端请求
            OutputStream outputStream=requestSocket.getOutputStream();
            //获取一个输出流，向服务端发送信息
            InputStream inputStream = targetSocket.getInputStream();
            byte[] temp = new byte[10240];
            //long fileCount = 0;
            while (true){
                int ret =inputStream.read(temp);
/*                if (ret==10240){
                    int a=0;
                }*/
                outputStream.write(temp,0,ret);
                outputStream.flush();
            }
        } catch (Exception e) {
            LogFactory.get().info("worker thread exit!");
            try {
                requestSocket.close();
                targetSocket.close();
            }catch (Exception ex){
                LogFactory.get().error(ex);
            }
            try{
                if (sendMessage&&((System.currentTimeMillis() - start)/1000)>2) {
                    sendUserWorkInfo(userPass.getUserName() + " 断开与" + userPass.getIp() + ":" + userPass.getPort() + "的连接，持续时间" + (System.currentTimeMillis() - start) / 1000 + "秒");
                }
            }catch (Exception ex){
                LogFactory.get().error(ex.getMessage());
            }
        }
    }

}
