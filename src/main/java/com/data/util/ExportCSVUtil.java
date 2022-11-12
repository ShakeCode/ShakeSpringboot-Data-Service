package com.data.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ExportCSVUtil {
    // 数据类型
    private Class<T> cls;

    // 填充数据
    private List<T> list;

    // 存储get方法
    private List<Method> methods;

    /**
     * 该方式导出csv文件会显示标题
     * @param list 填充数据
     * @param cls  数据类型
     * @throws Exception
     */
    public ExportCSVUtil(List<T> list, Class<T> cls) throws Exception {
        this.list = list;
        this.cls = cls;
        parse();
    }

    /**
     * 调用此方法，将数据写出到指定的文件流中
     * @param out 输出流
     * @throws Exception
     */
    public void doExport(OutputStream out) throws Exception {
        // utf8编码
        byte[] bytes = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        // 写入BOM
        out.write(bytes);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, "utf8");
        BufferedWriter writer = new BufferedWriter(outputStreamWriter);
        writeBody(writer, list);
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }

    /**
     * 写数据
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void writeBody(BufferedWriter writer, List<T> list) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        //数据遍历
        for (T obj : list) {
            int i = 0;
            int length = methods.size();
            for (Method method : methods) {
                Object result = method.invoke(obj, new Object[]{});
                String str = null;
                if (result == null) {
                    str = "";
                } else {
                    // 处理文本中的"
                    str = result.toString().replaceAll("\"", "\"\"");
                }

                if (i++ <= length - 1) {
                    // 文本用双引号包裹
                    writer.write("\"" + str + "\",");
                } else {
                    // 最后的元素需要使用换行符而不是“，” 需要特别注意
                    writer.write("\"" + str + "\"");
                }
            }
            // 换行
            writer.newLine();
        }
    }

    /**
     * 解析实体类，获取get或者 is方法
     */
    private void parse() throws NoSuchMethodException {
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Method method = null;
            try {
                // 普通get方法   getXnnn
                cls.getDeclaredMethod("get" + StringUtils.capitalize(fieldName));
            } catch (Exception e) {
            }
            if (method == null) {
                try {
                    // bool属性对应的方法  isXnn
                    cls.getDeclaredMethod("is" + StringUtils.capitalize(fieldName));
                } catch (Exception e) {
                }
            }
            // 方法不存在时抛出异常
            if (method == null) {
                throw new NoSuchMethodException(cls + "的属性" + fieldName + "对象没有对应的getter 方法");
            }
            //获取getter方法
            methods.add(method);
        }
    }
}
