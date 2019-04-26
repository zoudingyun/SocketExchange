package per.zdy.socketexchange.service;

public interface ServerCenterService  {

    /**
     * 服务入口，接受新的用户请求
     * @param port 监听端口
     * */
    public void server(int port);

    /**
     * 获取线程池活动线程数
     * */
    public int queryActiveThreadCount();

}
