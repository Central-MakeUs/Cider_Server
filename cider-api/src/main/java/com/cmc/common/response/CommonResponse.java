package com.cmc.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResponse<T> {

    private String message;
    private T data;

    private CommonResponse(String message) {
        this.message = message;
    }

    private CommonResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static CommonResponse<?> from(String message) {

        return new CommonResponse<>(message);
    }

    public static <T> CommonResponse<T> of(String message, T data) {

        return new CommonResponse<T>(message, data);
    }
}
