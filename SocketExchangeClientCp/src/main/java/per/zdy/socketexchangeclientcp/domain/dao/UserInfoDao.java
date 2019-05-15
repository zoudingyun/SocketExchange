package per.zdy.socketexchangeclientcp.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.domain.Pojo.UserInfo;

import java.util.List;


/**
 * 用户数据库表操作
 * @author ZDY
 * */
public interface UserInfoDao extends JpaRepository<UserInfo, Long> {
    /**插入*/
    @Override
    UserInfo save(UserInfo userInfo);

    /**查询通道*/
    @Query(nativeQuery = true,value="SELECT * FROM USER_INFO")
    List<UserInfo> findAllUserInfo();

    /**删除历史*/
    @Modifying@Transactional
    @Query(nativeQuery = true,value="delete USER_INFO  ")
    void deleteHistoryAllUserInfo();

}
