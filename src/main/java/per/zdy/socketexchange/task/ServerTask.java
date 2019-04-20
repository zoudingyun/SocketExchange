package per.zdy.socketexchange.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.zdy.socketexchange.threadPool.ServerThreadPoolCenter;
import per.zdy.socketexchange.threadPool.WorkerThreadPoolCenter;

import javax.annotation.PostConstruct;

/**
 * 作为服务端使用时的监听服务
 * @author zdy
 * */
@Service
public class ServerTask {

    @Autowired
    WorkerThreadPoolCenter serverThreadPoolCenter;

    @PostConstruct
    public void run(){
        for (int i = 1; i <= 10; i++) {
            MyTask myTask = new MyTask(String.valueOf(i));
            serverThreadPoolCenter.newThread(myTask);
        }


    }


    class MyTask implements Runnable {
        private String name;

        public MyTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(this.toString() + " is running!");
                Thread.sleep(3000); //让任务执行慢点
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "MyTask [name=" + name + "]";
        }
    }

}
