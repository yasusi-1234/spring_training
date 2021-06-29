package com.example.demo.controller.form.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractValidator<T> implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate(Object target, Errors errors) {
		try {
			boolean hasErrors = errors.hasErrors();
			if (!hasErrors || passThruBeanValidation(hasErrors)) {
				// 各機能で実装しているバリデーションを実行する
				doValidated((T) target, errors);
			}
		} catch (RuntimeException e) {
			log.error("validate error", e);
			throw e;
		}

	}

	protected abstract void doValidated(final T form, final Errors errors);

	/**
	 * 相関チェックバリデーションを実施するかどうかを示す値を返します。<br />
	 * デフォルトは、JSR-303バリデーションでエラーがあった場合は相関チェックを実施しません。
	 * 
	 * @return
	 */
	protected boolean passThruBeanValidation(boolean hasErrors) {
		return false;
	}

}
