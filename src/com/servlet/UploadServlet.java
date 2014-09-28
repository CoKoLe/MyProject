package com.servlet;

import com.util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 上传文件
 * Created by pengsheng on 2014/09/26.
 */
public class UploadServlet extends HttpServlet{

    private static final Log logger = LogFactory.getLog(UploadServlet.class);

    private static final String UPLOAD_CACHE_PATH = "../uploadFileCache";// 上传文件缓冲目录

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        File cacheFile = new File(UPLOAD_CACHE_PATH);
        if (!cacheFile.exists()) {
            cacheFile.mkdir();
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(8192 * 1024); // 设置缓冲区大小8MB
        factory.setRepository(cacheFile);// 设置缓冲区目录
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        upload.setSizeMax(8192 * 1024 * 10);// 设置最大文件大小为40MB

        try {
            List<FileItem> fileItemList = upload.parseRequest(request);
            if (fileItemList != null) {

                for (FileItem item : fileItemList) {

                    if (item.isFormField()) {

                        String fileName = item.getName();
                        String uploadDir = FileUtil.createDefaultUploadDir();
                        if (!new File(uploadDir).exists())
                            new File(uploadDir).mkdirs();

                        File uploadFile = new File(uploadDir + fileName);
                        if (!uploadFile.exists())
                            uploadFile.createNewFile();

                        item.write(uploadFile);
                    }
                }
            }
        } catch (FileUploadException e) {
            logger.error(e);
            sendResponse("解析上传文件错误.", response);
        } catch (Exception e) {
            logger.error(e);
            sendResponse("上传文件错误.请联系管理员", response);
        }
    }

    private void sendResponse(String message, HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.getWriter().print("<script>alert('" + message + "')</script>");
    }
}
