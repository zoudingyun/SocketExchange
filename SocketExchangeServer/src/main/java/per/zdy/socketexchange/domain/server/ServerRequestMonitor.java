package per.zdy.socketexchange.domain.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.LogFactory;
import per.zdy.socketexchange.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchange.threadPool.WorkerThreadPoolCenter;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static per.zdy.socketexchange.share.PublicVariable.socketServerOnline;

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
            while (true) {
                try{
                        socketServerOnline=true;
                        Socket ssocket = serverSocket.accept();
                        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
                        InputStream inputStream = ssocket.getInputStream();
                        OutputStream outputStream = ssocket.getOutputStream();
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
                                if (!socketServerOnline){
                                    break;
                                }
                                ServerDispatcher myTask = new ServerDispatcher(ssocket,targetSocket,workerThreadPoolCenter);
                                serverThreadPoolCenter.newThread(myTask);
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
                }
                catch (Exception ex){
                    LogFactory.get().error(ex);
                }
            }
        }catch (Exception ex){
            socketServerOnline=false;
            LogFactory.get().error(ex);
        }
    }

}
