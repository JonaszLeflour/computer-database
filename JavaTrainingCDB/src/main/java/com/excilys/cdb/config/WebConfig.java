package com.excilys.cdb.config;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author Jonasz
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.excilys.cdb" })
public class WebConfig implements WebMvcConfigurer { 
	/**
	 * @return resolver
	 */
	@Bean
	public ViewResolver resolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	/**
	 * @return message source
	 */
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("messages");
		return source;
	}

	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {  
        // I tried these many combinations separately.

        ResourceHandlerRegistration resourceRegistration = registry.addResourceHandler("/resources/**");
        resourceRegistration.addResourceLocations("/resources/**");
        registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/**");
        registry.addResourceHandler("/img/**").addResourceLocations("/WEB-INF/img/**");
        registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/**");
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/resources/"); 
              // do the classpath works with the directory under webapp?
     }

}