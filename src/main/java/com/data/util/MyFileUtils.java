package com.data.util;

import com.data.exception.ServiceException;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * The type File utils.
 */
@Data
public class MyFileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyFileUtils.class);

    private List<File> fileList;

    private String strPath;

    /**
     * Instantiates a new File utils.
     * @param fileList the fileList
     * @param strPath  the str path
     */
    public MyFileUtils(List<File> fileList, String strPath) {
        this.fileList = fileList;
        this.strPath = strPath;
    }

    /**
     * Get a ll file file [ ].
     * @param dir    the dir
     * @param suffix the suffix
     * @return the file [ ]
     */
    public static File[] getALlFile(File dir, String suffix) {
        if (dir == null || !dir.exists()) {
            return new File[]{};
        }
        if (dir.isDirectory()) {
            // FileFilter
            return dir.listFiles((pathname) -> pathname.getName().endsWith(suffix));
        }
        return new File[]{};
    }

    /**
     * Gets file list.
     * @param strPath the str path
     * @return the file list
     */
    public List<File> getFileList(String strPath) {
        LOGGER.info("getFileList src path:{}", strPath);
        File dir = new File(strPath);
        if (dir.exists()) {
            throw new ServiceException("file not exists");
        }
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (ArrayUtils.isEmpty(files)) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                // 判断是文件还是文件夹
                if (files[i].isDirectory()) {
                    // 获取文件绝对路径
                    getFileList(files[i].getAbsolutePath());
                } else {
                    // 判断文件名
                    String strFileName = files[i].getAbsolutePath();
                    LOGGER.info("not dir,srcFileName:{}", strFileName);
                    fileList.add(files[i]);
                }
            }
        }
        return fileList;
    }
}
