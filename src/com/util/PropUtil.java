package com.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Created by pengsheng on 2014/06/12.
 */
public class PropUtil {

    private static Logger logger = LogManager.getLogger(PropUtil.class);

    /***
     * @param propFilePath : 配置文件路径
     * @return
     */
    public static Properties getProperty(String propFilePath) {

        Properties properties = new Properties();
        InputStreamReader reader = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(propFilePath);
        if (input != null) {

            try {
                reader = new InputStreamReader(input, "UTF-8");
                properties.load(reader);

            } catch (UnsupportedEncodingException e) {
                logger.error(e);
            } catch (IOException e) {
                logger.error(e);
            } finally {

                try {

                    if (reader != null) {
                        reader.close();
                    }

                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }

        return properties;
    }

    /**
     *  获取配置项信息
     * @param key : 关键字
     * @param propFileDir : 配置文件路径
     * @return
     */
    public static String getProperty(String key, String propFileDir) {

        InputStream stream = null;

        try {
            File tempPathFile = new File(propFileDir);
            Properties pro = new Properties();
            if (tempPathFile == null || !tempPathFile.exists()) {
                return null;
            } else {

                stream = new FileInputStream(tempPathFile);
                pro.load(stream);
                stream.close();
                return pro.getProperty(key);
            }
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }

        return null;
    }

    /**
     *  保存配置项信息
     * @param key : 关键字
     * @param value ： 值
     * @param propFileDir : 配置文件路径
     */
    private void saveProperty(String key, String value, String propFileDir) {

        Properties tempPathProp = new Properties();
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {

            File tempPathFile = new File(propFileDir);
            if (!tempPathFile.exists())
                tempPathFile.createNewFile();

            if (tempPathFile != null && tempPathFile.exists()) {
                inputStream = new FileInputStream(tempPathFile);
                tempPathProp.load(inputStream);
            }

            tempPathProp.setProperty(key, value);
            outputStream = new FileOutputStream(tempPathFile);
            tempPathProp.store(outputStream, "");

        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

}
