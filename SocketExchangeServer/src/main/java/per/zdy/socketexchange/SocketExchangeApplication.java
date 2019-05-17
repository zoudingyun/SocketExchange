package per.zdy.socketexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocketExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocketExchangeApplication.class, args);
    }

}
