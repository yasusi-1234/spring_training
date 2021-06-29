package com.example.demo.controller.form.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.example.demo.controller.form.UserForm;

@Component
public class UserFormValidator extends AbstractValidator<UserForm> {

	@Override
	protected void doValidated(UserForm form, Errors errors) {
		// 確認用パスワードと突合わせる
		if (!Objects.equals(form.getPassword(), form.getPasswordConfirm())) {
			// 第一引数にfield名、第二引数にエラーコードを記載、messages.propertiesに記述してある
			errors.rejectValue("password", "users.unmatchPassword");
			errors.rejectValue("passwordConfirm", "users.unmatchPassword");
		}
	}

}
