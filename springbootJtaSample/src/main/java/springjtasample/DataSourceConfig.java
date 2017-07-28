package springjtasample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.SystemException;

@Configuration
public class DataSourceConfig {
	
	@Autowired
	private UserTransactionManager atomikosUserTransactionManager;
	
    @Bean(name = "primaryDataSource",destroyMethod = "close")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryDataSource",destroyMethod = "close")
    @Qualifier("secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("db1")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("db2")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    
    @Bean(name = "atomikosUserTransactionManager",destroyMethod = "close", initMethod = "init")
    public UserTransactionManager getUserTransactionManager(){
    	UserTransactionManager userTransactionManager = new UserTransactionManager();
    	userTransactionManager.setForceShutdown(true);
		return userTransactionManager;
    }
    
    @Bean(name = "atomikosUserTransaction")
    public UserTransactionImp getUserTransactionImp() throws SystemException{
    	UserTransactionImp userTransactionImp = new UserTransactionImp();
    	userTransactionImp.setTransactionTimeout(300);
		return userTransactionImp;
    }
    
    @Bean(name = "transactionManager")
    public JtaTransactionManager getJtaTransactionManager(){
    	JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
    	jtaTransactionManager.setTransactionManager(atomikosUserTransactionManager);
		return jtaTransactionManager;
    }
    
    @Bean
    @Qualifier("db1")
    @Primary
    public AtomikosDataSourceBean db3DataSourceBean() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("db3");
        atomikosDataSourceBean.setXaDataSourceClassName(
                "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        Properties properties = new Properties();
        properties.put("URL", "jdbc:mysql://172.28.128.4:3306/db1?useUnicode=true&amp;characterEncoding=utf-8");
        properties.put("user", "root");
        properties.put("password", "root");
        atomikosDataSourceBean.setXaProperties(properties);
        return atomikosDataSourceBean;
    }
    
    @Bean
    @Qualifier("db2")
    public AtomikosDataSourceBean db4DataSourceBean() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("db4");
        atomikosDataSourceBean.setXaDataSourceClassName(
                "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        Properties properties = new Properties();
        properties.put("URL", "jdbc:mysql://172.28.128.4:3306/db2?useUnicode=true&amp;characterEncoding=utf-8");
        properties.put("user", "root");
        properties.put("password", "root");
        atomikosDataSourceBean.setXaProperties(properties);
        return atomikosDataSourceBean;
    }
    
    
    
    
}