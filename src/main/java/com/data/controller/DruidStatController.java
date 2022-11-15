package com.data.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Druid stat controller.
 */
@RequestMapping("v1/druid")
@RestController
public class DruidStatController {
    /**
     * Druid states object.
     * @return the object
     */
    @GetMapping("/states")
    public Object druidStates() {
        // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}