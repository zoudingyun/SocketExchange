package per.zdy.socketexchangeclientcp.web;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.LogFactory;
import org.springframework.stereotype.Component;
import per.zdy.socketexchangeclientcp.domain.Pojo.ServerInfo;
import per.zdy.socketexchangeclientcp.share.PublicVariable;
import per.zdy.socketexchangeclientcp.share.Result;
import per.zdy.socketexchangeclientcp.share.ResultGenerator;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.webSocketSet;

@ServerEndpoint("/client/{sid}")
@Component
public class ServerCenterWebSocketController {


    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;

    //接收sid
    private String sid="";
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("sid") String sid) {
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        //在线数加1
        addOnlineCount();
        LogFactory.get().info("有新窗口开始监听:"+sid+",当前在线人数为" + getOnlineCount());
        this.sid=sid;
        try {
            ServerInfo serverInfo = new ServerInfo();
            while (true){
                serverInfo.setPassListCount(PublicVariable.passCount);
                if (PublicVariable.passCount>0){
                    serverInfo.setOnline(true);
                }else {
                    serverInfo.setOnline(false);
                }
                serverInfo.setServerMaximumPoolSize(ServerThreadPoolCenter.queryMaximumPoolSize());
                serverInfo.setWorkerMaximumPoolSize(WorkerThreadPoolCenter.queryMaximumPoolSize());
                serverInfo.setServerActiveThreadCount(ServerThreadPoolCenter.queryActiveThreadCount());
                serverInfo.setWorkerActiveThreadCount(WorkerThreadPoolCenter.queryActiveThreadCount());
                JSONObject jsonObject = JSONUtil.parseObj(serverInfo);
                sendMessage(ResultGenerator.genInfoResult(serverInfo,"clientInfo"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        } catch (IOException e) {
            LogFactory.get().error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        LogFactory.get().info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message) {
        LogFactory.get().info("收到来自窗口"+sid+"的信息:"+message);
        //群发消息
        for (ServerCenterWebSocketController item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                LogFactory.get().error(e.getMessage());
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        if (error.getMessage()!=null){
            LogFactory.get().error(error.getMessage(),"发生错误");
        }else {
            LogFactory.get().info("ws线程退出");
        }
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Result message) throws IOException {
        this.session.getBasicRemote().sendText(message.toString());
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发控制台消息
     * */
    public static void sendConsoleInfo(String message) throws IOException {
        for (ServerCenterWebSocketController item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                item.sendMessage(ResultGenerator.genConsoleMessage(message));
            } catch (IOException e) {
                continue;
            }
        }
    }

    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        for (ServerCenterWebSocketController item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        ServerCenterWebSocketController.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ServerCenterWebSocketController.onlineCount--;
    }
}



