package per.zdy.socketexchangeclientcp.service;

import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;

public interface ServerCenterService  {

    /**
     * 服务入口，接受新的用户请求
     * @param port 监听端口
     * */
    public void server(int port);

    /**
     * 获取服务台线程池活动线程数
     * */
    public int queryServerActiveThreadCount();

    /**
     * 获取工作线程池活动线程数
     * */
    public int queryWorkerActiveThreadCount();


    /**
     * 新增通道信息
     * */
    public PassList save(PassList passList);

}
