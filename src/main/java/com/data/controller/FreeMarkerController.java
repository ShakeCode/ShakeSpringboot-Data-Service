package com.data.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * The type Free marker controller.
 */
@RequestMapping("/v1")
@RestController
@Api(value = "FreeMarker测试相关的api", tags = "单元测试相关的api")
public class FreeMarkerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeMarkerController.class);

    /**
     * To demo model and view.
     * @param mv the mv
     * @return the model and view
     */
    @ApiOperation(value = "获取所有管理员")
    @GetMapping(value = "/admin/all")
    public ModelAndView toDemo(ModelAndView mv) {
        LOGGER.info("====>>跳转freemarker页面");
        mv.addObject("name", "jack");
        mv.setViewName("freemarker");
        return mv;
    }
}
