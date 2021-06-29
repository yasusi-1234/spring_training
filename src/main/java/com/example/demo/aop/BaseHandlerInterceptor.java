package com.example.demo.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.controller.base.AbstractRestController;

public abstract class BaseHandlerInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// コントローラーの動作前
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		// コントローラーの動作後
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		// 処理完了後
	}

	/**
	 * RestControllerかどうかを示す値をへんきゃくする
	 * 
	 * @param handler
	 * @return
	 */
	protected boolean isRestController(Object handler) {
		Object bean = getBean(handler, AbstractRestController.class);
		if (bean instanceof AbstractRestController) {
			return true;
		}
		return false;
	}

	/**
	 * 引数のオブジェクトが指定のクラスであるかどうかを示す真偽値を返却
	 * 
	 * @param obj
	 * @param clazz
	 * @return
	 */
	protected boolean isInstanceOf(Object obj, Class<?> clazz) {
		if (clazz.isAssignableFrom(obj.getClass())) {
			return true;
		}
		return false;
	}

	/**
	 * HandlerのBeanを返却する
	 * 
	 * @param <T>
	 * @param handler
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getBean(Object handler, Class<T> clazz) {
		if (handler != null && handler instanceof HandlerMethod) {
			Object hm = ((HandlerMethod) handler).getBean();
			if (clazz.isAssignableFrom(hm.getClass())) {
				return (T) hm;
			}
			return null;
		}
		return null;
	}
}
