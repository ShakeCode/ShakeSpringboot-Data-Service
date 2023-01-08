/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.util;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩工具类
 */
public class ZipUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

    private static final int BUFFER_SIZE = 4 * 1024;

    /**
     * 压缩成ZIP 方法1
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构; false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException {
        StopWatch watch = new StopWatch();
        watch.start();
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            watch.stop();
            LOGGER.info("压缩完成，耗时: {}ms", watch.getTime());
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        }
    }


    /**
     * 压缩成ZIP 方法2
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
        StopWatch watch = new StopWatch();
        watch.start();
        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out), StandardCharsets.UTF_8)) {
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                try (FileInputStream in = new FileInputStream(srcFile);) {
                    while ((len = in.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                zos.closeEntry();
            }
            watch.stop();
            LOGGER.info("压缩完成,耗时:{}ms", watch.getTime());
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        }
    }


    /**
     * 递归压缩方法
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         <p>
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */

    private static void compress(File sourceFile, ZipOutputStream zos,
                                 String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            try (FileInputStream in = new FileInputStream(sourceFile);) {
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件,不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意: file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        // 不保留源结构, 直接复制在同一根目录下后压缩
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * 解压zip文件
     * @param sourceFile 待解压的zip文件
     * @param descDir    解压后的存放路径
     */
    public static void unZipFiles(String sourceFile, String descDir) {
        StopWatch watch = new StopWatch();
        watch.start();
        // 解压目录不存在,就创建
        File descDirFile = new File(descDir);
        if (!descDirFile.exists()) {
            descDirFile.mkdirs();
        }
        String orgMkdirs = "";
        // 创建zip压缩对象
        try (ZipFile zip = new ZipFile(sourceFile, Charset.forName(StandardCharsets.UTF_8.displayName()))) {
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                String outPath = (descDir + "/" + zipEntryName).replaceAll("\\*", "/");
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                orgMkdirs = outPath.substring(0, outPath.lastIndexOf('/'));
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                try (InputStream in = zip.getInputStream(entry);
                     OutputStream out = new FileOutputStream(outPath)) {
                    byte[] buf1 = new byte[BUFFER_SIZE];
                    int len;
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                } catch (IOException e) {
                    LOGGER.error("解压失败:{}", e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("最后解压路径:" + orgMkdirs);
        watch.stop();
        LOGGER.info("解压缩完成，耗时: {}ms", watch.getTime());
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     * @throws FileNotFoundException the file not found exception
     */
    public static void main(String[] args) throws FileNotFoundException {
        // 单极目录集合文件合并压缩
       /* ZipUtils.toZip(Stream.of(new File("D:\\log\\20221005").listFiles()).collect(Collectors.toList()), new BufferedOutputStream(new FileOutputStream(new File("D:\\test-log.zip"))));

        ZipUtils.toZip("D:\\log", new BufferedOutputStream(new FileOutputStream(new File("D:\\test-qiantao-log.zip"))), true);
*/
        ZipUtils.unZipFiles("D:\\浏览器导出zip文件 (1).zip", "D:\\test-unzip");
    }
}
