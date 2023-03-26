package com.data.controller;

import com.data.email.EmailService;
import com.data.model.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Email controller.
 */
@Api("发送邮件业务")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class EmailController {
    private final EmailService emailService;

    /**
     * Send simple email result vo.
     * @return the result vo
     */
    @ApiOperation("发送常规文字邮件")
    @GetMapping("/simple-email/send")
    public ResultVO sendSimpleEmail() {
        return emailService.sendSimpleEmail();
    }

    /**
     * Send html email result vo.
     * @return the result vo
     */
    @ApiOperation("发送网页格式邮件")
    @GetMapping("/html-email/send")
    public ResultVO sendHtmlEmail() {
        return emailService.sendHtmlEmail();
    }

    /**
     * Send attachments mail result vo.
     * @return the result vo
     */
    @ApiOperation("发送带附件邮件")
    @GetMapping("/attachments-email/send")
    public ResultVO sendAttachmentsMail() {
        return emailService.sendAttachmentsMail();
    }

    /**
     * Send inline mail result vo.
     * @return the result vo
     */
    @ApiOperation("发送带页面和附件邮件")
    @GetMapping("/inline-email/send")
    public ResultVO sendInlineMail() {
        return emailService.sendInlineMail();
    }

    /**
     * Send template email result vo.
     * @param code the code
     * @return the result vo
     */
    @ApiOperation("发送模板格式邮件")
    @GetMapping("/templte-email/send")
    public ResultVO sendTemplateEmail(@RequestParam(value = "code", required = false, defaultValue = "497977") String code) {
        return emailService.sendTemplateEmail(code);
    }
}
