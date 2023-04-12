/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.rabbitMqConfig.dead_letter_delay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * The type Order info.
 */
@ApiModel("订单信息实体")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderInfo {
    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单类型")
    private String orderType;

    @ApiModelProperty("订单信息")
    private String orderMsg;

    @ApiModelProperty("订单创建日期")
    private Date createDate;
}
