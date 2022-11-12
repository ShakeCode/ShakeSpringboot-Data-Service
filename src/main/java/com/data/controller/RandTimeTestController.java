package com.data.controller;

import com.data.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RandTimeTestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandTimeTestController.class);

    public static void main(String[] args) throws ParseException {
        String fixSendDateTime;
        // 免打扰时间
        // 00:20:00-12:00:00, 12:30:00-13:30:30
        // 06:20:00-08:00:00, 09:30:00-23:30:59
        // 00:20:00-12:00:00, 12:30:00-23:59:50
        String[] timeArr = new String[]{"04:00:00-12:00:30", "12:00:59-23:59:59"};
        // 当前时间UTC
        String nowDateTimeStr = "2021-02-19 02:00:59";
        Date nowDate = DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND);
        LOGGER.info(">>now date:{}", DateUtils.format(nowDate, DateUtils.DATEFORMATSECOND));
        // UTC时间 + 8小时
        String nowDateTime = DateUtils.addDateByField(nowDate, Calendar.HOUR, 8);
        LOGGER.info(">> offset date:{}", nowDateTime);
        // 取出日期部分
        String nowDateStr = nowDateTime.substring(0, 10);
        LOGGER.info(">>> now date part str:{}", nowDateStr);

        // 早上免打扰开始时间
        String amStartTime = nowDateStr + " " + timeArr[0].split("-")[0];
        // 早上免打扰结束时间
        String amEndTime = nowDateStr + " " + timeArr[0].split("-")[1];
        // 下午免打扰开始时间
        String pmStartTime = nowDateStr + " " + timeArr[1].split("-")[0];
        // 下午免打扰结束时间
        String pmEndTime = nowDateStr + " " + timeArr[1].split("-")[1];

        // 还原为UTC时间
        String utcamStartTime = DateUtils.addDateByField(amStartTime, Calendar.HOUR, -8);
        String utcamEndTime = DateUtils.addDateByField(amEndTime, Calendar.HOUR, -8);
        String utcpmStartTime = DateUtils.addDateByField(pmStartTime, Calendar.HOUR, -8);
        String utcpmEndTime = DateUtils.addDateByField(pmEndTime, Calendar.HOUR, -8);

        // 判断是否周末
        if (DateUtils.isWeekend(nowDate)) {
            LOGGER.info(">>is weekwend ...");
            // 是否工作时间免打扰
            if (checkIfWorkTimeRest()) {
                // 推至周一后发送,处理24/24*2 时间差
         /*       int offSetTimeHour = getOffSetTimeHour(nowDate);
                utcamStartTime = DateUtils.addDateByField(utcamStartTime, Calendar.HOUR, offSetTimeHour);
                utcamEndTime = DateUtils.addDateByField(utcamEndTime, Calendar.HOUR, offSetTimeHour);
                utcpmStartTime = DateUtils.addDateByField(utcpmStartTime, Calendar.HOUR, offSetTimeHour);
                utcpmEndTime = DateUtils.addDateByField(utcpmEndTime, Calendar.HOUR, offSetTimeHour);*/
                // 取出可发送短信的时间
                fixSendDateTime = getFixPlanSendTime(nowDateTimeStr, utcamStartTime, utcamEndTime, utcpmStartTime, utcpmEndTime);
                LOGGER.info(">>before add offset hour:{}", fixSendDateTime);
                // 推至周一后发送,处理24/24*2 时间差
                int offSetTimeHour = getOffSetTimeHour(nowDate);
                LOGGER.info(">> add offset hour:{}", offSetTimeHour);
                fixSendDateTime = DateUtils.addDateByField(fixSendDateTime, Calendar.HOUR, offSetTimeHour);
            } else {
                // 免打扰关闭，不需要推至周一发送
                LOGGER.info(">>>免打扰关闭，不需要推至周一发送");
                // 当前时间推送到周一
                fixSendDateTime = DateUtils.format(DateUtils.getNextWeekMonday(DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND)), DateUtils.DATEFORMATSECOND);
            }
        } else {
            LOGGER.info(">>is not weekwend ...");
            // 是否工作时间免打扰
            if (checkIfWorkTimeRest()) {
                fixSendDateTime = getFixPlanSendTime(nowDateTimeStr, utcamStartTime, utcamEndTime, utcpmStartTime, utcpmEndTime);
            } else {
                // 当前时间延迟5分钟发送
                fixSendDateTime = DateUtils.addDateByField(nowDateTimeStr, Calendar.MINUTE, 5);
            }
        }
        LOGGER.info(">>> final plan send time:{}", fixSendDateTime);
    }

    private static String getFixPlanSendTime(String nowDateTimeStr, String utcamStartTime, String utcamEndTime, String utcpmStartTime, String utcpmEndTime) throws ParseException {
        LOGGER.info(">> utc  amStartTime:{},amEndTime:{}", utcamStartTime, utcamEndTime);
        LOGGER.info(">> utc  pmStartTime:{},pmEndTime:{}", utcpmStartTime, utcpmEndTime);
        LOGGER.info(">> utc  now :{}", nowDateTimeStr);

        String fixSendDateTime = null;
        if (DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND).getTime() < (
                DateUtils.parase(utcamStartTime, DateUtils.DATEFORMATSECOND)).getTime()
                ) {
            LOGGER.info(">> scene :{}", 1);
            // 在早上免打扰开始时间之前
            fixSendDateTime = randomDateStr(nowDateTimeStr, utcamStartTime);
        } else if ((DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND).getTime() >= (
                DateUtils.parase(utcamEndTime, DateUtils.DATEFORMATSECOND).getTime()) &&
                (DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND).before(
                        DateUtils.parase(utcpmStartTime, DateUtils.DATEFORMATSECOND)))
        )) {
            LOGGER.info(">> scene :{}", 2);
            // 在早上免打扰开始时间 和下午免打扰开始时间之间
            fixSendDateTime = randomDateStr(nowDateTimeStr, utcpmStartTime);
        } else if (DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND).after(
                DateUtils.parase(utcpmEndTime, DateUtils.DATEFORMATSECOND))) {
            LOGGER.info(">> scene :{}", 3);
            // 在下午免打扰结束时间之后
            fixSendDateTime = randomDateStr(nowDateTimeStr, DateUtils.addDateByField(nowDateTimeStr, Calendar.MINUTE, 1));
        } else {
            LOGGER.info(">> scene :{}", 4);
            if ((DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND).getTime() >=
                    DateUtils.parase(utcamStartTime, DateUtils.DATEFORMATSECOND).getTime()) &&
                    (DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND).getTime() <=
                            DateUtils.parase(utcamEndTime, DateUtils.DATEFORMATSECOND).getTime())) {
                LOGGER.info(">> scene :{}", 5);
                fixSendDateTime = randomDateStr(utcamEndTime, DateUtils.addDateByField(utcamEndTime, Calendar.SECOND, 10));
            } else if ((DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND).getTime() >=
                    DateUtils.parase(utcpmStartTime, DateUtils.DATEFORMATSECOND).getTime()) &&
                    (DateUtils.parase(nowDateTimeStr, DateUtils.DATEFORMATSECOND).getTime() <=
                            DateUtils.parase(utcpmEndTime, DateUtils.DATEFORMATSECOND).getTime())) {
                LOGGER.info(">> scene :{}", 6);
                fixSendDateTime = randomDateStr(utcpmEndTime, DateUtils.addDateByField(utcpmEndTime, Calendar.SECOND, 10));
            } else {
                LOGGER.info(">>> 不可识别时间段");
            }
        }
        return fixSendDateTime;
    }

    private static int getOffSetTimeHour(Date nowDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            // 周六
            return 48;
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            // 周日
            return 24;
        } else {
            return 0;
        }
    }


    private static boolean checkIfWorkTimeRest() {
        boolean isDisturb = true;
        return isDisturb;
    }


    public static String testRondomDate(String startTime, String endTime) {
        // Date date = randomDate("2017-07-01","20018-01-01");
        // Date date = randomDate("08:00:00", "09:00:59");
        Date date = randomDate(startTime, endTime);
        /*        System.out.println(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));*/
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    private static String randomDateStr(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            // 构造开始日期
            Date start = format.parse(beginDate);
            // 构造结束日期
            Date end = format.parse(endDate);
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return DateUtils.format(new Date(date), DateUtils.DATEFORMATSECOND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取随机日期
     * @param beginDate 起始日期，格式为：yyyy-MM-dd
     * @param endDate   结束日期，格式为：yyyy-MM-dd
     * @return
     */
    private static Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            // 构造开始日期
            Date start = format.parse(beginDate);
            // 构造结束日期
            Date end = format.parse(endDate);
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    public static String getRondomSendTime(String[] timeArr) {
        // String[] timeArr = new String[]{"06:20:00-12:00:00", "12:30:00-13:30:30"};
        String amStartTime = timeArr[0].split("-")[0];
        String amEndTime = timeArr[0].split("-")[1];

        String pmStartTime = timeArr[1].split("-")[0];
        String pmEndTime = timeArr[1].split("-")[1];

        Date date = randomDate(amStartTime, amEndTime, pmStartTime, pmEndTime);
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    private static Date randomDate(String amStartTime, String amEndTime
            , String pmStartTime, String pmEndTime) {
        try {
            // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            // 构造开始日期
            Date amStartDate = format.parse(amStartTime);
            // 构造结束日期
            Date amEndDate = format.parse(amEndTime);

            Date pmStartDate = format.parse(pmStartTime);
            Date pmEndDate = format.parse(pmEndTime);
            /*if (amStartDate.getTime() >= end.getTime()) {
                return null;
            }*/
            long date = randomExclude(amStartDate.getTime(), amEndDate.getTime()
                    , pmStartDate.getTime(), pmEndDate.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long randomExclude(long amStartDate, long amEndDate
            , long pmStartDate, long pmEndDate) {
        long rtn = amStartDate - (long) (Math.random() * (amEndDate - amStartDate));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if ((rtn == amStartDate || rtn == amEndDate) || (rtn == pmStartDate || rtn == pmEndDate)) {
            return randomExclude(amStartDate, amEndDate
                    , pmStartDate, pmEndDate);
        }
        return rtn;
    }

}
