package com.my.blog.website;

import java.util.Arrays;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.html.HtmlRenderer;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.my.blog.website.config.JasyptStringEncryptor;
import com.my.blog.website.utils.SpringUtils;
import com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties;

@MapperScan("com.my.blog.website.dao")
@SpringBootApplication
@EnableTransactionManagement
public class CoreApplication extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
    	DruidDataSource dataSource = new DruidDataSource();
    	dataSource.setInitialSize(5);
    	dataSource.setMaxActive(15);
    	dataSource.setMinIdle(5);
    	dataSource.setTestWhileIdle(true);
    	dataSource.setQueryTimeout(30);
    	dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        DataSource dataSource = dataSource();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    @Bean
	public JasyptEncryptorConfigurationProperties jasyptEncryptorConfigurationProperties() {
		JasyptEncryptorConfigurationProperties jasyptEncryptorConfigurationProperties = new JasyptEncryptorConfigurationProperties();
		jasyptEncryptorConfigurationProperties.setBean("jasyptStringEncryptor");
		return jasyptEncryptorConfigurationProperties;
	}

	@Bean
	public JasyptStringEncryptor jasyptStringEncryptor() {
		return new JasyptStringEncryptor();
	}
	@Bean
	public Extension tablesExtension() {
		return TablesExtension.create();
	}
	@Bean
	public Parser parser() {
		return Parser.builder().extensions(Arrays.asList(tablesExtension())).build();
	}
	@Bean
	public Renderer render() {
		return HtmlRenderer.builder()
		        .extensions(Arrays.asList(tablesExtension()))
		        .build();
	}
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(CoreApplication.class, args);
        SpringUtils.init(ctx);
    }
}
