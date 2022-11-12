package com.data.controller;

import com.data.util.DateUtils;
import com.data.util.GsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * The type Cal plan send time controller.
 * 设置工作免打扰时间(包含2段限制窗口(当前亚洲时区东八区),06:20-08:00, 09:30-23:59),周末免打扰开关按钮，计算可发送的计划发送时间
 * 2021-03-01
 */
public class CalPlanSendTimeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalPlanSendTimeController.class);

    private static final String[] TIME_ARR = new String[]{"00:00:00-12:00:30", "12:00:59-23:59:00"};

    private String calPlanSendTime(String nowUtcDate) throws ParseException {
        Date curUtcDateTime = StringUtils.isNotEmpty(nowUtcDate) ? DateUtils.parase(nowUtcDate, DateUtils.DATEFORMATSECOND) : new Date();
        LOGGER.info(">>now date:{}", DateUtils.format(curUtcDateTime, DateUtils.DATEFORMATSECOND));
        // UTC时间 + 8小时
        String nowDateTime = DateUtils.addDateByField(curUtcDateTime, Calendar.HOUR, 8);
        LOGGER.info(">> offset date:{}", nowDateTime);
        // 取出日期部分
        String nowDatePartStr = nowDateTime.substring(0, 10);
        LOGGER.info(">>> now date part str:{}", nowDatePartStr);
        // 获取系统配置免打扰时间段,并还原UTC时间
        String[][] workRestTimeArr = getWorkRestTimeConfig(nowDatePartStr);
        String win1StartTime = workRestTimeArr[0][0];
        String win1EndTime = workRestTimeArr[0][1];
        String win2StartTime = workRestTimeArr[1][0];
        String win2EndTime = workRestTimeArr[1][1];
        if (DateUtils.isWeekend(curUtcDateTime)) {
            LOGGER.info(">>is weekwend ...");
            if (checkIfWeekendRest()) {
                // 周末免打扰开启, 当前时间推至下周一00:00:00发送
                Date nextWeekMonDay = DateUtils.getNextWeekMonday(curUtcDateTime);
                return DateUtils.format(DateUtils.createHHMMSS(nextWeekMonDay, 0, 0, 0), DateUtils.DATEFORMATSECOND);
            } else {
                // 周末免打扰关闭
                return getWorkTimeFixPlanSendTime(curUtcDateTime, win1StartTime, win1EndTime, win2StartTime, win2EndTime);
            }
        } else {
            // 工作时间
            LOGGER.info(">>is not weekwend ...");
            return getWorkTimeFixPlanSendTime(curUtcDateTime, win1StartTime, win1EndTime, win2StartTime, win2EndTime);
        }
    }

    private String getWorkTimeFixPlanSendTime(Date curUtcDateTime, String win1StartTime, String win1EndTime, String win2StartTime, String win2EndTime) throws ParseException {
        LOGGER.info("curUtcDateTime:{}", DateUtils.format(curUtcDateTime, DateUtils.DATEFORMATSECOND));
        LOGGER.info("win1StartTime:{},win1EndTime:{}", win1StartTime, win1EndTime);
        LOGGER.info("win2StartTime:{},win2EndTime:{}", win2StartTime, win2EndTime);
        if (checkIfWorkTimeRest()) {
            // 工作时间免打扰开启
            return getFixPlanSendTime(curUtcDateTime, win1StartTime, win1EndTime, win2StartTime, win2EndTime);
        } else {
            // 工作时间免打扰关闭,立马发送
            return DateUtils.format(curUtcDateTime, DateUtils.DATEFORMATSECOND);
        }
    }

    private String getFixPlanSendTime(Date curUtcDateTime, String win1StartTime, String win1EndTime, String win2StartTime, String win2EndTime) throws ParseException {
        if (checkIfBetweenDate(curUtcDateTime, win1StartTime, win1EndTime)) {
            // 当前时间在窗口1之间,取窗口1结束时间+10秒
            return DateUtils.addDateByField(DateUtils.parase(win1EndTime, DateUtils.DATEFORMATSECOND), Calendar.SECOND, 10);
        } else if (checkIfBetweenDate(curUtcDateTime, win2StartTime, win2EndTime)) {
            // 当前时间在窗口2之间,取窗口2结束时间+10秒
            return DateUtils.addDateByField(DateUtils.parase(win2EndTime, DateUtils.DATEFORMATSECOND), Calendar.SECOND, 10);
        } else {
            // 均不在免打扰窗口内,立即发送
            return DateUtils.format(curUtcDateTime, DateUtils.DATEFORMATSECOND);
        }
    }

    private boolean checkIfBetweenDate(Date curUtcDateTime, String win1StartTime, String win1EndTime) throws ParseException {
        return curUtcDateTime.getTime() >= (DateUtils.parase(win1StartTime, DateUtils.DATEFORMATSECOND)).getTime()
                && curUtcDateTime.getTime() <= (DateUtils.parase(win1EndTime, DateUtils.DATEFORMATSECOND)).getTime();
    }

    private String[][] getWorkRestTimeConfig(String nowDatePartStr) throws ParseException {
        LOGGER.info(">>config rest time:{}", GsonUtil.toJson(TIME_ARR));
        // 早上免打扰开始时间
        String amStartTime = nowDatePartStr + " " + TIME_ARR[0].split("-")[0];
        // 早上免打扰结束时间
        String amEndTime = nowDatePartStr + " " + TIME_ARR[0].split("-")[1];
        // 下午免打扰开始时间
        String pmStartTime = nowDatePartStr + " " + TIME_ARR[1].split("-")[0];
        // 下午免打扰结束时间
        String pmEndTime = nowDatePartStr + " " + TIME_ARR[1].split("-")[1];
        // 还原为UTC时间
        String utcamStartTime = DateUtils.addDateByField(amStartTime, Calendar.HOUR, -8);
        String utcamEndTime = DateUtils.addDateByField(amEndTime, Calendar.HOUR, -8);
        String utcpmStartTime = DateUtils.addDateByField(pmStartTime, Calendar.HOUR, -8);
        String utcpmEndTime = DateUtils.addDateByField(pmEndTime, Calendar.HOUR, -8);
        return new String[][]{new String[]{utcamStartTime, utcamEndTime}, new String[]{utcpmStartTime, utcpmEndTime}};
    }

    public static void main(String[] args) throws ParseException {
        String nowUtcDate = "2021-03-06 00:00:00";
        String fixSendDateTime = new CalPlanSendTimeController().calPlanSendTime(nowUtcDate);
        LOGGER.info(">>> final plan send time:{}", fixSendDateTime);
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
        // 工作时间免打扰
        return true;
    }

    private static boolean checkIfWeekendRest() {
        // 周末时间免打扰
        return false;
    }

}
