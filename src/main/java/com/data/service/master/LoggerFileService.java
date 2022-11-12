package com.data.service.master;

import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Logger file service.
 */
@Service
public class LoggerFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFileService.class);

    /*对应日志目录-F:\\logs*/
    @Value("${logging.file.path:/logs}")
    private String logFilePath;

    @Value("${logging.file.absPath:F:\\ideaWorkplace\\DataCenter\\logs}")
    private String logFileAbsPath;

    @Value("${log.read.limit:3}")
    private Integer limitLine;

    /**
     * Query log content result vo.
     * @param word the word
     * @return the result vo
     */
    public String queryLogContent(String word) {
        /*FileUrlResource fileUrlResource = new FileUrlResource(logFilePath);
        OutputStream ops = fileUrlResource.getOutputStream();*/
        // File file = new File(logFilePath);
        File file = new File(logFilePath);
        if (file == null || !file.exists()) {
            return "";
        }
        File[] allLogFile = FileUtil.listFiles(file, (pathName) -> pathName.getName().endsWith(".log"));
        final Pattern pattern = Pattern.compile(word);
        StringBuilder stringBuilder = new StringBuilder();
        boolean shouldBreak = false;
        int hasReadLine = 0;
        for (File tempFile : allLogFile) {
            if (tempFile == null || !tempFile.isFile()) {
                continue;
            }
            if (shouldBreak) {
                break;
            }
            try (FileInputStream fis = new FileInputStream(tempFile);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 InputStreamReader ir = new InputStreamReader(bis, StandardCharsets.UTF_8);
                 BufferedReader br = new BufferedReader(ir)) {
                String line = "";
                while ((line = br.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        if (hasReadLine >= limitLine) {
                            shouldBreak = true;
                            break;
                        }
                        stringBuilder.append(line).append("\r\n");
                        hasReadLine += 1;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
