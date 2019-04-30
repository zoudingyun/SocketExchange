package per.zdy.socketexchangeclientcp.domain.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.domain.Pojo.User;

import java.time.LocalDate;
import java.util.List;

public interface PassListDao extends JpaRepository<PassList, Long> {
    /******************************create***************************************/
    @Override
    PassList save(PassList passList);
    /******************************read***************************************/

}
