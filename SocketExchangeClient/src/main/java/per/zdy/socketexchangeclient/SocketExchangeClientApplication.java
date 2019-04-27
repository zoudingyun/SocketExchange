package per.zdy.socketexchangeclient;


import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import per.zdy.socketexchangeclient.domain.JavaTest;

import javax.swing.*;
import java.awt.*;


@SpringBootApplication
public class SocketExchangeClientApplication {

    public static void main(String[] args) {
        //因为是图形化所以禁用headless模式
        System.setProperty("java.awt.headless","false");
        SpringApplication.run(SocketExchangeClientApplication.class, args);

        final String url = "http://127.0.0.1:8848/static/index.html";
        final String title = "SocketExchange";
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setSize(800 , 600);


        //禁用close功能
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //不显示标题栏,最大化,最小化,退出按钮
        //frame.setUndecorated(true);
        frame.add(view, BorderLayout.CENTER);

        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        browser.loadURL(url);
        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                super.onFinishLoadingFrame(event);
                if (event.isMainFrame()) {
                    //browser.executeJavaScript("alert('java調用了js')");

                    JSValue window = browser.executeJavaScriptAndReturnValue("window");
                    // 给jswindows对象添加一个扩展的属性
                    JavaTest javaObject = new JavaTest();
                    window.asObject().setProperty("javaTest", javaObject);
                }
            }
        });


    }

}
