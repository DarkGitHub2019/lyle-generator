package com.lyle.product.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyle.product.generator.model.Connection;
import com.lyle.product.generator.model.Table;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public interface GeneratorService extends IService<Connection> {

    JdbcTemplate createJdbcTemplateByConnectionId(Long connectionId) throws GeneralSecurityException, UnsupportedEncodingException;

    JdbcTemplate getJdbcTemplateByConnectionId(Long connectionId) throws GeneralSecurityException, UnsupportedEncodingException;

    byte [] generatorFiles(Long connectionId, String schema,Long configurationId,List<Table> tables) throws GeneralSecurityException, UnsupportedEncodingException;
}
