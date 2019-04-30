package per.zdy.socketexchangeclientcp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.domain.dao.PassListDao;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SocketExchangeClientCpApplicationTests {

	@Autowired
	PassListDao passListDao;

	@Test
	public void contextLoads() {
		List<PassList> passList = passListDao.findAllPassList();
		int a=0;
	}

}
