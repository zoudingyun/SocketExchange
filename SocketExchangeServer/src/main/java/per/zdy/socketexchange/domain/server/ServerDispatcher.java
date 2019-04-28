package per.zdy.socketexchange.domain.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import per.zdy.socketexchange.domain.worker.Socket2SocketWorker;
import per.zdy.socketexchange.threadPool.WorkerThreadPoolCenter;

import java.io.InputStream;
import java.io.OutputStream;
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
            InputStream inputStream = requestSocket.getInputStream();
            OutputStream outputStream = requestSocket.getOutputStream();
            byte[] bytes = new byte[10240];
            int len;
            StringBuilder sb = new StringBuilder();

            int ret = inputStream.read(bytes);
            sb.append(new String(bytes, 0, ret,"UTF-8"));

            //接受到客户端的传参
            JSONObject jsonObject = JSONUtil.parseObj(sb);
            if (!jsonObject.get("targetIp").equals("")&&!jsonObject.get("targetPort").equals("")
                    &&!jsonObject.get("userName").equals("")&&!jsonObject.get("userPwd").equals("")){
                try{
                    Socket targetSocket = new Socket(jsonObject.get("targetIp").toString()
                            ,Integer.parseInt(jsonObject.get("targetPort").toString()));

                    Socket2SocketWorker socket2SocketWorker = new Socket2SocketWorker(requestSocket,targetSocket);
                    workerThreadPoolCenter.newThread(socket2SocketWorker);
                    Socket2SocketWorker socket2SocketWorker2 = new Socket2SocketWorker(targetSocket,requestSocket);
                    workerThreadPoolCenter.newThread(socket2SocketWorker2);

                    JSONObject reJsonObject = new JSONObject();
                    reJsonObject.put("code","200");
                    reJsonObject.put("message","ok");
                    outputStream.write(JSONUtil.toJsonStr(reJsonObject).getBytes("UTF-8"));
                    outputStream.flush();

                }catch (Exception ex){
                    JSONObject reJsonObject = new JSONObject();
                    reJsonObject.put("code","500");
                    reJsonObject.put("message",ex);
                    outputStream.write(JSONUtil.toJsonStr(reJsonObject).getBytes("UTF-8"));
                    outputStream.flush();
                }
            }
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }

    }
}
