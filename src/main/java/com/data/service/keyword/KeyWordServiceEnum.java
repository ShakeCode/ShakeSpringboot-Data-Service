package com.data.service.keyword;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Key word service enum.
 */
@AllArgsConstructor
@Getter
public enum KeyWordServiceEnum {

    /**
     * Black phone key word service enum.
     */
    BLACK_PHONE("blackPhone", "blackPhoneSerivce"),
    /**
     * Sensitive key word service enum.
     */
    SENSITIVE("sensitive", "sensitiveService");

    private String code;

    private String serviceName;
}
