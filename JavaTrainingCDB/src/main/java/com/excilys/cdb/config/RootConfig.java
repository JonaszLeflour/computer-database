package com.excilys.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
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
		"com.excilys.cdb.dto" })
public class RootConfig extends AnnotationConfigApplicationContext {
	private static DataSource source = null;
	private static StandardServiceRegistry registry = null;
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

	@Bean
	static SessionFactory getSessionFactory() throws ClassNotFoundException, IOException {
		if (sessionFactory == null) {
			// Create registry builder
			StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

			// Hibernate settings equivalent to hibernate.cfg.xml's properties
			Map<String, String> settings = new HashMap<>();
			Properties prop = readProperties("config.properties");

			settings.put(Environment.DRIVER, "org.postgresql.Driver");
			settings.put(Environment.URL, prop.getProperty("database"));
			settings.put(Environment.USER, prop.getProperty("user"));
			settings.put(Environment.PASS, prop.getProperty("password"));
			settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL9Dialect");

			registryBuilder.applySettings(settings);
			registry = registryBuilder.build();
			MetadataSources sources = new MetadataSources(registry);
			Metadata metadata = sources.getMetadataBuilder().build();
			sessionFactory = metadata.getSessionFactoryBuilder().build();

		}
		return sessionFactory;
	}
}
