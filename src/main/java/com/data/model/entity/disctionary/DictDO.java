/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.model.entity.disctionary;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Dict do.
 */
@ApiModel(value = "DictDO", description = "字典数据实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DictDO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("键")
    private String code;

    @ApiModelProperty("键值")
    private String value;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createDate;

    /**
     * Instantiates a new Dict do.
     * @param code       the code
     * @param value      the value
     * @param createDate the create date
     */
    public DictDO(String code, String value, Date createDate) {
        this.code = code;
        this.value = value;
        this.createDate = createDate;
    }
}
