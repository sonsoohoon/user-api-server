package com.org.bithumb.apiserver.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    /**
     * Comment : 정상적인 이메일 인지 검증.
     */
    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    public static boolean isValidPassword(String password) {
        String regExpNum = "[0-9]"; //숫자
        String regExpSymbol = "[!,@,#,^,&,*,(,)]"; //특수문자
        String regExpLowerCase = "[a-z]"; //소문자
        String regExpUpperCase = "[A-Z]"; //대문자
    // 정규표현식 컴파일
        Pattern patternNum = Pattern.compile(regExpNum);
        Pattern patternSymbol = Pattern.compile(regExpSymbol);
        Pattern patternLowerCase = Pattern.compile(regExpLowerCase);
        Pattern patternUpperCase = Pattern.compile(regExpUpperCase);

    // 문자 매칭
        Matcher matcherNum = patternNum.matcher(password);
        Matcher matcherSymbol = patternSymbol.matcher(password);
        Matcher matcherLowerCase = patternLowerCase.matcher(password);
        Matcher matcherUpperCase = patternUpperCase.matcher(password);

    // 매칭 결과 확인
        int passCount = 0;
        if (matcherNum.find()) passCount++;
        if (matcherSymbol.find()) passCount++;
        if (matcherLowerCase.find()) passCount++;
        if (matcherUpperCase.find()) passCount++;

        return passCount >= 3 && password.length() >= 12 ? true : false;
    }
}
