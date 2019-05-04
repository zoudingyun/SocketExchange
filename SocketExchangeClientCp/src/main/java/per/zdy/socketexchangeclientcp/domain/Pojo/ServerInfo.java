package per.zdy.socketexchangeclientcp.domain.Pojo;

/**
 * 当前客户端的配置的实时信息
 * @author zdy
 * */
public class ServerInfo {

    /**监听服务最大线程数*/
    int ServerMaximumPoolSize;

    /**工作线程最大线程数*/
    int WorkerMaximumPoolSize;

    /**监听服务当前活动线程数*/
    int ServerActiveThreadCount;

    /**工作线程当前活动线程数*/
    int WorkerActiveThreadCount;

    /**配置通道数*/
    int passListCount;

    /**服务是否启动*/
    Boolean online;

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public int getServerMaximumPoolSize() {
        return ServerMaximumPoolSize;
    }

    public void setServerMaximumPoolSize(int serverMaximumPoolSize) {
        ServerMaximumPoolSize = serverMaximumPoolSize;
    }

    public int getWorkerMaximumPoolSize() {
        return WorkerMaximumPoolSize;
    }

    public void setWorkerMaximumPoolSize(int workerMaximumPoolSize) {
        WorkerMaximumPoolSize = workerMaximumPoolSize;
    }

    public int getServerActiveThreadCount() {
        return ServerActiveThreadCount;
    }

    public void setServerActiveThreadCount(int serverActiveThreadCount) {
        ServerActiveThreadCount = serverActiveThreadCount;
    }

    public int getWorkerActiveThreadCount() {
        return WorkerActiveThreadCount;
    }

    public void setWorkerActiveThreadCount(int workerActiveThreadCount) {
        WorkerActiveThreadCount = workerActiveThreadCount;
    }

    public int getPassListCount() {
        return passListCount;
    }

    public void setPassListCount(int passListCount) {
        this.passListCount = passListCount;
    }
}
