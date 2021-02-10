package com.lyle.product.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyle.product.generator.exception.EncryptException;
import com.lyle.product.generator.model.Connection;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

public interface ConnectionService extends IService<Connection> {
    boolean createConnection(Connection connection) throws EncryptException;

    String passwordEncrypt(String password) throws GeneralSecurityException, UnsupportedEncodingException;

    String passwordDecrypt(String password) throws GeneralSecurityException, UnsupportedEncodingException;
}
