package per.zdy.socketexchangeclientcp.share;

import cn.hutool.core.date.DateUtil;
import cn.hutool.log.LogFactory;
import per.zdy.socketexchangeclientcp.domain.Pojo.PassList;
import per.zdy.socketexchangeclientcp.domain.Pojo.ReturnPojo;
import per.zdy.socketexchangeclientcp.web.ServerCenterWebSocketController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class PublicVariable {

    //远程服务器地址
    public static String serverAddress;
    //远程服务器端口
    public static int serverPort;
    //通道数
    public static int passCount;
    //操作系统
    public static String osStr;

    public static Boolean isWin(){
        if (osStr.indexOf("Win")>=0){
            return true;
        }else {
            return false;
        }
    }

    public static void freshHostWin(List<PassList> passLists){
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:/Windows/System32/drivers/etc/hosts"));

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter("C:/Windows/System32/drivers/etc/hosts", true)));

            String line = null;

            List<String> hosts = new ArrayList<>();
            while ((line = br.readLine()) != null){
                hosts.add(line);
            }

            for (PassList host:passLists){
                boolean flag=true;
                for (String tmp:hosts){
                    if (tmp.indexOf(host.getAgentAdd())==(tmp.length()-host.getAgentAdd().length())){
                        flag=false;
                        break;
                    }
                }
                if (flag){
                    out.println("127.0.0.1 "+host.getAgentAdd());
                    flag=false;
                }
            }

            br.close();
            out.close();
        } catch (FileNotFoundException e) {
            LogFactory.get().error(e,"change hosts error！");
        } catch (IOException e) {
            LogFactory.get().error(e,"change hosts error！");
        }
    }

    public static void freshHostLinux(String host){

    }

    /**本地监听服务启动状态（true启动，false关闭）*/
    public static Boolean serverState = false;

    public static Boolean socketServerOnline = false;

    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    public static CopyOnWriteArraySet<ServerCenterWebSocketController> webSocketSet = new CopyOnWriteArraySet<ServerCenterWebSocketController>();

    public static byte[] byteConcat(byte[] a, byte[] b) {
        byte[] c= new byte[a.length+b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    public static ReturnPojo getJsonSuccess(){
        ReturnPojo returnPojo = new ReturnPojo();
        returnPojo.setCode(200);
        returnPojo.setMessage("success");

        return returnPojo;
    }

    public static String getTimeNow(){
        return DateUtil.now();
    }

    public static ReturnPojo getJsonFailed(){
        ReturnPojo returnPojo = new ReturnPojo();
        returnPojo.setCode(500);
        returnPojo.setMessage("failed");

        return returnPojo;
    }

    public static String[] deleteSubString(String str1,String str2) {
        StringBuffer sb = new StringBuffer(str1);
        int delCount = 0;
        String[] obj = new String[2];

        while (true) {
            int index = sb.indexOf(str2);
            if(index == -1) {
                break;
            }
            sb.delete(index, index+str2.length());
            delCount++;

        }
        if(delCount!=0) {
            obj[0] = sb.toString();
            obj[1] = delCount+"";
        }else {
            //不存在返回-1
            obj[0] = sb.toString();
            obj[1] = 0+"";
        }

        return obj;
    }

}
