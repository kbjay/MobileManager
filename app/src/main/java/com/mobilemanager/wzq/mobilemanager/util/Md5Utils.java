package com.mobilemanager.wzq.mobilemanager.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/3/26.
 */
public class Md5Utils {

    public static String getMd5(String str){
        String re="";
        StringBuffer sb= new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] digest = md5.digest(str.getBytes());
            for (byte b:digest) {
                int number=b&0xff;
                String string=Integer.toHexString(number);
                if(string.length()==0){
                    sb.append("0");

                }
                sb.append(string);
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        re=sb.toString();
        return re;

    }

}
