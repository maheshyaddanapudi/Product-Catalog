package com.my.retail.catalog.config.external.db;

import com.my.retail.catalog.constants.Constants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@Profile(Constants.PROFILE_EXTERNAL_POSTGRES_DB)
public class ExternalPostgresConfiguration {

    @Bean
    public DataSource dataSource(@Value("${spring.datasource.url:jdbc:postgresql://localhost:54131/catalog}") String url,
                                 @Value("${spring.datasource.username:catalog}") String datasourceUsername,
                                 @Value("${spring.datasource.password:Catalog@1234}") String datasourcePassword) {

        final Properties dataSourceProperties = new Properties();

        dataSourceProperties.setProperty(Constants.POOL_NAME, Constants.CATALOG_DB_POOL_NAME);
        dataSourceProperties.setProperty(Constants.MAX_LIFETIME, String.valueOf(Duration.ofMinutes(15).toMillis()));
        dataSourceProperties.setProperty(Constants.DRIVER_CLASS_NAME, Constants.POSTGRES_DRIVER);
        dataSourceProperties.setProperty("jdbcUrl", url);
        dataSourceProperties.setProperty("username", datasourceUsername);
        dataSourceProperties.setProperty("password", datasourcePassword);
        dataSourceProperties.setProperty(Constants.MAX_POOL_SIZE, Constants._100);
        dataSourceProperties.setProperty(Constants.MIN_IDLE, Constants._2);
        dataSourceProperties.setProperty(Constants.IDLE_TIMEOUT, String.valueOf(Duration.ofMinutes(10).toMillis()));
        dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.CACHE_PREP_STMTS,Constants.TRUE);
        dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.PREP_STMT_CACHE_SIZE, Constants._256);
        dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.PREP_STMT_CACHE_SQL_LIMIT, Constants._2048);
        dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.USER_SERVER_PREP_STMTS,Constants.TRUE);
        dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.USE_LEGACY_DATETIME_CODE,Constants.FALSE);
        dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.SERVER_TIMEZONE,Constants.UTC);
        dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.CONNECTION_COLLATION,Constants.utf8mb4_unicode_ci);
        dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.USE_SSL,Constants.FALSE);
        dataSourceProperties.setProperty(Constants.dataSource + Constants.DOT + Constants.AUTO_RECONNECT,Constants.TRUE);

        final HikariConfig hikariConfig = new HikariConfig(dataSourceProperties);

        final HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        return hikariDataSource;

    }
}
