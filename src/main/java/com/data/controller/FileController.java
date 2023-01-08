/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The type File controller.
 */
@Api(value = "文件下载")
@RequestMapping("v1/file")
@RestController
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    /**
     * Download zip.
     * @param request  the request
     * @param response the response
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @ApiOperation(value = "导出zip压缩包")
    @GetMapping("/download/zip")
    public void downloadZip(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        exportZip(response);
    }

    /**
     * Export zip.
     * @param response the response
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    void exportZip(HttpServletResponse response) throws UnsupportedEncodingException {
        //下载的文件名
        String fileName = "浏览器导出zip文件.zip";
        //设置响应头编码
        //  response.setContentType("application/octet-stream;charset=UTF-8");
        response.setContentType("application/zip");
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        //设置文件名
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.displayName()));
        //把Content-Disposition这个字段开放给前端可见，这样前端就可以取到后端生成的文件名
        // response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        try (
                //这里面为了防止内存溢出等问题，使用了BufferedOutputStream缓冲流，可以看到这里是把流输出到了response里面
                ZipOutputStream zos1 = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()), StandardCharsets.UTF_8);
        ) {
            // zos1.setMethod(ZipOutputStream.DEFLATED);
            zos1.putNextEntry(new ZipEntry("第一个内部压缩包.zip"));
            //这里才是嵌套压缩的重点，可以看到这里zos2里面的输出流是zos1，这样，在内部处理的时候只需要处理zos2就行了，输出的操作会由zos2自己往zos里面输出
            try (
                    ZipOutputStream zos2 = new ZipOutputStream(new BufferedOutputStream(zos1), StandardCharsets.UTF_8);
            ) {
                // 这里面内层压缩包的附件
                zos2.setMethod(ZipOutputStream.DEFLATED);
                zos2.putNextEntry(new ZipEntry("11.html"));
                try (InputStream is = new FileInputStream("D:\\log\\20221005\\11.html")) {
                    int b;
                    byte[] buffer = new byte[1024 * 4];
                    while ((b = is.read(buffer)) != -1) {
                        zos2.write(buffer, 0, b);
                    }
                    zos2.flush();
                } catch (Exception e) {
                    LOGGER.error("文件输出异常", e);
                }
            } catch (Exception e) {
                LOGGER.error("内部打包异常", e);
            }
            zos1.flush();
        } catch (Exception e) {
            LOGGER.error("文件下载失败", e);
        }

    }
}
