package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ZipScanner;

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

    /**
     *  删除文件或文件夹
     * @param fileDir : 文件路径
     */
    public static void deleteFile(String fileDir) {

        File file = new File(fileDir);

        Project project = new Project();
        Delete delete = new Delete();

        delete.setProject(project);
        delete.setDir(file);
        delete.execute();
    }

    /**
     *  拷贝文件或文件夹
     * @param srcFileDir : 源文件路径
     * @param destFileDir :目标文件路径
     */
    public static void copyFile(String srcFileDir, String destFileDir) {
        File srcFile = new File(srcFileDir);
        File destFile = new File(destFileDir);

        Project project = new Project();
        Copy copy = new Copy();

        copy.setProject(project);
        copy.setFile(srcFile);
        copy.setTofile(destFile);

        copy.execute();
    }

    /**
     *  拷贝文件夹并根据条件过滤文件
     * @param srcFileDir : 源文件路径
     * @param destFileDir :目标文件路径
     * @param includePattern : 包含pattern设置过滤的文件
     * @param excludePattern : 不包含pattern设置过滤的文件
     * pattern是一个使用“,”或空格分隔的匹配字符串，其中 “**”代表所有文件或目录，“*.*”代表说有文件， “*.java”代表所有扩展名为java的文件
     */
    public static void copyFolder(String srcFileDir, String destFileDir, String includePattern, String excludePattern) {

        File srcFile = new File(srcFileDir);
        File destFile = new File(destFileDir);

        Project project = new Project();
        Copy copy = new Copy();

        copy.setProject(project);
        copy.setTodir(destFile);

        FileSet fileSet = new FileSet();
        fileSet.setDir(srcFile);
        if (!StringUtil.isNullOrEmpty(includePattern))
            fileSet.setIncludes(includePattern);

        if (!StringUtil.isNullOrEmpty(excludePattern))
            fileSet.setExcludes(excludePattern);

        copy.addFileset(fileSet);

        copy.execute();
    }

    /**
     * 在指定路径中查找匹配的文件
     * @param directory : 文件夹路径
     * @param includePattern : 包含pattern匹配的文件
     * @param excludePattern : 不包含pattern匹配的文件
     *                       其中 {“**”}代表所有文件或目录，{“*.*”}代表说有文件， {“**\*.java”}代表所有扩展名为java的文件
     * @return
     */
    public static String[] findFileInDir(String directory, String[] includePattern, String[] excludePattern) {

        DirectoryScanner dscanner = new DirectoryScanner();
        dscanner.setBasedir(directory);
        dscanner.setIncludes(includePattern);
        dscanner.setExcludes(excludePattern);
        dscanner.scan();
        return dscanner.getDeselectedFiles();
    }

    /**
     * 在Zip文件中查找匹配的文件
     * @param zipFileDir : zip文件路径
     * @param includePattern : 包含pattern匹配的文件
     * @param excludePattern : 不包含pattern匹配的文件
     *                       其中 {“**”}代表所有文件或目录，{“*.*”}代表说有文件， {“*.java”}代表所有扩展名为java的文件
     * @return
     */
    public static String[] findFileInZip(String zipFileDir, String[] includePattern, String[] excludePattern) {

        ZipScanner zipscanner = new ZipScanner();
        zipscanner.setSrc(new File(zipFileDir));
        zipscanner.setIncludes(includePattern);
        zipscanner.setExcludes(excludePattern);
        zipscanner.scan();
        return zipscanner.getDeselectedFiles();
    }
}
