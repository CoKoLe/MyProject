package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * 64进制编码解码
 * Created by pengsheng on 2014/09/28.
 */
public class Base64Util {

    private static final Log logger = LogFactory.getLog(Base64Util.class);

    private static final char[] STANDARD_ENCODE_TABLE = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] STANDARD_DECODE_TABLE = new byte[''];

    static {

        for (int i = 0; i < STANDARD_DECODE_TABLE.length; i++) {
            STANDARD_DECODE_TABLE[i] = 127;
        }

        for (int i = 0; i < STANDARD_ENCODE_TABLE.length; i++) {
            STANDARD_DECODE_TABLE[STANDARD_ENCODE_TABLE[i]] = ((byte) i);
        }
    }

    private static int decode0(char[] ibuf, byte[] obuf, int wp) {
        int outlen = 3;

        if (ibuf[3] == '=') {
            outlen = 2;
        }
        if (ibuf[2] == '=') {
            outlen = 1;
        }

        int b0 = STANDARD_DECODE_TABLE[ibuf[0]];
        int b1 = STANDARD_DECODE_TABLE[ibuf[1]];
        int b2 = STANDARD_DECODE_TABLE[ibuf[2]];
        int b3 = STANDARD_DECODE_TABLE[ibuf[3]];

        switch (outlen) {

            case 1:
                obuf[wp] = ((byte) (b0 << 2 & 0xFC | b1 >> 4 & 0x3));
                return 1;
            case 2:
                obuf[(wp++)] = ((byte) (b0 << 2 & 0xFC | b1 >> 4 & 0x3));
                obuf[wp] = ((byte) (b1 << 4 & 0xF0 | b2 >> 2 & 0xF));
                return 2;
            case 3:
                obuf[(wp++)] = ((byte) (b0 << 2 & 0xFC | b1 >> 4 & 0x3));
                obuf[(wp++)] = ((byte) (b1 << 4 & 0xF0 | b2 >> 2 & 0xF));
                obuf[wp] = ((byte) (b2 << 6 & 0xC0 | b3 & 0x3F));
                return 3;
        }

        throw new RuntimeException("Base64 Internal Error");
    }

    public static byte[] decode(char[] data, int off, int len) {

        char[] ibuf = new char[4];
        int ibufcount = 0;
        byte[] obuf = new byte[len / 4 * 3 + 3];
        int obufcount = 0;

        for (int i = off; i < off + len; i++) {
            char ch = data[i];
            if ((ch == '=') || ((ch < STANDARD_DECODE_TABLE.length) && (STANDARD_DECODE_TABLE[ch] != 127))) {
                ibuf[(ibufcount++)] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    obufcount += decode0(ibuf, obuf, obufcount);
                }
            }
        }

        if (obufcount == obuf.length) {
            return obuf;
        }

        byte[] ret = new byte[obufcount];
        System.arraycopy(obuf, 0, ret, 0, obufcount);
        return ret;
    }

    public static byte[] decode(String data) {

        char[] ibuf = new char[4];
        int ibufcount = 0;
        byte[] obuf = new byte[data.length() / 4 * 3 + 3];
        int obufcount = 0;

        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            if ((ch == '=') || ((ch < STANDARD_DECODE_TABLE.length) && (STANDARD_DECODE_TABLE[ch] != 127))) {
                ibuf[(ibufcount++)] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    obufcount += decode0(ibuf, obuf, obufcount);
                }
            }
        }

        if (obufcount == obuf.length) {
            return obuf;
        }

        byte[] ret = new byte[obufcount];
        System.arraycopy(obuf, 0, ret, 0, obufcount);
        return ret;
    }

    public static void decode(char[] data, int off, int len, OutputStream ostream) throws IOException {
        char[] ibuf = new char[4];
        int ibufcount = 0;
        byte[] obuf = new byte[3];

        for (int i = off; i < off + len; i++) {
            char ch = data[i];
            if ((ch == '=') || ((ch < STANDARD_DECODE_TABLE.length) && (STANDARD_DECODE_TABLE[ch] != 127))) {
                ibuf[(ibufcount++)] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    int obufcount = decode0(ibuf, obuf, 0);
                    ostream.write(obuf, 0, obufcount);
                }
            }
        }
    }

    public static void decode(String data, OutputStream ostream) throws IOException {
        char[] ibuf = new char[4];
        int ibufcount = 0;
        byte[] obuf = new byte[3];

        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            if ((ch == '=') || ((ch < STANDARD_DECODE_TABLE.length) && (STANDARD_DECODE_TABLE[ch] != 127))) {
                ibuf[(ibufcount++)] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    int obufcount = decode0(ibuf, obuf, 0);
                    ostream.write(obuf, 0, obufcount);
                }
            }
        }
    }

    public static String encode(byte[] data) {
        return encode(data, 0, data.length);
    }

    public static String encode(byte[] data, int off, int len) {
        if (len <= 0) {
            return "";
        }
        char[] out = new char[len / 3 * 4 + 4];
        int rindex = off;
        int windex = 0;
        int rest = len - off;

        while (rest >= 3) {
            int i = ((data[rindex] & 0xFF) << 16) + ((data[(rindex + 1)] & 0xFF) << 8) + (data[(rindex + 2)] & 0xFF);
            out[(windex++)] = STANDARD_ENCODE_TABLE[(i >> 18)];
            out[(windex++)] = STANDARD_ENCODE_TABLE[(i >> 12 & 0x3F)];
            out[(windex++)] = STANDARD_ENCODE_TABLE[(i >> 6 & 0x3F)];
            out[(windex++)] = STANDARD_ENCODE_TABLE[(i & 0x3F)];
            rindex += 3;
            rest -= 3;
        }

        if (rest == 1) {
            int i = data[rindex] & 0xFF;
            out[(windex++)] = STANDARD_ENCODE_TABLE[(i >> 2)];
            out[(windex++)] = STANDARD_ENCODE_TABLE[(i << 4 & 0x3F)];
            out[(windex++)] = '=';
            out[(windex++)] = '=';
        } else if (rest == 2) {
            int i = ((data[rindex] & 0xFF) << 8) + (data[(rindex + 1)] & 0xFF);
            out[(windex++)] = STANDARD_ENCODE_TABLE[(i >> 10)];
            out[(windex++)] = STANDARD_ENCODE_TABLE[(i >> 4 & 0x3F)];
            out[(windex++)] = STANDARD_ENCODE_TABLE[(i << 2 & 0x3F)];
            out[(windex++)] = '=';
        }

        return new String(out, 0, windex);
    }

    public static void encode(byte[] data, int off, int len, OutputStream ostream) throws IOException {
        if (len <= 0) {
            return;
        }

        byte[] out = new byte[4];
        int rindex = off;
        int rest = len - off;
        while (rest >= 3) {
            int i = ((data[rindex] & 0xFF) << 16) + ((data[(rindex + 1)] & 0xFF) << 8) + (data[(rindex + 2)] & 0xFF);
            out[0] = ((byte) STANDARD_ENCODE_TABLE[(i >> 18)]);
            out[1] = ((byte) STANDARD_ENCODE_TABLE[(i >> 12 & 0x3F)]);
            out[2] = ((byte) STANDARD_ENCODE_TABLE[(i >> 6 & 0x3F)]);
            out[3] = ((byte) STANDARD_ENCODE_TABLE[(i & 0x3F)]);
            ostream.write(out, 0, 4);
            rindex += 3;
            rest -= 3;
        }

        if (rest == 1) {
            int i = data[rindex] & 0xFF;
            out[0] = ((byte) STANDARD_ENCODE_TABLE[(i >> 2)]);
            out[1] = ((byte) STANDARD_ENCODE_TABLE[(i << 4 & 0x3F)]);
            out[2] = 61;
            out[3] = 61;
            ostream.write(out, 0, 4);
        } else if (rest == 2) {
            int i = ((data[rindex] & 0xFF) << 8) + (data[(rindex + 1)] & 0xFF);
            out[0] = ((byte) STANDARD_ENCODE_TABLE[(i >> 10)]);
            out[1] = ((byte) STANDARD_ENCODE_TABLE[(i >> 4 & 0x3F)]);
            out[2] = ((byte) STANDARD_ENCODE_TABLE[(i << 2 & 0x3F)]);
            out[3] = 61;
            ostream.write(out, 0, 4);
        }
    }

    public static void encode(byte[] data, int off, int len, Writer writer) throws IOException {
        if (len <= 0) {
            return;
        }
        char[] out = new char[4];
        int rindex = off;
        int rest = len - off;
        int output = 0;

        while (rest >= 3) {
            int i = ((data[rindex] & 0xFF) << 16) + ((data[(rindex + 1)] & 0xFF) << 8) + (data[(rindex + 2)] & 0xFF);
            out[0] = STANDARD_ENCODE_TABLE[(i >> 18)];
            out[1] = STANDARD_ENCODE_TABLE[(i >> 12 & 0x3F)];
            out[2] = STANDARD_ENCODE_TABLE[(i >> 6 & 0x3F)];
            out[3] = STANDARD_ENCODE_TABLE[(i & 0x3F)];
            writer.write(out, 0, 4);
            rindex += 3;
            rest -= 3;
            output += 4;
            if (output % 76 == 0) {
                writer.write("\n");
            }
        }

        if (rest == 1) {
            int i = data[rindex] & 0xFF;
            out[0] = STANDARD_ENCODE_TABLE[(i >> 2)];
            out[1] = STANDARD_ENCODE_TABLE[(i << 4 & 0x3F)];
            out[2] = '=';
            out[3] = '=';
            writer.write(out, 0, 4);
        } else if (rest == 2) {
            int i = ((data[rindex] & 0xFF) << 8) + (data[(rindex + 1)] & 0xFF);
            out[0] = STANDARD_ENCODE_TABLE[(i >> 10)];
            out[1] = STANDARD_ENCODE_TABLE[(i >> 4 & 0x3F)];
            out[2] = STANDARD_ENCODE_TABLE[(i << 2 & 0x3F)];
            out[3] = '=';
            writer.write(out, 0, 4);
        }
    }
}
