package per.zdy.socketexchangeclientcp.share;

import cn.hutool.core.date.DateUtil;
import per.zdy.socketexchangeclientcp.domain.Pojo.ReturnPojo;

public class PublicVariable {

    //远程服务器地址
    public static String serverAddress;
    //远程服务器端口
    public static int serverPort;
    //通道数
    public static int passCount;

    //本地监听服务启动状态（true启动，false关闭）
    public static Boolean serverState = false;

    public static Boolean socketServerOnline = false;

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
}
