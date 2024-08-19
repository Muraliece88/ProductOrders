package com.abn.nl.loader;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Data
@Component
public class DataLoader {
    @Value("${sql.path}")
    private String sqlCommands;
    private final JdbcTemplate jdbcTemplate;
    private final ConfigurableEnvironment environment;
    private final DiscoveryClient discoveryClient;
    private final String configImport="spring.config.import";
    private final String configUri="spring.cloud.config.uri";

    public DataLoader(JdbcTemplate jdbcTemplate, ConfigurableEnvironment environment, DiscoveryClient discoveryClient) {
        this.jdbcTemplate =jdbcTemplate;
        this.environment = environment;
        this.discoveryClient = discoveryClient;

    }

    @PostConstruct
    public void init()  {
        try{
            ClassPathResource classPathResource = new ClassPathResource(sqlCommands);
            BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8));
            String sqls=reader.lines().collect(Collectors.joining("\n"));
            jdbcTemplate.execute( sqls );
        }
        catch(IOException e){
           throw new RuntimeException("No file found to load data");
        }



}


            }


