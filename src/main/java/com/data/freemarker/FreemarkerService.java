/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.freemarker;

import com.alibaba.fastjson.util.IOUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 功能：将指定的ftl模板输出到文件
 */
public class FreemarkerService {

    /**
     * The entry point of application.
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("host", "127.0.0.1:8060");
        dataMap.put("dataBody", "{\"userName\": \"admin\",\"password\": \"123456\"}");
        // 输出控制台
        print("api.ftl", dataMap);
        // 输出文件
        printFile("api.ftl", dataMap, "./src/main/resources/api-curl.txt");
    }

    /**
     * 打印到控制台
     * @param ftlName the ftl name
     * @param dataMap the root
     */
    public static void print(String ftlName, Map<String, Object> dataMap) {
        try {
            Template template = getTemplate(ftlName);
            template.process(dataMap, new PrintWriter(System.out));
        } catch (TemplateException te) {
            te.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * 通过文件名加载模板
     * @param ftlName ftl文件名
     * @return template template
     * @throws Exception the exception
     */
    public static Template getTemplate(String ftlName) {
        try {
            // 通过Freemaker的Configuration读取相应的ftl
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setSharedVariable("company", "Foo Inc.");
            cfg.setEncoding(Locale.US, StandardCharsets.UTF_8.name());
            File file = new File("./src/main/resources/templates/");
            cfg.setDirectoryForTemplateLoading(file);
            // 在模板文件目录中找到名称为name的文件
            Template template = cfg.getTemplate(ftlName);
            return template;
        } catch (IOException | TemplateModelException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输出到文件
     * @param ftlName ftl文件名
     * @param dataMap 传入的map
     * @param outFile 输出后的文件全部路径
     */
    public static void printFile(String ftlName, Map<String, Object> dataMap, String outFile) {
        Writer out = null;
        try {
            File file = new File(outFile);
            if (!file.getParentFile().exists()) {
                // 判断有没有父路径，就是判断文件整个路径是否存在,不存在就全部创建
                file.getParentFile().mkdir();
            }
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            Template template = getTemplate(ftlName);
            template.process(dataMap, out);
            out.flush();
        } catch (TemplateException te) {
            te.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(out);
        }
    }
}