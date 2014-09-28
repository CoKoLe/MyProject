package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Calendar;

/**
 * Created by pengsheng on 2014/09/26.
 */
public class FileUtil {

    private static final Log logger = LogFactory.getLog(FileUtil.class);

    private static String LS = File.separator;

    /**
     *  根据日期时间创建默认文件上传路径
     * @return
     */
    public static String createDefaultUploadDir() {

        String uploadDir = "";

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String _month = month < 10 ? "0" + month : month + "";

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        uploadDir = year + LS + _month + day + LS + hour + minute + LS + second + LS;
        return uploadDir;
    }
}
