package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;

/**
 * 16进制编码解码
 * Created by pengsheng on 2014/09/28.
 */
public class Base16Util {

    private static final Log logger = LogFactory.getLog(Base16Util.class);

    private static final String HEX = "0123456789ABCDEF";

    /**
     * 将字符串编码成16进制数据,适用于所有字符（包括中文）
     */
    public static String strToBase16(String str) {

        if (StringUtil.isNullOrEmpty(str))
            return "";

        // 根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        // 将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(HEX.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(HEX.charAt((bytes[i] & 0x0f) >> 0));
        }

        return sb.toString();
    }

    /**
     * 将16进制数据解码成字符串,适用于所有字符（包括中文）
     */
    public static String base16ToStr(String bytes) {

        if (StringUtil.isNullOrEmpty(bytes))
            return "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((HEX.indexOf(bytes.charAt(i)) << 4 | HEX.indexOf(bytes.charAt(i + 1))));

        return new String(baos.toByteArray());
    }

}
