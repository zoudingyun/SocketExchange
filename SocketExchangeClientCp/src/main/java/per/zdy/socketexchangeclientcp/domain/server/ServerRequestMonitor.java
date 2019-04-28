package per.zdy.socketexchangeclientcp.domain.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.LogFactory;
import per.zdy.socketexchangeclientcp.domain.Pojo.RequestInfoPojo;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.byteConcat;
import static per.zdy.socketexchangeclientcp.share.PublicVariable.socketServerOnline;


/**
 * 负责监听新用户连接并分发用户请求至子线程
 * @author ZDY
 * */
public class ServerRequestMonitor implements Runnable {


    int port=0;
    ServerThreadPoolCenter serverThreadPoolCenter;
    WorkerThreadPoolCenter workerThreadPoolCenter;

    public ServerRequestMonitor(int port, ServerThreadPoolCenter serverThreadPoolCenter,WorkerThreadPoolCenter workerThreadPoolCenter){
        this.port=port;
        this.serverThreadPoolCenter = serverThreadPoolCenter;
        this.workerThreadPoolCenter = workerThreadPoolCenter;
    }

    @Override
    public void run(){
        try {
            //监听服务端口
            ServerSocket serverSocket=new ServerSocket(port);
            socketServerOnline=true;
            while (true) {
                Socket ssocket = serverSocket.accept();
                Socket targetSocket = new Socket("127.0.0.1",10086);

                RequestInfoPojo requestInfoPojo = new RequestInfoPojo();
                requestInfoPojo.setTargetIp("172.30.200.51");
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
                        ServerDispatcher myTask = new ServerDispatcher(ssocket,targetSocket,workerThreadPoolCenter);
                        serverThreadPoolCenter.newThread(myTask);
                    }catch (Exception ex){
                        LogFactory.get().error(ex);
                    }
                }else {
                    LogFactory.get().error(reJsonObject.getStr("message"));
                }




            }
        }catch (Exception ex){
            LogFactory.get().error(ex);
        }
    }

}
