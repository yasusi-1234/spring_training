package com.example.demo.view;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.view.AbstractView;

import com.fasterxml.jackson.dataformat.csv.CsvGenerator.Feature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

import lombok.Setter;

public class CsvView extends AbstractView {

	protected static final CsvMapper csvMapper = createCsvMapper();

	protected Class<?> clazz;

	protected Collection<?> data;

	@Setter
	protected String filename;

	@Setter
	protected List<String> columns;

	/**
	 * コンストラクタ
	 * 
	 * @param clazz
	 * @param data
	 * @param filename
	 */
	public CsvView(Class<?> clazz, Collection<?> data, String filename) {
		setContentType("application/octet-stream; charset=Windows-31J;");
		this.clazz = clazz;
		this.data = data;
		this.filename = filename;
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ファイル名に日本語を含めても文字化けしないようにUTF-8にエンコードする
		String encodeFilename = encodeUTF8(filename);
		String contentdisposition = String.format("attachment; filename*=UTF-8''%s", encodeFilename);

		response.setContentType("application/octet-stream; charset=Windows-31J;");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentdisposition);

		// CSVヘッダをオブジェクトから作成する
		CsvSchema schema = csvMapper.schemaFor(clazz).withHeader();

		if (columns != null && !columns.isEmpty()) {
			// カラムが指定された場合は、スキーマを再構築する
			Builder builder = schema.rebuild().clearColumns();
			for (String column : columns) {
				builder.addColumn(column);
			}
			schema = builder.build();
		}

		// 書き出し
//		OutputStream outputStream = createTemporaryOutputStream(); // これだとだめです
		OutputStream out = response.getOutputStream(); // これを使うと良い
		System.out.println(data);
		System.out.println(schema.toString());
		try (Writer writer = new OutputStreamWriter(out, "Windows-31J")) {
//			System.out.println(csvMapper.writer(schema).writeValueAsString(data));

			csvMapper.writer(schema).writeValue(writer, data);
		}

	}

	/**
	 * CSVマッパーを作成する
	 * 
	 * @return
	 */
	static CsvMapper createCsvMapper() {
		CsvMapper mapper = new CsvMapper();
		mapper.configure(Feature.ALWAYS_QUOTE_STRINGS, true);
		mapper.findAndRegisterModules();
		return mapper;
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

}
