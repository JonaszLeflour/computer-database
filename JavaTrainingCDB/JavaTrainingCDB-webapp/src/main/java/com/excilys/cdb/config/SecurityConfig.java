package com.excilys.cdb.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.UserDAO;
import com.excilys.cdb.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	public PasswordEncoder  encoder() {
	    return new BCryptPasswordEncoder();
	}
	//@Autowired
	//private UserDetailsService userDetailsService;

	@Autowired
	UserService userService;
	
	@Autowired
	UserDAO userDAO;
	
	
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		Properties prop = readProperties("config.properties");
		for(User user :userDAO.getUsers()) {
			auth.inMemoryAuthentication()
			 .withUser(prop.getProperty(user.getName())).password(user.getPassword()).roles(user.getRole().getName());
		}
	}
	
	
	private static Properties readProperties(String configFileName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(configFileName);
		Properties prop = new Properties();
		prop.load(input);
		return prop;
	}
}
