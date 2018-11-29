package com.excilys.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
//import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Jonasz Leflour Spring configuration
 */
@Configuration
@ComponentScan(basePackages = { "com.excilys.cdb.persistence", "com.excilys.cdb.service", "com.excilys.cdb.controller",
		"com.excilys.cdb.dto", "com.excilys.cdb.model" })
public class RootConfig extends AnnotationConfigApplicationContext {
	private static SessionFactory sessionFactory = null;
	private static Properties readProperties(String configFileName) throws ClassNotFoundException, IOException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(configFileName);
		Properties prop = new Properties();
		prop.load(input);
		return prop;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
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
						.addAnnotatedClass(com.excilys.cdb.model.Computer.class)
						.addAnnotatedClass(com.excilys.cdb.model.Company.class)
						.addAnnotatedClass(com.excilys.cdb.model.User.class)
						.addAnnotatedClass(com.excilys.cdb.model.Role.class)
						.addAnnotatedClass(com.excilys.cdb.model.QComputer.class)
						.addAnnotatedClass(com.excilys.cdb.model.QCompany.class)
						.addAnnotatedClass(com.excilys.cdb.model.QUser.class)
						.addAnnotatedClass(com.excilys.cdb.model.QRole.class)
					.buildMetadata()
					.buildSessionFactory();
		}
		return sessionFactory;
	}
}
