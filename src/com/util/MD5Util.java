package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串Md5加密
 * Created by pengsheng on 2014/09/28.
 */
public class MD5Util {

    private static final Log logger = LogFactory.getLog(MD5Util.class);

    private static final String HEX = "0123456789ABCDEF";

    /**
     *  将字符串MD5编码
     * @param str
     * @return
     */
    public static String stringToMD5(String str) {

        StringBuffer buffer = new StringBuffer();
        MessageDigest messagedigest = null;

        if (StringUtil.isNullOrEmpty(str))
            return "";

        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            return "";
        }

        byte[] b = messagedigest.digest(str.getBytes());
        for (int i = 0; i < b.length; i++)
            buffer.append(byteToHexString(b[i]));

        return buffer.toString();
    }

    private static String byteToHexString(byte b) {

        int n = b;
        if (n < 0)
            n = n + 256;

        int index_1 = ((n & 0xf0) >> 4);//整除16
        int index_2 = ((n & 0x0f) >> 0);//取余16

        return String.valueOf(HEX.charAt(index_1) + HEX.charAt(index_2));
    }
}
