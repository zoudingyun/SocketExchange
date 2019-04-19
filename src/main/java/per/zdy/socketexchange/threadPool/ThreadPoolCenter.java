package per.zdy.socketexchange.threadPool;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池控制中心
 * @author zdy
 * */

@Service
public class ThreadPoolCenter {
    @PostConstruct
    public static void taskStart() throws Exception {
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

        /**创建线程池*/
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, threadFactory, handler);
        executor.prestartAllCoreThreads(); // 预启动所有核心线程

        for (int i = 1; i <= 10; i++) {
            MyTask task = new MyTask(String.valueOf(i));
            executor.execute(task);
        }

        System.in.read(); //阻塞主线程
    }

    static class NameTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
            System.out.println(t.getName() + " has been created");
            return t;
        }
    }

    public static class MyIgnorePolicy implements RejectedExecutionHandler {

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            System.err.println( r.toString() + " rejected");
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }

    static class MyTask implements Runnable {
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
