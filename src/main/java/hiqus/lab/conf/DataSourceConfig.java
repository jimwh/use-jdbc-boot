package hiqus.lab.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@PropertySources({@PropertySource("classpath:${env}.db.properties")})
public class DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public DataSource primaryDataSource() throws SQLException {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(env.getProperty("primary.db.driverClassName"));
        dataSource.setUrl(env.getProperty("primary.db.url"));
        dataSource.setUsername(env.getProperty("primary.db.username"));
        dataSource.setPassword(env.getProperty("primary.db.password"));
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(10);
        dataSource.setMaxIdle(3);
        dataSource.setMinIdle(1);
        return dataSource;
    }

    @Bean
    public TransactionAwareDataSourceProxy primaryTransactionAwareDataSource() throws SQLException {
        return new TransactionAwareDataSourceProxy(primaryDataSource());
    }

    @Bean
    public DataSourceTransactionManager primaryTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(primaryTransactionAwareDataSource());
    }

    @Bean
    public JdbcTemplate primaryJdbcTemplate() throws SQLException {
        return new JdbcTemplate(primaryTransactionAwareDataSource());
    }

}
