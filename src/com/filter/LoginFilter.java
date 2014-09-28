package com.filter;

import com.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pengsheng on 2014/09/28.
 */
public class LoginFilter implements Filter {

    private static final Log logger = LogFactory.getLog(LoginFilter.class);

    // 正则判断URL中是否包含和不包含
    private Pattern includeFilterPattern = null;
    private Pattern excludeFilterPattern = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String includeFilterURL = filterConfig.getInitParameter("include");
        if (includeFilterURL == null)
            return;
        includeFilterPattern = createPattern(includeFilterURL, "\\.", "|\\.");

        String excludeFilterURL = filterConfig.getInitParameter("exclude");
        if (excludeFilterURL == null)
            return;
        excludeFilterPattern = createPattern(excludeFilterURL, "/", "|/");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String pathInfo = request.getServletPath();
        if (StringUtil.isNullOrEmpty(pathInfo)) {
            new IllegalArgumentException("该用户已经登陆.");
        }

        if (includeFilterPattern != null) {

            Matcher matcher = includeFilterPattern.matcher(pathInfo.toLowerCase());
            boolean iscontains = matcher.find();
            if (iscontains) {

                matcher = excludeFilterPattern.matcher(pathInfo.toLowerCase());
                iscontains = matcher.find();
                if (iscontains) {
                    filterChain.doFilter(req, resp);
                    return;
                }

                HttpSession session = request.getSession();
                String sysAccessID = (String) session.getAttribute("sysAccessID");
                // 判断是否登陆
                if (StringUtil.isNullOrEmpty(sysAccessID)) {

                } else {

                }

            } else {

                filterChain.doFilter(req, resp);
            }
        }

    }

    /**
     *  构建正则规则
     * @param urlStr
     * @param sepStr1
     * @param sepStr2
     * @return
     */
    private Pattern createPattern(String urlStr, String sepStr1, String sepStr2) {

        if (urlStr == null || "".equals(urlStr))
            return null;

        String[] urlArray = urlStr.split(",");
        StringBuffer buffer = new StringBuffer(urlStr.length() + urlArray.length * 2);
        buffer.append(sepStr1).append(urlArray[0]);
        for (int i = 1; i < urlArray.length; i++) {
            buffer.append(sepStr2).append(urlArray[i]);
        }

        Pattern pattern = Pattern.compile(buffer.toString());
        return pattern;
    }

    /**
     * 获得真实IP地址
     * @param request
     * @return ip
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        return ip;
    }

    @Override
    public void destroy() {

    }
}
