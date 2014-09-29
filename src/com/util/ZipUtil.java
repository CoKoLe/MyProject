package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;

/**
 * Created by pengsheng on 2014/09/29.
 */
public class ZipUtil {

    private static final Log logger = LogFactory.getLog(ZipUtil.class);
    private static final int BUFFER = 1024;
    /**
     * 解压缩
     * @param sourceZip ： 源zip文件
     * @param targetDir ：目标文件夹
     */
    public static void unZip(String sourceZip, String targetDir) {
        unZip(sourceZip, targetDir, "", "");
    }

    /**
     * 解压缩
     * @param sourceZip ：源zip文件
     * @param targetDir ：目标文件夹
     * @param includePattern : 只解压缩包含的文件或文件夹,
     * @param excludePattern : 不解压缩包含的文件或文件夹
     * pattern是一个使用“,”或空格分隔的匹配字符串，其中 “**”代表所有文件或目录，“*.*”代表说有文件， “*.java”代表所有扩展名为java的文件
     */
    public static void unZip(String sourceZip, String targetDir, String includePattern, String excludePattern) {

        Project proejct = new Project();
        Expand expand = new Expand();

        expand.setEncoding("GBK");
        expand.setProject(proejct);
        expand.setSrc(new File(sourceZip));
        expand.setOverwrite(false);//是否覆盖

        File file = new File(targetDir);
        expand.setDest(file);

        PatternSet patternset = new PatternSet();
        patternset.setProject(proejct);
        patternset.setIncludes(includePattern);
        patternset.setExcludes(excludePattern);

        expand.addPatternset(patternset);

        expand.execute();
    }

    /**
     * 压缩不带目录
     * @param sourceFile 压缩的源文件夹
     * @param targetZip 生成的目标文件
     */
    public static void zip(String sourceFile, String targetZip) {
        zip(sourceFile, targetZip, "", "");
    }

    /**
     * 压缩不带目录,且过滤匹配条件的文件
     * @param sourceFile 压缩的源文件夹
     * @param targetZip 生成的目标文件
     * @param includePattern : 只压缩包含的文件或文件夹,
     * @param excludePattern : 不压缩包含的文件或文件夹
     * pattern是一个使用“,”或空格分隔的匹配字符串，其中 “**”代表所有文件或目录，“*.*”代表说有文件， “*.java”代表所有扩展名为java的文件
     */
    public static void zip(String sourceFile, String targetZip, String includePattern, String excludePattern) {

        Project project = new Project();
        Zip zip = new Zip();

        zip.setEncoding("GBK");
        zip.setProject(project);

        zip.setDestFile(new File(targetZip));//设置生成的目标zip文件File对象

        FileSet fileSet = new FileSet();
        fileSet.setProject(project);
        fileSet.setDir(new File(sourceFile));//设置将要进行压缩的源文件File对象

        fileSet.setIncludes(includePattern);
        fileSet.setExcludes(excludePattern);

        zip.addFileset(fileSet);
        zip.execute();
    }

    /**
     *  压缩文件包含路径
     * @param sourceFile : 压缩的源文件夹
     * @param targetZip : 生成的目标文件
     * @throws Exception
     */
    public static void ziphasDir(String sourceFile, String targetZip) throws Exception {

        File inputFile = new File(sourceFile);
        File outputFile = new File(targetZip);

        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
        out.setEncoding("GBK");
        zip(out, inputFile, "");
        out.close();
    }

    private static void zip(ZipOutputStream out, File file, String base) throws Exception {

        if (file.isDirectory()) {

            File[] fileList = file.listFiles();
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base + "/"));
            }

            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fileList.length; i++) {
                zip(out, fileList[i], base + fileList[i].getName());
            }

        } else {

            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(file);
            int len;
            byte[] buf = new byte[BUFFER];
            while ((len = in.read(buf, 0, BUFFER)) != -1) {
                out.write(buf, 0, len);
            }
            in.close();
            out.closeEntry();
        }
    }

    /**
     * 解压缩zip文件包含文件夹
     * @param zipFileName : 源zip文件
     * @param targetDir : 目标文件夹
     * @throws Exception
     */
    public static void unZiphasDir(String zipFileName, String targetDir) throws Exception {

        ZipFile zipFile = new ZipFile(zipFileName, "GBK");
        Enumeration emu = zipFile.getEntries();
        while (emu.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) emu.nextElement();
            if (entry.isDirectory()) {
                File dir = new File(targetDir + entry.getName());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                continue;
            }

            BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
            File file = new File(targetDir + entry.getName());
            File parent = file.getParentFile();
            if (parent != null && (!parent.exists())) {
                parent.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
            byte[] buf = new byte[BUFFER];
            int len = 0;
            while ((len = bis.read(buf, 0, BUFFER)) != -1) {
                fos.write(buf, 0, len);
            }

            bos.flush();
            bos.close();
            bis.close();
        }

        zipFile.close();
    }
}
