package com.data.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


//官方文档:http://poi.apache.org/components/spreadsheet/how-to.html#sxssf

/**
 * The type Example event user model.
 * 2020-11-15
 */
public class ExampleEventUserModel {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleEventUserModel.class);

    /**
     * Process one sheet.
     * @param filename the filename
     * @throws Exception the exception
     */
    public void processOneSheet(String filename, int sheetId) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        // To look up the Sheet Name / Sheet Order / rID,
        //  you need to processExcelSheet the core Workbook stream.
        // Normally it's of the form rId# or rSheet#
        InputStream sheet2 = r.getSheet("rId" + sheetId);
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
        LOG.info(">>>> data list:{}", SheetHandler.dataList);
    }

    /**
     * Process all sheets.
     * @param filename the filename
     * @throws Exception the exception
     */
    public void processAllSheets(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
            System.out.println("Processing new sheet:\n");
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
            System.out.println("");
        }
    }

    /**
     * Fetch sheet parser xml reader.
     * @param sst the sst
     * @return the xml reader
     * @throws SAXException                 the sax exception
     * @throws ParserConfigurationException the parser configuration exception
     */
    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        ContentHandler contentHandler = new SheetHandler(sst);
        parser.setContentHandler(contentHandler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;

        // 每行数据
        private List<String> rowList = new ArrayList<>();

        // 全部sheet数据
        public static final List<List<String>> dataList = new ArrayList<>();
        //当前行
        private int curRow = 0;
        //当前列
        private int curCol = 0;
        //日期标志
        private boolean dateFlag;
        //数字标志
        private boolean numberFlag;

        private boolean isTElement;

        /**
         * 总行号
         */
        private Integer totalRowCount;

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        @Override
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) {
            // 获取总行号  格式： A1:B5    取最后一个值即可
            if ("dimension".equals(name) && (totalRowCount == null)) {
                // 本质是xml,将扩展名换成zip,解压缩可以看到组成xlxl\worksheets\shee1.xml
                String dimensionStr = attributes.getValue("ref");
                totalRowCount = Integer.parseInt(dimensionStr.substring(dimensionStr.indexOf(":") + 2)) - 1;
                LOG.info("@@@ total num：{}", totalRowCount);
            }

            // c => cell
            if (name.equals("c")) {
                // Print the cell reference
                // System.out.print(attributes.getValue("r") + " - ");   // 输出A1 B1 等位置信息
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if (cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }

                //日期格式
                String cellDateType = attributes.getValue("s");
                if ("1".equals(cellDateType)) {
                    dateFlag = true;
                } else {
                    dateFlag = false;
                }
                String cellNumberType = attributes.getValue("s");
                if ("2".equals(cellNumberType)) {
                    numberFlag = true;
                } else {
                    numberFlag = false;
                }
            }
            //当元素为t时
            if ("t".equals(name)) {
                isTElement = true;
            } else {
                isTElement = false;
            }
            // Clear contents cache
            lastContents = "";
        }

        @Override
        public void endElement(String uri, String localName, String name) {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            // 根据SST的索引值的到单元格的真正要存储的字符串
            // 这时characters()方法可能会被调用多次
            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
//                System.out.println(idx);
                lastContents = sst.getItemAt(idx).getString();
                nextIsString = false;
            }
            // v => contents of a cell
            // Output after we've seen the string contents
          /*  if (name.equals("v")) {
                System.out.println(lastContents);
            }*/

            //t元素也包含字符串
            if (isTElement) {
                LOG.info("@@@@@T element");
                String value = lastContents.trim();
                rowList.add(curCol, value);
                curCol++;
                isTElement = false;
                // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
                // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
            } else if ("v".equals(name)) {
                String value = lastContents.trim();
                value = value.equals("") ? " " : value;
                //日期格式处理
                if (dateFlag) {
                    Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd/MM/yyyy");
                    value = dateFormat.format(date);
                }
                //数字类型处理
                if (numberFlag) {
                    BigDecimal bd = new BigDecimal(value);
                    value = bd.setScale(3, BigDecimal.ROUND_UP).toString();
                }
                rowList.add(curCol, value);
                curCol++;
            } else {
                //如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
                if (name.equals("row")) {
                    // rowReader.getRows(sheetIndex,curRow,rowList);
                    LOG.info(">>>>>data:{}", rowList);
                    dataList.add(new ArrayList<>(rowList));
                    rowList.clear();
                    curRow++;
                    curCol = 0;
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            lastContents += new String(ch, start, length);
        }
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        ExampleEventUserModel example = new ExampleEventUserModel();
        example.processOneSheet("F:\\黑名单号码.xlsx", 2);
        // example.processAllSheets(args[0]);
    }
}
