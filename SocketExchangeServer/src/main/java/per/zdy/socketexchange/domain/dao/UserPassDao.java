package per.zdy.socketexchange.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import per.zdy.socketexchange.domain.pojo.UserPass;

import java.util.List;


/**
 * 用户数据库表操作
 * @author ZDY
 * */
@Service
public interface UserPassDao extends JpaRepository<UserPass, Long> {
    /**插入*/
    @Override
    UserPass save(UserPass userPass);

    /**查询通道*/
    @Query(nativeQuery = true,value="SELECT * FROM USER_PASS")
    List<UserPass> findAllUserPass();

    /**删除历史*/
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value="delete USER_PASS  ")
    void deleteHistoryAllUserPass();
    
}
