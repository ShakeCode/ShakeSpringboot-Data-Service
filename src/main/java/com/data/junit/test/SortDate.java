package com.data.junit.test;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SortDate {

    private static final Logger LOG = LoggerFactory.getLogger(SortDate.class);

    public static void main(String[] args) throws ParseException {
        // sortDate1();
        List<Map<String, Object>> datas = Lists.newArrayList(new HashedMap<String, Object>(2) {{
            put("name", "jack");
            put("sendTime", "2020-09-10 22:18:22");
        }}, new HashedMap<String, Object>(2) {{
            put("name", "linn");
            put("sendTime", "2020-08-10 22:18:22");
        }}, new HashedMap<String, Object>(2) {{
            put("name", "ding");
            put("sendTime", "2020-09-20 22:18:22");
        }}, new HashedMap<String, Object>(2) {{
            put("name", "hevin");
            put("sendTime", "2020-09-05 22:18:22");
        }});
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        datas.sort((o1, o2) -> {
            try {
                return df.parse(MapUtils.getString(o2, "sendTime")).compareTo(df.parse(MapUtils.getString(o1, "sendTime")));
            } catch (ParseException e) {
                LOG.error("prase exception:{}", e.getMessage());
            }
            return 0;
        });
        datas.forEach(System.out::println);

        // 比较毫秒数会出现意想不到的结果！！！
        LOG.info("----->stream lambda sort:");
        List<Map<String, Object>> temList = datas.stream().sorted((o1, o2) -> {
            try {
                return df.parse(MapUtils.getString(o2, "sendTime")).compareTo(df.parse(MapUtils.getString(o1, "sendTime")));
            } catch (ParseException e) {
                LOG.error("prase exception:{}", e.getMessage());
            }
            return 0;
        }).collect(Collectors.toList());

        temList.forEach(System.out::println);
        LOG.info("----->stream lambda max:");
        Map<String, Object> maxSendTimeMap = datas.stream().max((o1, o2) -> {
            try {
                // 升序取到最大  降序序取到最小
                return df.parse(MapUtils.getString(o1, "sendTime")).compareTo(df.parse(MapUtils.getString(o2, "sendTime")));
            } catch (ParseException e) {
                LOG.error("prase exception:{}", e.getMessage());
            }
            return 0;
        }).get();
        LOG.info("max send time:{}", maxSendTimeMap);

        LOG.info("----->stream lambda min:");
        Map<String, Object> minSendTimeMap = datas.stream().min((o1, o2) -> {
            try {
                return df.parse(MapUtils.getString(o1, "sendTime")).compareTo(df.parse(MapUtils.getString(o2, "sendTime")));
            } catch (ParseException e) {
                LOG.error("prase exception:{}", e.getMessage());
            }
            return 0;
        }).get();
        System.out.println(minSendTimeMap);

        LOG.info("min send time:{}", minSendTimeMap);


        Collections.sort(datas, (o1, o2) -> {
            try {
                return df.parse(MapUtils.getString(o2, "sendTime")).compareTo(df.parse(MapUtils.getString(o1, "sendTime")));
            } catch (ParseException e) {
                LOG.error("prase exception:{}", e.getMessage());
            }
            return 0;
        });

    }

    private static void sortDate1() throws ParseException {
        String s1 = "2019-08-10 22:18:22";
        String s2 = "2018-08-10 22:18:22";
        String s3 = "2017-08-10 22:19:22";
        List<Date> arr = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        arr.add(df.parse(s1));
        arr.add(df.parse(s2));
        arr.add(df.parse(s3));
        for (int i = 0, len = getd(arr).size(); i < len; i++) {
            System.out.println(arr.get(i));
        }
    }

    public static List<Date> getd(List<Date> dateList) {
        // 升序
        /*dateList.sort((a1, a2) -> {
            return a1.compareTo(a2);
        });*/
        // 简化
        // dateList.sort(Date::compareTo);
        // 降序
        dateList.sort((a1, a2) -> {
            return a2.compareTo(a1);
        });
        return dateList;
    }
}
