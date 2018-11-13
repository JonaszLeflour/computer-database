package com.excilys.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Jonasz Leflour Spring configuration
 */
@Configuration
@ComponentScan(basePackages ="com.excilys.cdb.persistence")
public class DAOConfig extends AnnotationConfigApplicationContext{
	static DataSource source = null;
	
	/**
	 * @return data source
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@Bean
	public static DataSource getDataSource() throws IOException, ClassNotFoundException {
		if(source != null) {
			return source;
		}
		Class.forName("com.mysql.cj.jdbc.Driver");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("config.properties");
		Properties prop = new Properties();
		HikariConfig config = new HikariConfig();
		prop.load(input);

		config.setJdbcUrl(prop.getProperty("database"));
		config.setUsername(prop.getProperty("user"));
		config.setPassword(prop.getProperty("password"));

		source = new HikariDataSource(config);
		return source;
	}

	/**
	 * @return datasource transaction manager for HikariDB datasource
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@Bean
	public static DataSourceTransactionManager getManager() throws IOException, ClassNotFoundException {
		DataSourceTransactionManager manager = new DataSourceTransactionManager();
		manager.setDataSource(getDataSource());
		return manager;
	}
}
