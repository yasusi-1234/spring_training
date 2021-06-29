package com.example.demo.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.view.AbstractView;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 * PDFビュー
 *
 */
public class PdfView extends AbstractView {

	protected String report;

	protected Collection<?> data;

	protected String filename;

	/**
	 * コンストラクタ
	 * 
	 * @param report
	 * @param data
	 * @param filename
	 */
	public PdfView(String report, Collection<?> data, String filename) {
		super();
		this.report = report;
		this.data = data;
		this.filename = filename;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// IEの場合はContent-Lengthヘッダが指定されていないとダウンロードが失敗するので、
		// サイズを取得するための一時的なバイト配列ストリームにコンテンツを書き出すようにする
		ByteArrayOutputStream baos = createTemporaryOutputStream();

		// 帳票レイアウト
		JasperReport report = loadReport();

		// データ設定
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(this.data);
		JasperPrint print = JasperFillManager.fillReport(report, model, dataSource);

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
		exporter.exportReport();

		// ファイル名に日本語を含めても文字化けしないようにUTF-8にエンコードする
		String encodeFilename = encodeUtf8(filename);
		String contentdisposition = String.format("atachment; filename*=UTF-8' '%s", encodeFilename);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentdisposition);

		writeToResponse(response, baos);
	}

	/**
	 * 帳票レイアウトを読み込む
	 * 
	 * @return
	 */
	protected final JasperReport loadReport() {
		ClassPathResource resource = new ClassPathResource(this.report);

		try {
			String filename = resource.getFilename();
			if (filename.endsWith(".jasper")) {
				try (InputStream is = resource.getInputStream()) {
					return (JasperReport) JRLoader.loadObject(is);
				}
			} else if (filename.endsWith(".jrxml")) {
				try (InputStream is = resource.getInputStream()) {
					JasperDesign design = JRXmlLoader.load(is);
					return JasperCompileManager.compileReport(design);
				}
			} else {
				throw new IllegalArgumentException(
						".jasper または .jrxml の帳票を指定してください。 [" + filename + "] must end in either ");
			}
		} catch (IOException ex) {
			throw new IllegalArgumentException("failed to load report." + resource, ex);
		} catch (JRException ex) {
			throw new IllegalArgumentException("failed to load report." + resource, ex);
		}
	}

	/**
	 * UTF-8でエンコードした文字列を返します。
	 * 
	 * @param filename
	 * @return
	 */
	private String encodeUtf8(String filename) {
		String encoded = null;
		try {
			encoded = URLEncoder.encode(filename, "UTF-8");
		} catch (UnsupportedEncodingException ignore) {
			// should never happens
		}
		return encoded;
	}

}
