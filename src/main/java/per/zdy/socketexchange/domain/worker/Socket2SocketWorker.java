package per.zdy.socketexchange.domain.worker;

import cn.hutool.log.LogFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Socket2SocketWorker implements Runnable {

    Socket requestSocket;
    Socket targetSocket;

    public Socket2SocketWorker(Socket requestSocket,Socket targetSocket) {
        this.requestSocket = requestSocket;
        this.targetSocket = targetSocket;
    }

    @Override
    public void run() {
        try {
            LogFactory.get().info("worker thread created!");
            //获取输入流，接受客户端请求
            OutputStream outputStream=requestSocket.getOutputStream();
            //获取一个输出流，向服务端发送信息
            InputStream inputStream = targetSocket.getInputStream();
            byte[] temp = new byte[10240];
            while (true){
                int ret =inputStream.read(temp);
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

        }
    }

}
