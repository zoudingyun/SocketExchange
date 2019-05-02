package per.zdy.socketexchangeclientcp.threadPool;

import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主功能线程池控制中心
 * @author zdy
 * */

@Service
public class WorkerThreadPoolCenter {
    /**核心线程池大小*/
    @Value("${workerThread.corePoolSize}")
    int corePoolSize;

    /**最大线程池大小*/
    @Value("${workerThread.maximumPoolSize}")
    int maximumPoolSize;

    /**线程最大空闲时间*/
    @Value("${workerThread.keepAliveTime}")
    long keepAliveTime;

    /**时间单位*/
    TimeUnit unit = TimeUnit.SECONDS;

    /**线程等待队列*/
    @Value("${workerThread.workQueue}")
    int workQueueNum;



    /**线程创建工厂*/
    ThreadFactory threadFactory = new WorkerThreadPoolCenter.NameTreadFactory();

    /**拒绝策略*/
    RejectedExecutionHandler handler = new WorkerThreadPoolCenter.MyIgnorePolicy();

    /**线程池*/
    ThreadPoolExecutor executor;


    public void threadPoolCreate() throws Exception {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(workQueueNum);
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, threadFactory, handler);
        //executor.prestartAllCoreThreads(); // 预启动所有核心线程
        LogFactory.get().info(">>>workerThreadPool Initialization complete.<<<");
    }

    public void newThread(Runnable command){
        if (command == null){
            throw new NullPointerException();
        }
        else {
            executor.execute(command);
        }
    }

    public void shutdownThread(){
        List<Runnable> lists = executor.shutdownNow();
        for (Runnable list:lists){
            executor.remove(list);
        }
    }

    public boolean removeTask(Runnable task){
        return executor.remove(task);
    }

    /**活跃线程数*/
    public int queryActiveThreadCount(){
        return executor.getActiveCount();
    }

    class NameTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
            //System.out.println(t.getName() + " has been created");
            return t;
        }
    }

     class MyIgnorePolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            LogFactory.get().error( r.toString() + " rejected");
            //System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }


}
