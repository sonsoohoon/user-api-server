package com.org.bithumb.apiserver.util;

public enum ERR_CODE {
    OK(200),
    WEEK_PASSWORD(-30001),
    INVALID_PARAMETER(-30002),
    INVALID_EMAIL_FORMAT(-30003),
    MISSING_PARAMETER(-30004),
    NOT_AUTHONTICATION(-30005),
    FILTER_ERROR(-30006),
    ALREADY_EXIST_USER(-30007),
    NOT_FOUND_USER(-30008),
    INVALID_PASSWORD(-30009),
    NO_PERMISSION_USER(30011)
    ;

    final private int code;

    private ERR_CODE(int code) {
        // enum에서 생성자 같은 역할
        this.code = code;
    }
    public int getErrCode() { // 문자를 받아오는 함수
         return code;
    }
}
