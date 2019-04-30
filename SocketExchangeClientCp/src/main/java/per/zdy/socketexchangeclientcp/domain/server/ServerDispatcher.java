package per.zdy.socketexchangeclientcp.domain.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.LogFactory;
import per.zdy.socketexchangeclientcp.domain.Pojo.RequestInfoPojo;
import per.zdy.socketexchangeclientcp.domain.worker.Socket2SocketWorker;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.remoteAddress;
import static per.zdy.socketexchangeclientcp.share.PublicVariable.remotePort;

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
            Socket targetSocket = new Socket(remoteAddress,remotePort);
            RequestInfoPojo requestInfoPojo = new RequestInfoPojo();
            requestInfoPojo.setTargetIp("10.80.48.209");
            requestInfoPojo.setTargetPort(3389);
            requestInfoPojo.setUserName("zdy");
            requestInfoPojo.setUserPwd("123456");
            JSONObject jsonObject = JSONUtil.parseObj(requestInfoPojo);

            OutputStream outputStream = targetSocket.getOutputStream();
            byte[] send = (JSONUtil.toJsonStr(jsonObject)+'\n').getBytes("UTF-8");
            outputStream.write(send,0,send.length);
            outputStream.flush();

            byte[] bytes = new byte[10240];
            StringBuilder sb = new StringBuilder();
            InputStream inputStream = targetSocket.getInputStream();
            int ret = inputStream.read(bytes);
            sb.append(new String(bytes, 0, ret,"UTF-8"));
            //接受服务器的返回
            JSONObject reJsonObject = JSONUtil.parseObj(sb);
            if(reJsonObject.get("code").equals("200")){
                try{
                    Socket2SocketWorker socket2SocketWorker = new Socket2SocketWorker(requestSocket,targetSocket);
                    workerThreadPoolCenter.newThread(socket2SocketWorker);
                    Socket2SocketWorker socket2SocketWorker2 = new Socket2SocketWorker(targetSocket,requestSocket);
                    workerThreadPoolCenter.newThread(socket2SocketWorker2);
                }catch (Exception ex){
                    LogFactory.get().error(ex);
                }
            }else {
                LogFactory.get().error(reJsonObject.getStr("message"));
            }


        }catch (Exception ex){
            LogFactory.get().error(ex);
        }
    }
}
