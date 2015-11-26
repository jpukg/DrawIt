package draw_it.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages = "draw_it.data")
@ComponentScan(basePackages = "draw_it")
@EnableTransactionManagement
public class DataSourceConfig {

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(100000);
        return resolver;
    }
    
	@Autowired
    @Bean
    public DataSource dataSource(Properties envProperties) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(envProperties.getProperty("jdbc.driverClassName"));
        config.setJdbcUrl(envProperties.getProperty("jdbc.url"));
        config.setUsername(envProperties.getProperty("jdbc.username"));
        config.setPassword(envProperties.getProperty("jdbc.password"));

        DataSource dataSource = new HikariDataSource(config);

        return dataSource;
    }

	
    @Bean
    public Properties envProperties() {
        Properties prop = new Properties();
        String propFileName = "application.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        try {
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            System.out.println("Properties Exception!");
        }
        return prop;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       Properties envProperties) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("draw_it.data");

        Properties jpaProperties = new Properties();

        jpaProperties.put("hibernate.dialect", envProperties.getProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.hbm2ddl.auto",
                envProperties.getProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.ejb.naming_strategy",
                envProperties.getProperty("hibernate.ejb.naming_strategy"));
        // Not necessary in the deploy version.
        jpaProperties.put("hibernate.show_sql",
                envProperties.getProperty("hibernate.show_sql"));
        jpaProperties.put("hibernate.format_sql",
                envProperties.getProperty("hibernate.format_sql"));
        jpaProperties.put("hibernate.hbm2ddl.import_files",
                envProperties.get("hibernate.hbm2ddl.import_files"));
        jpaProperties.put("hibernate.hbm2ddl.import_files_sql_extractor",
                envProperties.get("hibernate.hbm2ddl.import_files_sql_extractor"));

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
