package com.excilys.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
//import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
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
@ComponentScan(basePackages = { "com.excilys.cdb.persistence", "com.excilys.cdb.service", "com.excilys.cdb.controller",
		"com.excilys.cdb.dto", "com.excilys.cdb.model" })
public class RootConfig extends AnnotationConfigApplicationContext {
	@Autowired
	private static DataSource source = null;
	private static SessionFactory sessionFactory = null;

	private static Properties readProperties(String configFileName) throws ClassNotFoundException, IOException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(configFileName);
		Properties prop = new Properties();
		prop.load(input);
		return prop;
	}

	/**
	 * @return data source
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Bean
	public static DataSource getDataSource() throws IOException, ClassNotFoundException {
		if (source != null) {
			return source;
		}
		Properties prop = readProperties("config.properties");
		HikariConfig config = new HikariConfig();

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

	/**
	 * @return session factory
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Bean
	public static SessionFactory getSessionFactory() throws ClassNotFoundException, IOException {
		//!\\TODO: fix sessionfactory creation errors
		if (sessionFactory == null) {
			StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
			Map<String, String> settings = new HashMap<>();
			Properties prop = readProperties("config.properties");
			Class.forName("com.mysql.cj.jdbc.Driver");
			settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
			settings.put(Environment.URL, prop.getProperty("database"));
			settings.put(Environment.USER, prop.getProperty("user"));
			settings.put(Environment.PASS, prop.getProperty("password"));
			settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
			registryBuilder.applySettings(settings);
			sessionFactory = new MetadataSources(registryBuilder.build())
						.addPackage(Package.getPackage("com.excilys.cdb.model"))
						.addAnnotatedClass(org.hibernate.internal.SessionImpl.class)
						.addAnnotatedClass(com.excilys.cdb.model.Computer.class)
						.addAnnotatedClass(com.excilys.cdb.model.Company.class)
						.addAnnotatedClass(com.excilys.cdb.model.QComputer.class)
						.addAnnotatedClass(com.excilys.cdb.model.QCompany.class)
					.buildMetadata()
					.buildSessionFactory();
		}
		return sessionFactory;
	}
}
