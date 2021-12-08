package com.nd.util;

import java.security.MessageDigest;

/**
 * MD5加密
 *
 * @author cwj
 */
public class MD5Util {

    public static String encoderByMd5(String s, String charsetName) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] strTemp = s.getBytes(charsetName);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 默认的编码是"gbk",加密结果与C#一致
     *
     * @param s
     * @return 结果
     */
    public static String encoderByMd5(String s) {
        return encoderByMd5(s, "utf-8");
    }

}
