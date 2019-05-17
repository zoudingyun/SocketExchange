package per.zdy.socketexchange.domain.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import per.zdy.socketexchange.domain.pojo.AllUserPass;
import per.zdy.socketexchange.domain.worker.Socket2SocketWorker;
import per.zdy.socketexchange.threadPool.WorkerThreadPoolCenter;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import static per.zdy.socketexchange.share.PublicVariable.userPassesMap;
import static per.zdy.socketexchange.web.ServerCenterWebSocketController.sendConsoleInfo;
import static per.zdy.socketexchange.web.ServerCenterWebSocketController.sendUserWorkInfo;

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
                    &&!jsonObject.get("userId").equals("")&&!jsonObject.get("userPwd").equals("")){
                try{
                    List<AllUserPass> allUserPasses = userPassesMap.get(jsonObject.get("userId").toString()+jsonObject.get("userPwd").toString());
                    if (allUserPasses == null){
                        JSONObject reJsonObject = new JSONObject();
                        reJsonObject.put("code","401");
                        reJsonObject.put("message","请核对用户信息！");
                        outputStream.write(JSONUtil.toJsonStr(reJsonObject).getBytes("UTF-8"));
                        outputStream.flush();
                        return;
                    }else {
                        for (AllUserPass allUserPassTmp :allUserPasses){
                            if (allUserPassTmp.getIp().equals(jsonObject.get("targetIp"))||allUserPassTmp.getIp().equals("*")){
                                if (allUserPassTmp.getPort().equals(jsonObject.get("targetPort")+"")||allUserPassTmp.getPort().equals("*")){
                                    try{
                                        sendUserWorkInfo(allUserPassTmp.getUserName()+" 尝试主动连接-"+jsonObject.get("targetIp").toString()+":"+jsonObject.get("targetPort").toString());

                                        Socket targetSocket = new Socket(jsonObject.get("targetIp").toString()
                                                ,Integer.parseInt(jsonObject.get("targetPort").toString()));

                                        Socket2SocketWorker socket2SocketWorker = new Socket2SocketWorker(requestSocket,targetSocket,allUserPassTmp,true);
                                        workerThreadPoolCenter.newThread(socket2SocketWorker);
                                        Socket2SocketWorker socket2SocketWorker2 = new Socket2SocketWorker(targetSocket,requestSocket,allUserPassTmp);
                                        workerThreadPoolCenter.newThread(socket2SocketWorker2);

                                        JSONObject reJsonObject = new JSONObject();
                                        reJsonObject.put("code","200");
                                        reJsonObject.put("message","ok");
                                        outputStream.write(JSONUtil.toJsonStr(reJsonObject).getBytes("UTF-8"));
                                        outputStream.flush();
                                        return;
                                    }catch (Exception ex){
                                        sendUserWorkInfo(allUserPassTmp.getUserName()+" 连接-"+jsonObject.get("targetIp").toString()+":"+jsonObject.get("targetPort").toString()+"失败！原因："+ex.getMessage());
                                        JSONObject reJsonObject = new JSONObject();
                                        reJsonObject.put("code","402");
                                        reJsonObject.put("message","服务器无法连接目标地址："+ex.getMessage());
                                        outputStream.write(JSONUtil.toJsonStr(reJsonObject).getBytes("UTF-8"));
                                        outputStream.flush();
                                        return;
                                    }
                                }
                            }
                        }
                        JSONObject reJsonObject = new JSONObject();
                        reJsonObject.put("code","401");
                        reJsonObject.put("message","权限不足，禁止访问！");
                        outputStream.write(JSONUtil.toJsonStr(reJsonObject).getBytes("UTF-8"));
                        outputStream.flush();
                        return;
                    }


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
