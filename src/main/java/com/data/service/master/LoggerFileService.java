package com.data.service.master;

import com.data.model.ResultVO;
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

    @Value("${logging.file.path:/logs}")
    private String logFilePath;

    @Value("${logging.file.absPath:F:\\ideaWorkplace\\DataCenter\\logs}")
    private String logFileAbsPath;

    /**
     * Query log content result vo.
     * @param word the word
     * @return the result vo
     */
    public String queryLogContent(String word) {
        /*FileUrlResource fileUrlResource = new FileUrlResource(logFilePath);
        OutputStream ops = fileUrlResource.getOutputStream();*/
        // File file = new File(logFilePath);
        File file = new File(logFileAbsPath);
        if (file == null || !file.exists()) {
            return "";
        }
        File[] allLogFile = FileUtil.listFiles(file, (pathName) -> pathName.getName().endsWith(".log"));
        final Pattern pattern = Pattern.compile(word);
        StringBuilder stringBuilder = new StringBuilder();
        for (File tempFile : allLogFile) {
            if (tempFile == null || !tempFile.isFile()) {
                continue;
            }
            try (FileInputStream fis = new FileInputStream(tempFile);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 InputStreamReader ir = new InputStreamReader(bis, StandardCharsets.UTF_8);
                 BufferedReader br = new BufferedReader(ir)) {
                String line = "";
                while ((line = br.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        stringBuilder.append(line).append("\r\n");
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
