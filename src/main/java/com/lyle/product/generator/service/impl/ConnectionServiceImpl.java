package com.lyle.product.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyle.product.generator.constants.CommonConstant;
import com.lyle.product.generator.exception.EncryptException;
import com.lyle.product.generator.mapper.ConnectionMapper;
import com.lyle.product.generator.model.Connection;
import com.lyle.product.generator.service.ConnectionService;
import com.lyle.product.generator.util.EncryptUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

@Service
public class ConnectionServiceImpl extends ServiceImpl<ConnectionMapper, Connection> implements ConnectionService {

    private static final String DEFAULT_AES_KEY = "fdsfd32s6a5232a6";

    @Override
    public boolean createConnection(Connection connection) throws EncryptException {
        if (null == connection.getPort()) {
            switch (connection.getDbType()) {
                case 1:
                    connection.setPort(CommonConstant.DEFAULT_PORT_MYSQL);
                    break;
                case 2:
                    connection.setPort(CommonConstant.DEFAULT_PORT_ORACLE);
                    break;
                case 3:
                    connection.setPort(CommonConstant.DEFAULT_PORT_SQL_SERVER);
                    break;
                case 4:
                    connection.setPort(CommonConstant.DEFAULT_PORT_SQL_MONGO);
                    break;
                default:
                    return false;
            }
        }
        if (null == connection.getConnectionName()) {
            connection.setConnectionName(connection.getHost());
        }
        if (null == connection.getDriver() && 1 == connection.getDbType()) {
            connection.setDriver("com.mysql.jdbc.Driver");
        }

        try {
            connection.setPassword(passwordEncrypt(connection.getPassword()));
        } catch (Exception e) {
            throw new EncryptException("加密异常");
        }

        return this.saveOrUpdate(connection);
    }

    public String passwordEncrypt(String password) {
        return EncryptUtils.parseByte2HexStr(EncryptUtils.encryptAES(password,DEFAULT_AES_KEY));
    }

    public String passwordDecrypt(String passwordEncrypted) throws GeneralSecurityException, UnsupportedEncodingException {
        return new String(EncryptUtils.decryptAES(EncryptUtils.parseHexStr2Byte(passwordEncrypted),DEFAULT_AES_KEY));
    }

}
