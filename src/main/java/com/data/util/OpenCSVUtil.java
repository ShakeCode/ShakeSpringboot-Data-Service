/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.util;

import com.data.model.Person;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://blog.csdn.net/qq_41609208/article/details/111461171
 * The type Open csv util.
 */
public class OpenCSVUtil {

    private static final String TAB = "\t";

    /**
     * Write csv 2.
     * @param dataList the data list
     * @param filePath the file path
     */
    public void writeCSV2(List<Person> dataList, String filePath) {
        try (
                Writer writer = new FileWriterWithEncoding(filePath, StandardCharsets.UTF_8);
                ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                        // 分隔符
                        .withSeparator(ICSVWriter.DEFAULT_SEPARATOR)
                        // 不使用引号
                        .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER)
                        .withEscapeChar(CSVWriter.NO_ESCAPE_CHARACTER)
                        .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                        .build();
        ) {
            // 手动加上BOM标识
            writer.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            // 设置显示的顺序
            String[] columnMapping = {"name", "age", "sex", "phone", "address"};
            ColumnPositionMappingStrategy<Person> mapper =
                    new ColumnPositionMappingStrategy<>();
            mapper.setType(Person.class);
            mapper.setColumnMapping(columnMapping);

            // 写表头
            String[] header = {"姓名", "年龄", "性别", "手机", "住址"};
            csvWriter.writeNext(header);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withMappingStrategy(mapper)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withEscapechar('\\').build();
            beanToCsv.write(dataList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
        System.out.println(filePath + "数据导出成功");
    }

    /**
     * Read csv 2.
     * @param finalPath the final path
     */
    public void readCSV2(String finalPath) {
        try ( // 使用BOMInputStream自动去除UTF-8中的BOM
              Reader reader = new InputStreamReader(new BOMInputStream(
                      new FileInputStream(finalPath)), StandardCharsets.UTF_8);
              CSVReader csvReader = new CSVReader(reader);) {
            // 列名的映射
            HeaderColumnNameTranslateMappingStrategy<Person> strategy =
                    new HeaderColumnNameTranslateMappingStrategy<>();
            strategy.setType(Person.class);
            Map<String, String> columnMapping = new HashMap<>(5);
            columnMapping.put("姓名", "name");
            columnMapping.put("年龄", "age");
            columnMapping.put("性别", "sex");
            columnMapping.put("手机", "phone");
            columnMapping.put("住址", "address");
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Person> csvToBean = new CsvToBeanBuilder(csvReader)
                    .withMappingStrategy(strategy)
                    .withSkipLines(2)
                    .withVerifyReader(true)
                    .build();
            List<Person> list = csvToBean.parse();
            for (Person p : list) {
                System.out.println(p.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        List<Person> dataList = new ArrayList<>();
        Person person1 = new Person();
        person1.setName("張三");
        person1.setSex(1);
        person1.setAge(55);
        person1.setPhone(TAB + "13911111111" + TAB);
        person1.setAddress("北京海淀区");
        dataList.add(person1);
        Person person2 = new Person();
        person2.setName("小美");
        person2.setSex(0);
        person2.setAge(20);
        person2.setPhone("13911112222" + "https://blog.csdn.net/weixin_33503998/article/details/114762357");
        person2.setAddress("北京西城区");
        dataList.add(person2);
        Person person3 = new Person();
        person3.setName("小明");
        person3.setSex(1);
        person3.setAge(25);
        person3.setPhone("13933333333");
        person3.setAddress("北京海淀区");
        dataList.add(person3);

        OpenCSVUtil writer = new OpenCSVUtil();
        String finalPath2 = "D:/bbb.csv";
        writer.writeCSV2(dataList, finalPath2);
        writer.readCSV2(finalPath2);
    }
}