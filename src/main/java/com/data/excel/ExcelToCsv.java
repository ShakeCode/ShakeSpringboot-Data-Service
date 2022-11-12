package com.data.excel;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class ExcelToCsv {

    /**
     * 将excel(xls/xlsx)转换成csv文件
     *
     * @param excelFile
     * @param csvFile
     * @return String
     */
    public static String getCsv(String excelFile, String csvFile) {
        //.xlsx文件后缀转成csv
        if (excelFile.endsWith(".xlsx")) {
//            XLSX2CSV.trans(excelFile, csvFile);
            return csvFile;
        }
        //.xls文件后缀转成csv
        else {
            try {
                OutputStream os = new FileOutputStream(new File(csvFile));
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF8");
                BufferedWriter bw = new BufferedWriter(osw);
                // 载入Excel文件
                WorkbookSettings ws = new WorkbookSettings();
                ws.setLocale(new Locale("en", "EN"));
                Workbook wk = Workbook.getWorkbook(new File(excelFile), ws);
                // 从工作簿(workbook)取得每页(sheets)
                for (int sheet = 0; sheet < wk.getNumberOfSheets(); sheet++) {
                    Sheet s = wk.getSheet(sheet);
                    Cell[] row = null;
                    // 从每页(sheet)取得每个区块(Cell)
                    for (int i = 0; i < s.getRows(); i++) {
                        row = s.getRow(i);
                        if (row.length > 0) {
                            bw.write(row[0].getContents());
                            for (int j = 1; j < row.length; j++) {
                                //写入分隔符
                                bw.write(',');
                                bw.write(row[j].getContents());
                            }
                        }
                        bw.newLine();
                    }
                }
                bw.flush();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return csvFile;
        }
    }
}
