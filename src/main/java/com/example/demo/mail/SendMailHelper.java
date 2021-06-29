package com.example.demo.mail;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SendMailHelper {

	private final JavaMailSender javaMailSender;

	@Autowired
	public SendMailHelper(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}

	/**
	 * メールを送信する
	 * 
	 * @param fromAddress
	 * @param toaddress
	 * @param subject
	 * @param body
	 */
	public void sendMail(String fromAddress, String toaddress, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromAddress);
		message.setTo(toaddress);
		message.setSubject(subject);
		message.setText(body);

		try {
			javaMailSender.send(message);
		} catch (MailException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定したテンプレートのメール本文を返します。
	 *
	 * @param template
	 * @param objects
	 * @return
	 */
	public String getMailBody(String template, Map<String, Object> params) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		Context context = new Context();
		if (params != null && !params.isEmpty()) {
			params.forEach(context::setVariable);
		}

		return templateEngine.process(template, context);
	}

	private ITemplateResolver templateResolver() {
		StringTemplateResolver resolver = new StringTemplateResolver();
		resolver.setTemplateMode("TEXT");
		resolver.setCheckExistence(false); // 安全を取ってキャッシュしない
		return resolver;
	}
}
