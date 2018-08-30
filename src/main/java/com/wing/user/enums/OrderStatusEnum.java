package com.wing.user.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum {

    NEW(0),
    FINISHED(1),
    CANCEL(2);

    private Integer code;

    OrderStatusEnum(Integer code) {
        this.code = code;
    }
}
