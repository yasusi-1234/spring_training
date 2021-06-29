package com.example.demo.view;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.example.demo.domain.model.Customer;
import com.example.demo.view.ExcelView.Callback;

public class CustomerExcel implements Callback {

	@Override
	public void buildExcelWorkbook(Map<String, Object> model, Collection<?> data, Workbook workbook) {
		// シートを作成する
		Sheet sheet = workbook.createSheet("ユーザー");
		sheet.setDefaultColumnWidth(30);

		// フォント
		Font font = workbook.createFont();
		font.setFontName("メイリオ");
		font.setBold(true);
		font.setColor(HSSFColorPredefined.WHITE.getIndex());

		// ヘッダーのスタイル
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColorPredefined.DARK_GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("苗字");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("名前");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("メールアドレス");
		header.getCell(2).setCellStyle(style);

		// 明細
		@SuppressWarnings("unchecked")
		List<Customer> users = (List<Customer>) data;

		int count = 1;
		for (Customer user : users) {
			Row userRow = sheet.createRow(count++);
			userRow.createCell(0).setCellValue(user.getFirstName());
			userRow.createCell(1).setCellValue(user.getLastName());
			userRow.createCell(2).setCellValue(user.getEmail());
		}
	}

}
