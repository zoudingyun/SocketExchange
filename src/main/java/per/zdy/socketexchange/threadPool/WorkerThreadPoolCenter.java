package per.zdy.socketexchange.threadPool;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主功能线程池控制中心
 * @author zdy
 * */

@Service
public class WorkerThreadPoolCenter {
    /**核心线程池大小*/
    int corePoolSize = 2;

    /**最大线程池大小*/
    int maximumPoolSize = 4;

    /**线程最大空闲时间*/
    long keepAliveTime = 10;

    /**时间单位*/
    TimeUnit unit = TimeUnit.SECONDS;

    /**线程等待队列*/
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);

    /**线程创建工厂*/
    ThreadFactory threadFactory = new NameTreadFactory();

    /**拒绝策略*/
    RejectedExecutionHandler handler = new MyIgnorePolicy();

    /**线程池*/
    ThreadPoolExecutor executor;


    @PostConstruct
    public void threadPoolCreate() throws Exception {
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, threadFactory, handler);
        //executor.prestartAllCoreThreads(); // 预启动所有核心线程
    }

    public void newThread(Runnable command){
        if (command == null){
            throw new NullPointerException();
        }
        else {
            executor.execute(command);
        }
    }

    class NameTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
            System.out.println(t.getName() + " has been created");
            return t;
        }
    }

     class MyIgnorePolicy implements RejectedExecutionHandler {

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            System.err.println( r.toString() + " rejected");
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }


}
