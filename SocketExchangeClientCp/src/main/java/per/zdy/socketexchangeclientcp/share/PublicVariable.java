package per.zdy.socketexchangeclientcp.share;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import per.zdy.socketexchangeclientcp.domain.Pojo.RequestInfoPojo;
import per.zdy.socketexchangeclientcp.domain.Pojo.ReturnPojo;

public class PublicVariable {
    //远程服务器地址
    public static String remoteAddress;
    //远程服务器端口
    public static int remotePort;

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

    public static ReturnPojo getJsonFailed(){
        ReturnPojo returnPojo = new ReturnPojo();
        returnPojo.setCode(500);
        returnPojo.setMessage("failed");

        return returnPojo;
    }
}
