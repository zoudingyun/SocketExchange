package per.zdy.socketexchangeclientcp.service;

import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.domain.Pojo.UserInfo;

import java.util.List;

public interface ServerCenterService  {

    /**
     * 启动服务
     * */
    public void server();

    /**
     * 关闭服务
     * */
    public void closeServer();

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
    public void save(List<PassList> passList);

    /**
     * 查询通道信息
     * */
    public List<PassList> queryPass();

    /**
     * 查询通道数量
     * */
    public int queryPassCount();

    /**
     * 查询用户信息
     * @return 用户信息
     * */
    public UserInfo queryUser();

    /**
     * 保存用户信息
     * */
    public void saveUser(UserInfo userInfo);


}
