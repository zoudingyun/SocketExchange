package per.zdy.socketexchange.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import per.zdy.socketexchange.domain.pojo.AllUserPass;
import per.zdy.socketexchange.service.ServerCenterService;

import java.util.ArrayList;
import java.util.List;

import static per.zdy.socketexchange.share.PublicVariable.userPassesMap;

@Component
public class UpdateUserPassTask {

    @Autowired
    ServerCenterService serverCenterService;

    @Scheduled(cron = "0/1 * * * * ? ")
    void run(){
        List<AllUserPass> allUserPasses = serverCenterService.queryAllUserPass();
        for (AllUserPass allUserPass:allUserPasses){
            if (userPassesMap!=null){
                List<AllUserPass> tmp = userPassesMap.get(allUserPass.getUserId()+allUserPass.getUserPwd());
                if (tmp!=null){
                    tmp.remove(allUserPass.getUserId()+allUserPass.getUserPwd());
                }
            }
            List<AllUserPass> allUserPasses1 = new ArrayList<>();
            allUserPasses1.add(allUserPass);
            for (AllUserPass tmpPass:allUserPasses){
                if (tmpPass.getUserId().equals(allUserPass.getUserId())){
                    allUserPasses1.add(tmpPass);
                    break;
                }
            }
            userPassesMap.put(allUserPass.getUserId()+allUserPass.getUserPwd(),allUserPasses1);
        }
    }

}
