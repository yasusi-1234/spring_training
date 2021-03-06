package com.example.demo.aop;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.MDC;

import com.example.demo.common.XORShiftRandom;

import lombok.extern.slf4j.Slf4j;

/**
 * 処理時間をLogに出力する
 * 
 * @author yasuyasu
 *
 */
@Slf4j
public class RequestTrackingInterceptor extends BaseHandlerInterceptor {

	private static final ThreadLocal<Long> startTimeHolder = new ThreadLocal<>();

	private static final String HEADER_X_TRACK_ID = "X-Track-Id";

	// 乱数生成器
	private final XORShiftRandom random = new XORShiftRandom();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// コントローラーの前処理
		// 現在時刻を記録
		Long beforeNanoSec = System.nanoTime();
		startTimeHolder.set(beforeNanoSec);

		// トラッキングID
		String trackId = getTrackId(request);
		MDC.put(HEADER_X_TRACK_ID, trackId);
		response.setHeader(HEADER_X_TRACK_ID, trackId);

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 処理完了後
		Long beforeSec = startTimeHolder.get();

		if (beforeSec == null) {
			return;
		}

		Long elapsedNanoSec = System.nanoTime() - beforeSec;
		Long elapsedMilliSec = NANOSECONDS.toMillis(elapsedNanoSec);
		log.info("path={}, method={}, Elapsed={}ms.", request.getRequestURI(), request.getMethod(), elapsedMilliSec);
		// 破棄
		startTimeHolder.remove();
	}

	/**
	 * トラッキングIDを取得する
	 * 
	 * @param request
	 * @return
	 */
	private String getTrackId(HttpServletRequest request) {
		String trackId = request.getHeader(HEADER_X_TRACK_ID);
		if (trackId == null) {
			int seed = Integer.MAX_VALUE;
			trackId = String.valueOf(random.nextInt(seed));
		}
		return trackId;
	}
}
