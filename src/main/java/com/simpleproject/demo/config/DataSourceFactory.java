package com.simpleproject.demo.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


//connection pool


public class DataSourceFactory {
    private static final HikariDataSource dataSource;


    static {
        try {
            Properties props = new Properties();
            InputStream input = DataSourceFactory.class.getClassLoader().getResourceAsStream("application.properties");
            try {

                if (input == null) throw new RuntimeException("application.properties not found!");
                props.load(input);
                if(input == null) throw new RuntimeException("application.properties not found!");
            } catch (IOException e) {
                throw new RuntimeException("Props were not loaded!", e);
            }


            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));
            try {

                config.setMaximumPoolSize(Integer.parseInt(props.getProperty("pool.maximumpoolsize")));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Maximum Pool Size definition is not correct!", e);
            }

            dataSource = new HikariDataSource(config);


        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }


    }


    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
