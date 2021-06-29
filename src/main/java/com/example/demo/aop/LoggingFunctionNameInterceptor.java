package com.example.demo.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;

import com.example.demo.common.FunctionNameAware;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingFunctionNameInterceptor extends BaseHandlerInterceptor {

	private static final String MDC_FUNCTION_NAME = "FUNCTION_NAME";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// コントローラーの動作前
		FunctionNameAware fna = getBean(handler, FunctionNameAware.class);
		if (fna != null) {
			String functionName = fna.getFunctionName();
			MDC.put(MDC_FUNCTION_NAME, functionName);
		}
		return true;
	}
}
