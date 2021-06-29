package com.example.demo.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

public class ExcelView extends AbstractXlsxView {

	protected String filename;

	protected Collection<?> data;

	protected Callback callback;

	/**
	 * コンストラクタ
	 * 
	 * @param filename
	 * @param data
	 * @param callback
	 */
	public ExcelView(String filename, Collection<?> data, Callback callback) {
		setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=Windows-31J;");
		this.filename = filename;
		this.data = data;
		this.callback = callback;
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String encodeFilename = encodeUTF8(filename);
		String contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodeFilename);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

		// Excelブックを構築する
		callback.buildExcelWorkbook(model, data, workbook);
	}

	/**
	 * UTF-8でエンコードした文字列を返します。
	 * 
	 * @param filename
	 * @return
	 */
	private String encodeUTF8(String filename) {
		String encoded = null;

		try {
			encoded = URLEncoder.encode(filename, "UTF-8");
		} catch (UnsupportedEncodingException ignore) {
			// should never happens
		}

		return encoded;
	}

	public interface Callback {
		/**
		 * Excelブックを構築します。
		 *
		 * @param model
		 * @param data
		 * @param workbook
		 */
		void buildExcelWorkbook(Map<String, Object> model, Collection<?> data, Workbook workbook);
	}

}
