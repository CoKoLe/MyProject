package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 36进制编码解码
 * Created by pengsheng on 2014/09/28.
 */
public class Base36Util {

    private static Log logger = LogFactory.getLog(Base36Util.class);

    private static String[] base36Digit = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    /**
     * base36解码
     * @param str
     * @return
     */
    public static String base36ToStr(String str) {

        String returnStr = "";
        byte[] hexByte = str.getBytes();

        for (int i = 0; i < hexByte.length; i += 3) {
            int hexValue = base36toInt(hexByte[i]) * 36 * 36 + base36toInt(hexByte[(i + 1)]) * 36 + base36toInt(hexByte[(i + 2)]);
            returnStr = returnStr + (char) hexValue;
        }

        return returnStr;
    }

    /**
     *  base36编码
     * @param str
     * @return
     */
    public static String strToBase36(String str) {

        String returnStr = "";
        if (StringUtil.isNullOrEmpty(str)) {
            return returnStr;
        }

        for (int i = 0; i < str.length(); i++) {
            int n = str.codePointAt(i);
            returnStr = returnStr + pad(intToBase36(n), 3, "0");
        }

        return returnStr;
    }

    protected static int base36toInt(byte abyte) {
        if ((abyte >= 48) && (abyte <= 57)) {
            return abyte - 48;
        }
        if ((abyte >= 97) && (abyte <= 122)) {
            return abyte - 97 + 10;
        }
        if ((abyte >= 65) && (abyte <= 70)) {
            return abyte - 65 + 10;
        }
        return -1;
    }

    protected static String intToBase36(int n) {
        String result = "";
        while (n >= 36) {
            result = base36Digit[(n % 36)] + result;
            n /= 36;
        }
        result = base36Digit[n] + result;
        return result;
    }

    private static String pad(String str, int len, String p) {
        String result = str;
        for (int i = str.length(); i < len; i++) {
            result = p + result;
        }
        return result;
    }

}
