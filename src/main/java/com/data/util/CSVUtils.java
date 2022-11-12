package com.data.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * The type Csv utils.
 * open csv 官网地址：http://opencsv.sourceforge.net/index.html#quick_start
 * 2020-11-30
 */
public class CSVUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CSVUtils.class);

    /*
    lunix   下换行符只有:   \n

    Mac 下换行符有: \r

    windows 下换行方式: \r\n
    */

    private static final String DELIMITER = ",";

    private static String LINE_SEPARATOR;

    static {
        // LINE_SEPARATOR = System.getProperty("line.separator");
        LINE_SEPARATOR = StringUtils.isEmpty(System.lineSeparator()) ? "\r\n" : System.lineSeparator();
    }

    /**
     * 功能说明：获取UTF-8编码文本文件开头的BOM签名。
     * BOM(Byte Order Mark)，是UTF编码方案里用于标识编码的标准标记。例：接收者收到以EF BB BF开头的字节流，就知道是UTF-8编码。
     * @return UTF-8编码文本文件开头的BOM签名
     */
    public static String getBOM() {
        byte by[] = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        return new String(by);
    }

    /**
     * Write csv 2 file.
     * @param fileName   the file name
     * @param headerData the header data
     * @param bodyData   the body data
     */
    public static void writeCSV2File(final String fileName, final String[] headerData, final List<String[]> bodyData) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), Charset.forName("gbk")), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            writer.writeNext(headerData);
            writer.writeAll(bodyData);
            writer.flush();
        } catch (Exception e) {
            LOG.error("将数据写入CSV出错", e);
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LOG.error("关闭文件输出流出错", e);
                }
            }

        }
    }

    /**
     * Write csv 2 stream.
     * @param response   the response
     * @param fileName   the file name
     * @param headerData the header data
     * @param bodyData   the body data
     * @throws IOException the io exception
     */
    public static void writeCSV2Stream(final HttpServletResponse response, final String fileName, final String[] headerData, final List<String[]> bodyData) throws IOException {
        // tempFile = File.createTempFile("tempBlack", ".csv");
        File tempFile = new File("tempBlack.csv");
        LOG.info(">>>>temp file path:{}", tempFile.getCanonicalPath());
        writeCsvTempFile(headerData, bodyData, tempFile);
        LOG.info(">>>>write temp csv end,file size:{}B", tempFile.length());
        outPutCsvStream(response, fileName, tempFile);
    }

    private static void outPutCsvStream(HttpServletResponse response, String fileName, File tempFile) {
        try (FileInputStream fis = new FileInputStream(tempFile);
             BufferedInputStream bis = new BufferedInputStream(fis);
             OutputStream out = response.getOutputStream()) {
            response.reset();
            response.setContentType("application/csv");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
           /* int byteCount = 0;
            byte[] buffer = new byte[4096];
            int bytesRead;
            for (boolean var4 = true; (bytesRead = bis.read(buffer)) != -1; byteCount += bytesRead) {
                out.write(buffer, 0, bytesRead);
            }*/
            // 等价操作
            /*byte[] buffer = new byte[4096];
            int i = bis.read(buffer);
            while (i != -1) {
                out.write(buffer, 0, i);
                i = bis.read(buffer);
            }*/
            IOUtils.copy(bis, out);
            out.flush();
        } catch (Exception e) {
            LOG.error("write csv file stream fail", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                LOG.info("删除临时文件:{}", FileUtils.deleteQuietly(tempFile));
            }
        }
    }

    private static void writeCsvTempFile(String[] headerData, List<String[]> bodyData, File tempFile) {
        try (
                FileOutputStream fos = new FileOutputStream(tempFile);
                Writer writer = new OutputStreamWriter(fos, Charset.forName("gbk"));
                CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            csvWriter.writeNext(headerData);
            csvWriter.writeAll(bodyData);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read csv list.
     * @param tempFile the temp file
     * @return the list
     */
    public static List<String[]> readCSV(final File tempFile) {
        List<String[]> list = null;
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(tempFile), Charset.forName("gbk"))).build()) {
            list = reader.readAll();
        } catch (Exception e) {
            LOG.error("read csv file error", e);
        }
        return list;
    }


    /**
     * 写CSV并转换为字节流,可不经过中间文件，但生成文件内存占用较大,容易OOM
     * @param tableHeaderArr 表头
     * @param dataList       数据
     * @return
     */
    public static void exportCSVStream(final HttpServletResponse response, final String fileName, String[] tableHeaderArr, List<List<String>> dataList) throws IOException {
        File tempFile = FileUtils.getFile("tempBlack.csv");
        LOG.info(">>>>temp file path:{}", tempFile.getCanonicalPath());
        // excel文件需要通过文件头的bom来识别编码，而CSV文件格式不自带bom,所以写文件时，需要先写入bom头，否则excel打开乱码
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            bufferedWriter.write(new String(ByteOrderMark.UTF_8.getBytes()));
            //写表头
            StringBuffer sbu = new StringBuffer();
            String tableHeader = String.join(DELIMITER, tableHeaderArr);
            // sbu.append(tableHeader + StringUtils.CR + StringUtils.LF);
            LOG.info(">>>>system line separator:{}", LINE_SEPARATOR);
            sbu.append(tableHeader).append(LINE_SEPARATOR);
            for (List<String> dataRow : dataList) {
                for (int i = 0, len = dataRow.size(); i < len; i++) {
                    if (i == len - 1) {
                        sbu.append(dataRow.get(i)).append(LINE_SEPARATOR);
                    } else {
                        sbu.append(dataRow.get(i)).append(DELIMITER);
                    }
                }
            }
            bufferedWriter.write(sbu.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        LOG.info(">>>>write temp csv end,file size:{}B", tempFile.length());
        try (OutputStream out = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(new FileInputStream(tempFile))) {
            IOUtils.copy(bis, out);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
        } catch (IOException ioe) {
            LOG.error("exportCSVStream IOException", ioe);
        } catch (Exception ex) {
            LOG.error("exportCSVStream Exception", ex);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                LOG.info("删除临时文件:{}", FileUtils.deleteQuietly(tempFile));
            }
        }
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
       /* List<String[]> dataList = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            dataList.add(new String[]{"15018268060" + i, "20201130新增"});
        }
        CSVUtils.writeCSV2File("d:\\20201130黑名单.csv", new String[]{"号码", "备注"}, dataList);*/
        System.out.println(GsonUtil.toJson(CSVUtils.readCSV(new File("d:\\黑名单2030.csv"))));
    }
}
