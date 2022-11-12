package com.data.model;

public enum ResultCode {

    SUCCESS(200, "操作成功！"),
    UNAUTHENTICATED(403, "拒绝访问"),
    UNAUTHORISE(401, "权限不足"),
    SERVER_ERROR(500, "抱歉，系统繁忙，请稍后重试！");

    // 操作代码
    int code;
    // 提示信息
    String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
