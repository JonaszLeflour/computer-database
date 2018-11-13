package com.excilys.cdb.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Jonasz Leflour
 *
 */
public class WebInit implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.setInitParameter("spring.profiles.default", "dev");
		
		AnnotationConfigWebApplicationContext appContext =
			      new AnnotationConfigWebApplicationContext();
		appContext.setDisplayName("JavaTrainingCDB");
		appContext.register(DAOConfig.class);
		appContext.refresh();
		
		
		appContext.register(WebConfig.class);
		servletContext.addListener(new ContextLoaderListener(appContext));
	
		ServletRegistration.Dynamic dispatcher =
		        servletContext.addServlet("dispatcher", new DispatcherServlet(appContext));
		dispatcher.setLoadOnStartup(1);
	    dispatcher.addMapping("/");
	}

}
