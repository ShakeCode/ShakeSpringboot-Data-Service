package com.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * The type Base service.
 * 2020-11-30
 */
@Service
public class BaseService {

    @Autowired
    public HttpServletResponse response;

    @Autowired
    public HttpServletRequest request;


    /**
     * Gets multi file.
     * @param request the request
     * @return the multi file
     */
    public Map<String, MultipartFile> getMultiFile(HttpServletRequest request) {
        // 将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        // 检查form中是否有enctype="multipart/form-data"
        if (resolver.isMultipart(request)) {
            return multipartRequest.getFileMap();
        }
        return null;
    }
}
