package com.data.excel;

import com.data.excel.handler.ExcelReadDataDelegated;
import com.data.excel.handler.ExcelXlsxReaderWithDefaultHandler;

import java.util.List;

public class ExcelReaderUtil {

    public static void readExcel(String filePath, ExcelReadDataDelegated excelReadDataDelegated) throws Exception {
        int totalRows;
        if (filePath.endsWith(ExcelConstant.EXCEL07_EXTENSION)) {
            ExcelXlsxReaderWithDefaultHandler excelXlsxReader = new ExcelXlsxReaderWithDefaultHandler(excelReadDataDelegated);
            totalRows = excelXlsxReader.process(filePath);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xlsx!");
        }
        System.out.println("读取的数据总行数：" + totalRows);
    }


    public static void main(String[] args) throws Exception {
        String path = "F:\\黑名单号码.xlsx";
        ExcelReaderUtil.readExcel(path, new ExcelReadDataDelegated() {
            @Override
            public void readExcelDate(int sheetIndex, int totalRowCount, int curRow, List<String> cellList) {
                System.out.println("总行数为：" + totalRowCount + " 行号为：" + curRow + " 数据：" + cellList);
            }
        });
    }

}
