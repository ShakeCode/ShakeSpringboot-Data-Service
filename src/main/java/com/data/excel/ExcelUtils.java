package com.data.excel;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Excel utils.
 * POI 官网地址：http://poi.apache.org/spreadsheet/index.html
 * 2020-11-17
 */
public class ExcelUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ExcelUtils.class);

    private OPCPackage xlsxPackage;
    /**
     * 从最左边开始读取的列数
     */
    private int minColumns;

    private Map<String, List<List<String>>> dataMap = new LinkedHashMap<>();

    //结果List
    private List<List<String>> dataList = new ArrayList<>();

    //每行List
    private List<String> rowList = new ArrayList();

    private class SheetCSVContentHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
        private boolean firstCellOfRow;
        private int currentRow = -1;
        private int currentCol = -1;

        private void outputMissingRows(int number) {
            for (int i = 0; i < number; i++) {
                // 空行放进null
                dataList.add(null);
                /*for (int j = 0; j < minColumns; j++) {
                }*/
            }
        }

        @Override
        public void startRow(int rowNum) {
            // row index start from 0
            // col index start from 0
            // 空行处理
            outputMissingRows(rowNum - currentRow - 1);
            currentRow = rowNum;
            currentCol = -1;
        }

        @Override
        public void endRow(int rowNum) {
            // 确保最小列数
            for (int i = currentCol; i < minColumns; i++) {
                rowList.add("");
                LOG.info("第" + (currentRow + 1) + "行," + (currentCol + 2) + "列为空!!");
            }
            if (minColumns == -1) {
                minColumns = rowList.size() - 1;
            }
            dataList.add(new ArrayList<>(rowList));
            rowList.clear();
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment xssfComment) {
            // 处理缺失的单元格
            if (cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }
            int thisCol = (new CellReference(cellReference)).getCol();
            int missedCols = thisCol - currentCol - 1;
            //第currentRow+1行  第thisCol列
            for (int i = 0; i < missedCols; i++) {
                rowList.add("");
                LOG.info("第" + (currentRow + 1) + "行，" + (thisCol) + "列为空");
            }
            rowList.add(formattedValue);
            currentCol = thisCol;
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {
        }
    }

    /**
     * Process sheet.
     * @param styles           the styles
     * @param strings          the strings
     * @param sheetHandler     the sheet handler
     * @param sheetInputStream the sheet input stream
     * @throws IOException  the io exception
     * @throws SAXException the sax exception
     */
    public void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            XSSFSheetXMLHandler.SheetContentsHandler sheetHandler,
            InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        //XMLReader sheetParser = XMLHelper.newXMLReader();
        XMLReader sheetParser = XMLReaderFactory.createXMLReader();
        ContentHandler handler = new XSSFSheetXMLHandler(
                styles, strings, sheetHandler, formatter, false);
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);

    }


    /**
     * Process.
     * @param path the path
     * @return the map
     * @throws IOException        the io exception
     * @throws OpenXML4JException the open xml 4 j exception
     * @throws SAXException       the sax exception
     * @throws Exception          the exception
     */
    public Map<String, List<List<String>>> processAllExcelSheet(String path) throws IOException, OpenXML4JException, SAXException, Exception {
        long startTime = System.currentTimeMillis();
        try {
            OPCPackage pkg = OPCPackage.open(path, PackageAccess.READ);
            this.xlsxPackage = pkg;
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
            XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
            StylesTable styles = xssfReader.getStylesTable();
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            while (iter.hasNext()) {
                try (InputStream stream = iter.next()) {
                    processSheet(styles, strings, new SheetCSVContentHandler(), stream);
                    String sheetName = iter.getSheetName();
                    dataMap.put(sheetName, new ArrayList<>(dataList));
                    // 清除上一个sheet缓存数据
                    dataList.clear();
                }
            }
        } catch (InvalidOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        } finally {
            xlsxPackage.close();
        }
        long endTime = System.currentTimeMillis();
        LOG.info("解析excel耗时:{}ms", endTime - startTime);
        return dataMap;
    }

    /**
     * Process excel sheet list.
     * @param path    the path
     * @param sheetId the sheet id
     * @return the list
     * @throws Exception the exception
     */
    public List<List<String>> processExcelSheet(String path, int sheetId) throws Exception {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        OPCPackage pkg = OPCPackage.open(path, PackageAccess.READ);
        try {
            this.xlsxPackage = pkg;
            ReadOnlySharedStringsTable ross = new ReadOnlySharedStringsTable(this.xlsxPackage);
            XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
            StylesTable styles = xssfReader.getStylesTable();
            inputStream = xssfReader.getSheet("rId" + sheetId);
            processSheet(styles, ross, new SheetCSVContentHandler(), inputStream);
        } catch (InvalidOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        } finally {
            if (xlsxPackage != null) {
                xlsxPackage.close();
            }
            if (pkg != null) {
                pkg.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        long endTime = System.currentTimeMillis();
        LOG.info("解析excel耗时:{}ms", endTime - startTime);
        return dataList;
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(new ExcelUtils().processExcelSheet("F:\\黑名单号码.xlsx", 1));
        System.out.println(new ExcelUtils().processAllExcelSheet("F:\\黑名单号码.xlsx"));
    }
}
