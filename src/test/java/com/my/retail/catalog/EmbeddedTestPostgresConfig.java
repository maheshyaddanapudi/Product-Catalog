package com.my.retail.catalog;

import com.my.retail.catalog.constants.Constants;
import com.my.retail.catalog.db.repositories.PriceRepository;
import com.my.retail.catalog.db.repositories.ProductRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.flapdoodle.embed.process.runtime.Network;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.lang.String.format;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = {ProductRepository.class, PriceRepository.class})
@Profile(Constants.TEST)
public class EmbeddedTestPostgresConfig {
    private static final List<String> DEFAULT_ADDITIONAL_INIT_DB_PARAMS = Arrays
            .asList("--nosync", "--locale=en_US.UTF-8");

    /**
     * @param config the PostgresConfig configuration which will be used to get the needed host, port..
     * @return the created DB datasource
     */
    @Bean
    @DependsOn("postgresProcess")
    public DataSource dataSource(PostgresConfig config) {

        final Properties dataSourceProperties = new Properties();

        dataSourceProperties.setProperty(Constants.POOL_NAME, Constants.CATALOG_DB_POOL_NAME);
        dataSourceProperties.setProperty(Constants.MAX_LIFETIME, String.valueOf(Duration.ofMinutes(15).toMillis()));
        dataSourceProperties.setProperty(Constants.DRIVER_CLASS_NAME, Constants.POSTGRES_DRIVER);
        dataSourceProperties.setProperty("jdbcUrl", format("jdbc:postgresql://%s:%s/%s", config.net().host(), config.net().port(), config.storage().dbName()));
        dataSourceProperties.setProperty("username", config.credentials().username());
        dataSourceProperties.setProperty("password", config.credentials().password());
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

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setDataSource(dataSource);
        // set the packages to scan , it can be useful if you have big project and you just need to local partial entities for testing
        lcemfb.setPackagesToScan("com.my.retail.catalog.db.entities", "com.my.retail.catalog.db.repositories");
        HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
        lcemfb.setJpaVendorAdapter(va);
        lcemfb.setJpaProperties(getHibernateProperties());
        lcemfb.afterPropertiesSet();
        return lcemfb;

    }
    /**
     * @param localContainerEntityManagerFactoryBean
     * @return the JPA transaction manager
     */
    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());

        return transactionManager;
    }
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    /**
     * @return the hibernate properties
     */
    private Properties getHibernateProperties() {
        Properties ps = new Properties();
        ps.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        ps.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        ps.put("hibernate.hbm2ddl.auto", "update");
        ps.put("hibernate.connection.characterEncoding", "UTF-8");
        ps.put("hibernate.connection.charSet", "UTF-8");

        ps.put(AvailableSettings.FORMAT_SQL, "true");
        ps.put(AvailableSettings.SHOW_SQL, "true");
        return ps;
    }

    /**
     * @return PostgresConfig that contains embedded db configuration like user name , password
     * @throws IOException
     */
    @Bean
    public PostgresConfig postgresConfig(@Value("${spring.datasource.name:test}") String databaseName,
                                         @Value("${spring.datasource.username:catalog}") String datasourceUsername,
                                         @Value("${spring.datasource.password:Catalog@1234}") String datasourcePassword) throws IOException {
        // make it readable from configuration source file or system , it is hard coded here for explanation purpose only
        final PostgresConfig postgresConfig = new PostgresConfig(Version.V9_6_8,
                new AbstractPostgresConfig.Net("localhost", Network.getFreeServerPort()),
                new AbstractPostgresConfig.Storage(databaseName),
                new AbstractPostgresConfig.Timeout(),
                new AbstractPostgresConfig.Credentials(datasourceUsername, datasourcePassword)
        );

        postgresConfig.getAdditionalInitDbParams().addAll(DEFAULT_ADDITIONAL_INIT_DB_PARAMS);

        return postgresConfig;
    }

    /**
     * @param config the PostgresConfig configuration to use to start Postgres db process
     * @return PostgresProcess , the started db process
     * @throws IOException
     */
    @Bean(destroyMethod = "stop", name = "postgresProcess")
    public PostgresProcess postgresProcess(PostgresConfig config) throws IOException {
        PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter.getDefaultInstance();
        PostgresExecutable exec = runtime.prepare(config);
        PostgresProcess process = exec.start();
        return process;
    }
}
