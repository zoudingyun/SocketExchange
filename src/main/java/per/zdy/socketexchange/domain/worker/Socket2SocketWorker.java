package per.zdy.socketexchange.domain.worker;

public class Socket2SocketWorker implements Runnable {
    private String name;

    public Socket2SocketWorker(String name) {
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
