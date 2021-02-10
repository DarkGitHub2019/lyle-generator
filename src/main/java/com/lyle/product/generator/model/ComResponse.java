package com.lyle.product.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComResponse<T> {

    T content;
    Integer code;
    String message;

    public ComResponse<T> success(T content, String message) {
        return new ComResponse<T>(content, 1, message);
    }

    public ComResponse<T> failed(T content, String message) {
        return new ComResponse<T>(content, -1, message);
    }

    public ComResponse<T> exception(T content, String message) {
        return new ComResponse<T>(content, 2, message);
    }

    public ComResponse<T> noConnection(T content, String message) {
        return new ComResponse<T>(content, 3, message);
    }

    public ComResponse<T> noUser(T content, String message) {
        return new ComResponse<T>(content, 3, message);
    }


    public ComResponse<T> success(T content) {
        return new ComResponse<T>(content, 1);
    }

    public ComResponse<T> failed(T content) {
        return new ComResponse<T>(content, -1);
    }

    public ComResponse<T> exception(T content) {
        return new ComResponse<T>(content, 2);
    }

    public ComResponse<T> noConnection(T content) {
        return new ComResponse<T>(null, 3, "没有该连接");
    }

    public ComResponse<T> noUser() {
        return new ComResponse<T>(null, 4, "没有该用户");
    }

    public ComResponse(T content, Integer code) {
        this.content = content;
        this.code = code;
    }
}
