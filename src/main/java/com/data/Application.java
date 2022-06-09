package com.data;

import com.data.service.custer.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/*解决占用问题

1、查找端口占用的进程ID
netstat -ano | findstr 8019

2、查找进程ID 对应的程序
tasklist | findstr 1124

3、终止运行的程序
taskkill /f /t  /im java.exe*/

//@EnableEncryptableProperties
@EnableCaching
@EnableScheduling
@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.data"})
public class Application {

    private static ConfigurableApplicationContext configurableApplicationContext;

    public static void main(String[] args) {
        configurableApplicationContext = SpringApplication.run(Application.class, args);
//        xml 形式 可使用的上下文
//        FileSystemXmlApplicationContext
//        ClassPathXmlApplicationContext
        System.out.println("user-service bean:" + configurableApplicationContext.getBean(UserService.class));
        System.out.println("user-service bean:" + configurableApplicationContext.getBean("user-service"));
    }
}