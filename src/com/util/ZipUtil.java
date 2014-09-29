package com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;

import java.io.File;

/**
 * Created by pengsheng on 2014/09/29.
 */
public class ZipUtil {

    private static final Log logger = LogFactory.getLog(ZipUtil.class);

    /**
     * 解压缩
     * @param sourceZip 源zip文件
     * @param targetDir 生成的目标目录下
     */
    public static void unZip(String sourceZip, String targetDir) {
        unZip(sourceZip, targetDir, "", "");
    }

    /**
     * 解压缩
     * @param sourceZip 源zip文件
     * @param targetDir 生成的目标目录下
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
}
