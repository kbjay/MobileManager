package com.mobilemanager.wzq.mobilemanager.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/24.
 */
public class HttpUtils {
    public static String getStringFromStream(InputStream is){
        String re="";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte [] b= new byte[1024];
        int len=0;
        try {
            while((len=is.read(b))!=-1){
                baos.write(b,0,len);
            }

            baos.close();
            re=baos.toString("GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re;

    }
}
