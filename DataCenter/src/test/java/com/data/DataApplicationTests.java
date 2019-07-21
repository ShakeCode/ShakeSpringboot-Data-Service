package com.data;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional  //执行SQL后回归操作
@EnableEncryptableProperties
public class DataApplicationTests {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void testEncrypt() throws Exception {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("G0CvDz7oJn6");
        //要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("root");
        String password = textEncryptor.encrypt("root123");
        System.out.println("username:"+username);
        System.out.println("password:"+password);

        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");          // 加密的算法，这个算法是默认的
        config.setPassword("ljk");                        // 加密的密钥
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "linjingke";
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);

    }

    @Test
    public void testDe() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("ljk");
        standardPBEStringEncryptor.setConfig(config);
        String encryptedText = "aHsFtlQjatrOP2s8bfLGkUG55z53KLNi";
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println(plainText);

    }

    @Test
    public void contextLoads() {
        send1();
        send2();
//        sendFanoutExchange();
    }

    public void send1() {
        String context = "hi, i am message 1";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("MyTopicExchange", "topic.one", context);
    }

    public void send2() {
        String context = "hi, i am messages 2";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("MyTopicExchange", "topic.two", context);
    }


    public void sendFanoutExchange() {
        String context = "hi, fanout msg ";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("fanoutExchange", "", context);
    }

    public static void main(String[] args) {
       String pass = Base64Utils.encodeToString(new String("hello").getBytes());
        System.out.println(pass);
        System.out.println(new String(Base64Utils.decode(pass.getBytes())));
    }
}


