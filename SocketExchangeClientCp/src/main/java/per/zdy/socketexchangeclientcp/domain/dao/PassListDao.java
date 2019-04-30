package per.zdy.socketexchangeclientcp.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;

import java.util.List;


/**
 * 转发通道数据库表操作
 * @author ZDY
 * */
public interface PassListDao extends JpaRepository<PassList, Long> {
    /**插入*/
    @Override
    PassList save(PassList passList);

    /**查询*/
    @Query(nativeQuery = true,value="SELECT * FROM PASS_LIST WHERE DELETE_FLAG = 0 order by id")
    List<PassList> findAllPassList();

    /**缓存配置*/
    @Modifying@Transactional
    @Query(nativeQuery = true,value="update PASS_LIST set DELETE_FLAG  =1")
    void updateHistoryAllPassList();

    /**删除历史*/
    @Modifying@Transactional
    @Query(nativeQuery = true,value="delete PASS_LIST where DELETE_FLAG  >0")
    void deleteHistoryAllPassList();


}
