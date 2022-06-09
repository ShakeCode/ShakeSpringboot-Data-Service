package com.data.aspect;


import com.data.annotation.DS;
import com.data.dbConfig.DataSourceContextHolder;
import com.data.dbConfig.DataSourceEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Order(0)
@Component
public class DynamicDataSourceAspect {

    @Around("@annotation(com.data.annotation.DS)")
    public Object  beforeSwitchDS(ProceedingJoinPoint proceedingJoinPoint){
        Object[] args = proceedingJoinPoint.getArgs();
        //获得当前访问的class
        Class<?> className = proceedingJoinPoint.getTarget().getClass();
        //获得访问的方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature)proceedingJoinPoint.getSignature()).getParameterTypes();
        String dataSource = DataSourceContextHolder.DEFAULT_DS;
        try {
            // 得到访问的方法对象
            /*getMethod,getDeclaredMethod。getMethod只能调用public声明的方法，而getDeclaredMethod基本可以调用任何类型声明的方法*/
            Method method = className.getDeclaredMethod(methodName, argClass);
            // 判断是否存在@DS注解
            if (method.isAnnotationPresent(DS.class)) {
                DS annotation = method.getAnnotation(DS.class);
                // 取出注解中的数据源名
                dataSource = annotation.value().name();
            }
            // 切换数据源
            System.out.println("------------------>注解数据源dataSource:"+dataSource);
            DataSourceContextHolder.setDB(dataSource);
            return proceedingJoinPoint.proceed(args); //执行方法
        } catch (Exception e) {
            e.printStackTrace();
            DataSourceContextHolder.clearDB(); //清除数据源
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            DataSourceContextHolder.clearDB(); //清除数据源
        }finally {
            DataSourceContextHolder.clearDB(); //清除数据源
        }
        return  null;
    }
}
