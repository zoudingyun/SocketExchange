package per.zdy.socketexchange.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.zdy.socketexchange.domain.dao.UserInfoDao;
import per.zdy.socketexchange.domain.dao.UserPassDao;
import per.zdy.socketexchange.domain.pojo.AllUserPass;
import per.zdy.socketexchange.domain.pojo.UserInfo;
import per.zdy.socketexchange.domain.pojo.UserPass;
import per.zdy.socketexchange.domain.server.ServerDispatcher;
import per.zdy.socketexchange.domain.server.ServerRequestMonitor;
import per.zdy.socketexchange.service.ServerCenterService;
import per.zdy.socketexchange.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchange.threadPool.WorkerThreadPoolCenter;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServerCenterServiceImpl implements ServerCenterService {

    @Autowired
    ServerThreadPoolCenter serverThreadPoolCenter;

    @Autowired
    WorkerThreadPoolCenter workerThreadPoolCenter;

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    UserPassDao userPassDao;

    @Override
    public void server(int port){
        try {
            ServerRequestMonitor serverRequestMonitor = new ServerRequestMonitor(port,serverThreadPoolCenter,workerThreadPoolCenter);
            serverThreadPoolCenter.newThread(serverRequestMonitor);
        } catch (Exception e) {
            e.printStackTrace();
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
    public List<AllUserPass> queryAllUserPass(){
        List<UserInfo> userInfos = userInfoDao.findAllUserInfo();
        List<UserPass> userPasses = userPassDao.findAllUserPass();
        List<AllUserPass> allUserPasses = new ArrayList<>();
        for (UserPass userPass:userPasses){
            if (!userPass.getTYPE().equals("group")){
                AllUserPass allUserPass = new AllUserPass();
                allUserPass.setInherit(userPass.getINHERIT());
                allUserPass.setIp(userPass.getIP());
                allUserPass.setPort(userPass.getPORT());
                allUserPass.setType(userPass.getTYPE());
                allUserPass.setUserId(userPass.getUserId());
                allUserPass.setUserPwd("");
                allUserPass.setUserName("");
                for (UserInfo userInfo:userInfos){
                    if (userInfo.getUserId().equals(allUserPass.getUserId())){
                        allUserPass.setUserPwd(userInfo.getUserPwd());
                        allUserPass.setUserName(userInfo.getUserName());
                        break;
                    }
                }


                allUserPasses.add(allUserPass);
                for (UserPass userPass1:userPasses){
                    if (allUserPass.getInherit().equals(userPass1.getUserId())){
                        AllUserPass allUserPass1 = new AllUserPass();
                        allUserPass1.setInherit(allUserPass.getInherit());
                        allUserPass1.setIp(userPass1.getIP());
                        allUserPass1.setPort(userPass1.getPORT());
                        allUserPass1.setType(allUserPass.getType());
                        allUserPass1.setUserId(allUserPass.getUserId());
                        allUserPass1.setUserPwd(allUserPass.getUserPwd());
                        allUserPass1.setUserName(allUserPass.getUserName());
                        allUserPasses.add(allUserPass1);
                    }
                }
            }
        }

        return allUserPasses;

    }
}
