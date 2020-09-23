package ru.job4j.accident.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * HbmConfig.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Configuration
@PropertySource("classpath:app.properties")
@EnableTransactionManagement
public class HbmConfig {

    @Bean
    public DataSource ds(@Value("${jdbc.driver}") String driver,
                         @Value("${jdbc.url}") String url,
                         @Value("${jdbc.username}") String username,
                         @Value("${jdbc.password}") String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(@Value("${hibernate.show_sql}") String sql,
                                                  @Value("${hibernate.dialect}") String dialect,
                                                  @Value("${hibernate.hbm2ddl.auto}") String createTables,
                                                  @Value("${hibernate.format_sql}") String formatSql,
                                                  @Value("${hibernate.use_sql_comments}") String comments,
                                                  DataSource ds) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(ds);
        sessionFactory.setPackagesToScan("ru.job4j.accident.model");
        Properties cfg = new Properties();
        cfg.setProperty("hibernate.show_sql", sql);
        cfg.setProperty("hibernate.dialect", dialect);
        cfg.setProperty("hibernate.hbm2ddl.auto", createTables);
        cfg.setProperty("hibernate.format_sql", formatSql);
        cfg.setProperty("hibernate.use_sql_comments", comments);
        sessionFactory.setHibernateProperties(cfg);
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager htx(SessionFactory sf) {
        HibernateTransactionManager tx = new HibernateTransactionManager();
        tx.setSessionFactory(sf);
        return tx;
    }
}