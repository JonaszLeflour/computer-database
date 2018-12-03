package com.excilys.cdb.config;

//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.excilys.cdb.model.Role;
import com.excilys.cdb.persistence.RoleDAO;
//import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.UserDAO;
import com.excilys.cdb.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@ComponentScan(basePackages= {"com.excilys.cdb.config"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	UserService userService;

	@Autowired
	UserDAO userDAO;

	@Autowired
	RoleDAO roleDAO;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		// Properties prop = readProperties("config.properties");
		auth.authenticationProvider(authenticationProvider(userDetailsService));

		/*
		 * for(User user :userDAO.getUsers()) { auth.inMemoryAuthentication()
		 * .withUser(user.getName()).password(user.getPassword()).roles(user.getRole().
		 * getName()); }
		 */
	}
	
	

	@Bean
	public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		 Role role1 = roleDAO.getRoleByName("admin"); 
		 Role role2 = roleDAO.getRoleByName("guest"); 
		 http.authorizeRequests()
		 .antMatchers("/login").permitAll()
		 .antMatchers("/dashboard").hasAnyRole(role1.getName(), role2.getName())
		 .antMatchers("/delete").hasRole(role1.getName())
		 .antMatchers("/editcomputer").hasRole(role1.getName())
		 .antMatchers("/addcomputer").hasRole(role1.getName())
		 .and().formLogin()
		 .defaultSuccessUrl("/dashboard", true)
		 .and().logout()
		 .logoutUrl("/logout")
		 .logoutSuccessUrl("/login").permitAll() .and().csrf().disable();
		 
		 /*http.authorizeRequests() .antMatchers("/login").permitAll()
		 .antMatchers("/dashboard").hasRole("admin")
		 .antMatchers("/editComputer").hasRole("admin")
		 .antMatchers("/addComputer").hasRole("admin") .and().formLogin()
		 .defaultSuccessUrl("/dashboard", true).and().logout()
		 .logoutSuccessUrl("/login").permitAll() .and().csrf().disable();
		 

		http.authorizeRequests().antMatchers("/login").permitAll();

		for (Role role : roleDAO.getRoles()) {
			if (role.isSelect()) {
				http.authorizeRequests().antMatchers("/dashboard").hasRole(role.getName());
			}
			if (role.isEdit()) {
				http.authorizeRequests().antMatchers("/editComputer").hasRole(role.getName());
			}
			if (role.isInsert()) {
				http.authorizeRequests().antMatchers("/addComputer").hasRole(role.getName());
			}
		}
		http.authorizeRequests().and().formLogin().defaultSuccessUrl("/dashboard", true).and().logout()
				.logoutSuccessUrl("/login").permitAll().and().csrf().disable();*/

	}

	/*
	 * private static Properties readProperties(String configFileName) throws
	 * ClassNotFoundException, IOException { ClassLoader classLoader =
	 * Thread.currentThread().getContextClassLoader(); InputStream input =
	 * classLoader.getResourceAsStream(configFileName); Properties prop = new
	 * Properties(); prop.load(input); return prop; }
	 */
}
