package com.data.email;

import com.data.exception.ServiceException;
import com.data.model.ResultVO;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;


/**
 * The type Email service.
 * https://blog.csdn.net/LwinnerG/article/details/108519809
 */
@RequiredArgsConstructor
@Service
public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class);

    private final TemplateEngine templateEngine;

    private final JavaMailSender jms;

    @Value("${spring.mail.username}")
    private String fromAccount;

    @Value("${spring.mail.toAccount:abc903936584@163.com}")
    private String toAccount;

    /**
     * Send simple email result vo.
     * @return the result vo
     */
    public ResultVO sendSimpleEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // 发送方
            message.setFrom(fromAccount);
            // 接收方
            message.setTo(toAccount);
            // 标题
            message.setSubject("一封文字内容的邮件");
            // 内容
            message.setText("hi my friend, i miss u");
            jms.send(message);
            return ResultVO.success("success");
        } catch (Exception e) {
            LOGGER.error("send email fail", e);
            throw new ServiceException("send email fail");
        }
    }

    /**
     * Send html email result vo.
     * @return the result vo
     */
    public ResultVO sendHtmlEmail() {
        MimeMessage message;
        try {
            message = jms.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAccount);
            helper.setTo(toAccount);
            helper.setSubject("一封HTML格式的邮件");
            // 带HTML格式的内容
            StringBuffer sb = new StringBuffer("<p style='color:#42b983'>使用Spring Boot发送HTML格式邮件。</p>");
            helper.setText(sb.toString(), true);
            jms.send(message);
            return ResultVO.success("success");
        } catch (Exception e) {
            LOGGER.error("send html email fail", e);
            throw new ServiceException("send html email fail");
        }
    }

    /**
     * Send attachments mail result vo.
     * @return the result vo
     */
    public ResultVO sendAttachmentsMail() {
        MimeMessage message;
        try {
            message = jms.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAccount);
            helper.setTo(toAccount);
            helper.setSubject("一封带附件的邮件");
            helper.setText("详情参见附件内容！");
            // 传入附件
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/file/公共开源api文档.docx"));
            helper.addAttachment("公共开源api文档.docx", file);
            jms.send(message);
            return ResultVO.success("success");
        } catch (Exception e) {
            LOGGER.error("send Attachments fail", e);
            throw new ServiceException("send Attachments email fail");
        }
    }

    /**
     * Send inline mail result vo.
     * @return the result vo
     */
    public ResultVO sendInlineMail() {
        MimeMessage message;
        try {
            message = jms.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAccount);
            helper.setTo(toAccount);
            helper.setSubject("一封带静态资源的邮件");
            helper.setText("<html><body>美图：<img src='cid:img'/></body></html>", true);
            // 传入附件
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/img/monkey.jpg"));
            helper.addInline("img", file);
            jms.send(message);
            return ResultVO.success("success");
        } catch (Exception e) {
            LOGGER.error("send inline fail", e);
            throw new ServiceException("send inline email fail");
        }
    }

    /**
     * Send template email result vo.
     * @param code the code
     * @return the result vo
     */
    public ResultVO sendTemplateEmail(String code) {
        MimeMessage message;
        try {
            message = jms.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAccount);
            helper.setTo(toAccount);
            helper.setSubject("邮件摸板测试");
            // 处理邮件模板
            Context context = new Context();
            context.setVariable("code", code);
            String template = templateEngine.process("emailTemplate", context);
            helper.setText(template, true);
            jms.send(message);
            return ResultVO.success("success");
        } catch (Exception e) {
            LOGGER.error("send template fail", e);
            throw new ServiceException("send template email fail");
        }
    }
}
