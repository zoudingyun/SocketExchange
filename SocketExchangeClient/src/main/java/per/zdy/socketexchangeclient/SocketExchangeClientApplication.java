package per.zdy.socketexchangeclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class SocketExchangeClientApplication {

    public static void main(String[] args) {
        //因为是图形化所以禁用headless模式
        System.setProperty("java.awt.headless","false");
        SpringApplication.run(SocketExchangeClientApplication.class, args);

        JFrame frame = new JFrame();
        frame.setSize(800 , 600);
        //禁用close功能
        //frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //不显示标题栏,最大化,最小化,退出按钮
        //frame.setUndecorated(true);
        //frame.add(view, BorderLayout.CENTER);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

    }

}
