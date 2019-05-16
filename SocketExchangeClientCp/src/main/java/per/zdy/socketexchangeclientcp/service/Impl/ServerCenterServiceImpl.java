package per.zdy.socketexchangeclientcp.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.domain.Pojo.UserInfo;
import per.zdy.socketexchangeclientcp.domain.dao.PassListDao;
import per.zdy.socketexchangeclientcp.domain.dao.UserInfoDao;
import per.zdy.socketexchangeclientcp.domain.server.ServerRequestMonitor;
import per.zdy.socketexchangeclientcp.service.ServerCenterService;
import per.zdy.socketexchangeclientcp.share.PublicVariable;
import per.zdy.socketexchangeclientcp.share.Result;
import per.zdy.socketexchangeclientcp.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchangeclientcp.threadPool.WorkerThreadPoolCenter;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.*;

@Service
public class ServerCenterServiceImpl implements ServerCenterService {

    @Autowired
    ServerThreadPoolCenter serverThreadPoolCenter;

    @Autowired
    WorkerThreadPoolCenter workerThreadPoolCenter;

    @Autowired
    PassListDao passListDao;

    @Autowired
    UserInfoDao userInfoDao;


    @Override
    public void server(){
        //启动服务
        serverState=true;
        List<PassList> passLists = passListDao.findAllPassList();
        if (passLists.size()>0){
            List<PassList> errorPassList = new ArrayList<>();
            for (PassList passList:passLists){
                try {
                    ServerRequestMonitor serverRequestMonitor =
                            new ServerRequestMonitor(Integer.parseInt(passList.getAgentPort()),
                                    passList.getRemoteAdd(),
                                    Integer.parseInt(passList.getRemotePort()),
                                    serverThreadPoolCenter,
                                    workerThreadPoolCenter,
                                    queryUser());
                    serverThreadPoolCenter.newThread(serverRequestMonitor);
                    passCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                    errorPassList.add(passList);
                }
            }
            if (isWin()){
                freshHostWin(passLists);
            }

        }else {
            try {
                throw new Exception("no passLists!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void closeServer(){
        serverState=false;
        List<PassList> passLists = passListDao.findAllPassList();
        for (PassList passList:passLists){
            try {
                Socket targetSocket = new Socket();
                targetSocket.connect(new InetSocketAddress("127.0.0.1",Integer.parseInt(passList.getAgentPort())),500);
                targetSocket.close();
                passCount=0;
            }catch (Exception ex){
                continue;
            }
        }
    }

    @Override
    public int queryServerActiveThreadCount(){
        return serverThreadPoolCenter.queryActiveThreadCount();
    }

    @Override
    public int queryWorkerActiveThreadCount(){
        return workerThreadPoolCenter.queryActiveThreadCount();
    }

    @Override
    public void save(List<PassList> passLists){
        passListDao.deleteHistoryAllPassList();
        passListDao.updateHistoryAllPassList();
        for (PassList passList:passLists){
            passListDao.save(passList);
        }

    }

    @Override
    public List<PassList> queryPass(){
        return passListDao.findAllPassList();
    }

    @Override
    public int queryPassCount(){
        return passCount;
    }

    @Override
    public UserInfo queryUser(){
        List<UserInfo> userInfos = userInfoDao.findAllUserInfo();
        if (userInfos.size()==1){
            return userInfos.get(0);
        }else {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId("test");
            userInfo.setUserName("测试用户");
            userInfo.setUserPwd("123456");
            return userInfo;
        }
    }

    @Override
    public void saveUser(UserInfo userInfo){
        userInfoDao.deleteHistoryAllUserInfo();
        userInfoDao.save(userInfo);
    }
}
