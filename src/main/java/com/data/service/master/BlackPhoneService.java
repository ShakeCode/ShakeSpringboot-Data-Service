package com.data.service.master;

import com.data.model.ResultVO;
import com.data.service.BaseService;
import com.data.util.CSVUtils;
import com.data.util.DateUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Black phone service.
 * 2020-12-01
 * <p>
 * 100W导出 2列 号码和备注
 * <p>
 * 1、opencsv +中间文件缓存，无OOM风险，耗时： 6162ms, 导出文件大小：21.8 MB(大数据量导出优选方案)
 * <p>
 * 2、SXSSFWorkbook(含有磁盘缓存，无OOM风险),耗时：14391ms，导出文件大小： 7.04MB (文件内存占用最少，大数据量导出优选方案)
 * <p>
 * 3、原生IO导出csv，无中间文件缓存，有OOM风险，耗时：5146 ms, 导出文件大小：40.12 MB （文件内存占用最大,适合小数据量导出）
 */
@Service
public class BlackPhoneService extends BaseService {
    private static final Logger LOG = LoggerFactory.getLogger(BlackPhoneService.class);

    private static final String[] BLACK_FILE_HEADER = new String[]{"号码", "备注"};


    /**
     * Import black result vo.
     * @return the result vo
     */
    public ResultVO importBlack() {
        return null;
    }

    /**
     * Export open csv file.
     * @throws IOException the io exception
     */
    public void exportOpenCsvFile() throws IOException {
        LOG.info(">>>>exportOpenCsvFile...");
        long startTime = DateUtil.getNowTimeMills();
        List<String[]> dataList = new ArrayList<>();
        for (int i = 0; i < 100 * 10000; i++) {
            dataList.add(new String[]{"150" + i, "20201130新增"});
        }
        CSVUtils.writeCSV2Stream(response, "黑名单2030.csv", BLACK_FILE_HEADER, dataList);
        LOG.info(">>>exportOpenCsvFile waste time:{}ms", DateUtil.getNowTimeMills() - startTime);
    }


    /**
     * Export sxssf workbook file.
     * @throws IOException the io exception
     */
    public void exportSXSSFWorkbookFile() throws IOException {
        LOG.info(">>>>exportSXSSFWorkbookFile...");
        long startTime = DateUtil.getNowTimeMills();
        // 超过1000行缓存磁盘
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        SXSSFSheet sxssfSheet = workbook.createSheet("黑名单");
        // 设置并获取到需要的样式
        XSSFCellStyle xssfCellStyleHeader = getAndSetXSSFCellStyleHeader(workbook);
        XSSFCellStyle xssfCellStyleOne = getAndSetXSSFCellStyleOne(workbook);
        SXSSFRow row = sxssfSheet.createRow(0);
        for (int i = 0; i < BLACK_FILE_HEADER.length; i++) {
            SXSSFCell cell = row.createCell(i);
            cell.setCellValue(BLACK_FILE_HEADER[i]);
            cell.setCellStyle(xssfCellStyleHeader);
        }
        for (int i = 0; i < 100 * 10000; i++) {
            SXSSFRow sxssfRow = sxssfSheet.createRow(i + 1);
            for (int j = 0; j < BLACK_FILE_HEADER.length; j++) {
                SXSSFCell cell = sxssfRow.createCell(j);
                if (j == 0) {
                    cell.setCellValue("15" + j);
                }
                if (j == 1) {
                    cell.setCellValue("20201201新增");
                }
                cell.setCellStyle(xssfCellStyleOne);
            }
        }
        String fileName = "黑名单号码1201.xlsx";
        try (OutputStream out = response.getOutputStream()) {
            response.reset();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
        LOG.info(">>>exportSXSSFWorkbookFile waste time:{}ms", DateUtil.getNowTimeMills() - startTime);
    }

    /**
     * 获取并设置header样式
     */
    private XSSFCellStyle getAndSetXSSFCellStyleHeader(SXSSFWorkbook sxssfWorkbook) {
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) sxssfWorkbook.createCellStyle();
        Font font = sxssfWorkbook.createFont();
        // 字体大小
        font.setFontHeightInPoints((short) 14);
        // 字体粗细
        font.setBold(true);
        // 将字体应用到样式上面
        xssfCellStyle.setFont(font);
        // 是否自动换行
        xssfCellStyle.setWrapText(false);
        // 水平居中
        xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return xssfCellStyle;
    }

    /**
     * 获取并设置样式一
     */
    private XSSFCellStyle getAndSetXSSFCellStyleOne(SXSSFWorkbook sxssfWorkbook) {
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) sxssfWorkbook.createCellStyle();
        XSSFDataFormat format = (XSSFDataFormat) sxssfWorkbook.createDataFormat();
        // 是否自动换行
        xssfCellStyle.setWrapText(false);
        // 水平居中
        xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 前景颜色
        // xssfCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        xssfCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        // 边框
        xssfCellStyle.setBorderBottom(BorderStyle.THIN);
        xssfCellStyle.setBorderRight(BorderStyle.THIN);
        xssfCellStyle.setBorderTop(BorderStyle.THIN);
        xssfCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        xssfCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        xssfCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        xssfCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        // 防止数字过长,excel导出后,显示为科学计数法,如:防止8615192053888被显示为8.61519E+12
        xssfCellStyle.setDataFormat(format.getFormat("0"));
        return xssfCellStyle;
    }

    /**
     * 获取并设置样式二
     */
    private XSSFCellStyle getAndSetXSSFCellStyleTwo(SXSSFWorkbook sxssfWorkbook) {
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) sxssfWorkbook.createCellStyle();
        XSSFDataFormat format = (XSSFDataFormat) sxssfWorkbook.createDataFormat();
        // 是否自动换行
        xssfCellStyle.setWrapText(false);
        // 水平居中
        xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 边框
        xssfCellStyle.setBorderBottom(BorderStyle.THIN);
        xssfCellStyle.setBorderRight(BorderStyle.THIN);
        xssfCellStyle.setBorderTop(BorderStyle.THIN);
        xssfCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        xssfCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        xssfCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        xssfCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        // 垂直居中
        xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 防止数字过长,excel导出后,显示为科学计数法,如:防止8615192053888被显示为8.61519E+12
        xssfCellStyle.setDataFormat(format.getFormat("0"));
        return xssfCellStyle;
    }

    public void exportCsvStream() throws IOException {
        LOG.info(">>>>exportCsvStream...");
        long startTime = DateUtil.getNowTimeMills();
        List<List<String>> dataList = new ArrayList<>();
        for (int i = 0; i < 100 * 10000; i++) {
            List<String> row = new ArrayList<>(2);
            row.add("150" + i);
            row.add("20201201新增");
            dataList.add(row);
        }
        CSVUtils.exportCSVStream(response, "黑名单号码1201.csv", BLACK_FILE_HEADER, dataList);
        LOG.info(">>>exportCsvStream waste time:{}ms", DateUtil.getNowTimeMills() - startTime);
    }
}
