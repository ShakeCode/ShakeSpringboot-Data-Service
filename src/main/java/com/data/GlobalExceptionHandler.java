package com.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 */
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public Map<String, Object> runtimeExceptionHandler(RuntimeException e, HttpServletRequest request) {
        Map<String, Object> resp = dealMsgMap(e, request);
        return resp;
    }

    @ExceptionHandler(Exception.class)
    public Map<String, Object> exceptionHandler(Exception e, HttpServletRequest request) {
        Map<String, Object> resp = dealMsgMap(e, request);
        return resp;
    }

    private Map<String, Object> dealMsgMap(Exception e, HttpServletRequest request) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 500);
        resp.put("msg", e.getMessage());
        resp.put("data", null);
        resp.put("url", request.getRequestURL().toString());
        log(e, request);
        return resp;
    }

    private void log(Exception ex, HttpServletRequest request) {
        logger.error("************************异常开始*******************************");
        logger.error("请求异常了:", ex);
        logger.error("请求地址：" + request.getRequestURL());
       /* Enumeration enumeration = request.getParameterNames();
        logger.error("请求参数");
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            logger.error(name + "---" + request.getParameter(name));
        }
        StackTraceElement[] error = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : error) {
            logger.error(stackTraceElement.toString());
        }*/
        logger.error("************************异常结束*******************************");
    }
}
