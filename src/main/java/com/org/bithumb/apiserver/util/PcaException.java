package com.org.bithumb.apiserver.util;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PcaException extends Exception{
    private final ERR_CODE errCode;
    public PcaException(String msg, ERR_CODE errcode){
        super(msg);
        errCode=errcode;
    }
    /*caException(String msg){
        this(msg, 0);
    }*/
    public int getErrCode(){
        return errCode.getErrCode();
    }
}

