package com.excilys.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;

	
	
	
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		Properties prop = readProperties("config.properties");
		
		
	    auth.inMemoryAuthentication()
	        .withUser(prop.getProperty("user")).password(encoder().encode(prop.getProperty("password"))).roles("ADMIN")
	        .and()
	        .withUser("user").password(encoder().encode("userPass")).roles("USER");
	}
	 
	@Bean
	public PasswordEncoder  encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
	private static Properties readProperties(String configFileName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(configFileName);
		Properties prop = new Properties();
		prop.load(input);
		return prop;
	}
}
