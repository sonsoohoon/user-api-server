package com.org.bithumb.apiserver.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PctUserDto {
    private String uid;
    private String name;
    private LocalDateTime lastLoginTime;

    @Builder
    public PctUserDto(String uid, String name, LocalDateTime lastLoginTime) {
        this.uid = uid;
        this.name = name;
        this.lastLoginTime = lastLoginTime;
    }

}
