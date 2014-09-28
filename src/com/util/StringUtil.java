package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengsheng on 2014/09/28.
 */
public class StringUtil {

    private static final Log logger = LogFactory.getLog(StringUtil.class);

    public static boolean isNullOrEmpty (Object object) {

        if (object instanceof String) {

            if (object == null || "".equals(object)) {
                return true;
            }
        } else if (object instanceof List) {

            if (object == null ||  ((List) object).size() <= 0) {
                return true;
            }
        } else if (object instanceof Map) {

            if (object == null || ((Map) object).isEmpty()) {
                return true;
            }
        } else if (object instanceof HashMap) {

            if (object == null || ((HashMap) object).isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
