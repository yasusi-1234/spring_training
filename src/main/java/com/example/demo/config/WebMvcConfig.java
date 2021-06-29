package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.aop.LoggingFunctionNameInterceptor;
import com.example.demo.aop.RequestTrackingInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private MessageSource messageSource;

	/**
	 * LocalValidatorFactoryBeanのsetValidationMessageSourceで
	 * バリデーションメッセージをValidationMessages.propertiesからSpringの
	 * MessageSource(messages.properties)に上書きする
	 * 
	 * @return localValidatorFactoryBean
	 */
	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		return localValidatorFactoryBean;
	}

	@Override
	public Validator getValidator() {
		return validator();
	}

	@Bean
	public ModelMapper modelmapper() {
		return new ModelMapper();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggingFunctionNameInterceptor());
		registry.addInterceptor(requestTrackingInterceptor());
	}

	@Bean
	public LoggingFunctionNameInterceptor loggingFunctionNameInterceptor() {
		return new LoggingFunctionNameInterceptor();
	}

	@Bean
	public RequestTrackingInterceptor requestTrackingInterceptor() {
		return new RequestTrackingInterceptor();
	}

}
