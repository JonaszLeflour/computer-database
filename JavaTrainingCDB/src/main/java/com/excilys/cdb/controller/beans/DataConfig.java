package com.excilys.cdb.controller.beans;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Jonasz Leflour
 * Spring configuration
 */
@Configuration
public class DataConfig {
	/**
	 * @return data source
	 * @throws IOException
	 */
	@Bean
	public DataSource source() throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("config.properties");
		Properties prop = new Properties();
		HikariConfig config = new HikariConfig();
		prop.load(input);
		
		config.setJdbcUrl(prop.getProperty("database"));
		config.setUsername(prop.getProperty("user"));
		config.setPassword(prop.getProperty("password"));
		
		DataSource source = new HikariDataSource();
		return source;
	}
	
	/**
	 * @return datasource transaction manager for HikariDB datasource
	 * @throws IOException
	 */
	@Bean
	  public DataSourceTransactionManager manager() throws IOException{
		DataSourceTransactionManager manager = new DataSourceTransactionManager();
	    manager.setDataSource(source());
	    return manager;
	  }

}
