package per.zdy.socketexchange.share;

import per.zdy.socketexchange.web.ServerCenterWebSocketController;

import java.net.Socket;
import java.util.concurrent.CopyOnWriteArraySet;

public class PublicVariable {
    public static Boolean socketServerOnline = false;

    //通道数
    public static int passCount;

    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    public static CopyOnWriteArraySet<ServerCenterWebSocketController> webSocketSet = new CopyOnWriteArraySet<ServerCenterWebSocketController>();
}
