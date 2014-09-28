package com.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
}
