package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by pengsheng on 2014/09/29.
 */
public class IpUtil {

    private static Log logger = LogFactory.getLog(IpUtil.class);

    /**
     *  获取客户端Ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        return ip;
    }

    /**
     *  获取本机的所有Ip (仅适用ipv4)
     * @return
     */
    public static List<String> getLocalIp() {

        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return getAllLocalIp_Linux();
        } else {
            return getAllLocalIp_window_mac();
        }
    }

    /**
     *  windows获取本机的所有Ip (仅适用ipv4)
     * @return
     */
    private static List<String> getAllLocalIp_window_mac() {

        List<String> list = new ArrayList<String>();

        try {
            String hostName = getLocalHostName();
            if (hostName.length() > 0) {
                InetAddress[] addrArray = InetAddress.getAllByName(hostName);
                if (addrArray.length > 0) {
                    for (int i = 0; i < addrArray.length; i++) {
                        if (addrArray[i] instanceof Inet4Address)
                            list.add(addrArray[i].getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }

        return list;
    }

    /**
     * 获取Linux机器Ip
     * @return
     */
    private static List<String> getAllLocalIp_Linux() {

        List<String> list = new ArrayList<String>();
        InetAddress inet = null;
        Enumeration allNetInterfaces = null;

        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();// 获取此机器上的所有接口
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    inet = (InetAddress) addresses.nextElement();
                    if (inet != null && inet instanceof Inet4Address) {
                        String ip = inet.getHostAddress();
                        list.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            logger.error(e);
        }

        return list;
    }

    /**
     * 获取本机域名
     */
    public static String getLocalHostName() {
        String hostName = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();
        } catch (Exception e) {
            logger.error(e);
        }
        return hostName;
    }
}
