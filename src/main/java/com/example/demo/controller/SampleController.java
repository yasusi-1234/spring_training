package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.controller.form.UserForm;
import com.example.demo.controller.form.validator.UserFormValidator;
import com.example.demo.controller.user.CustomerCsv;
import com.example.demo.domain.model.Customer;
import com.example.demo.domain.service.CustomerService;
import com.example.demo.mail.SendMailHelper;
import com.example.demo.view.CsvView;
import com.example.demo.view.CustomerExcel;
import com.example.demo.view.ExcelView;
import com.example.demo.view.PdfView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("sample")
public class SampleController {

	private final CustomerService customerService;

	private final UserFormValidator userFormValidator;

	private final ModelMapper modelMapper;

	private final SendMailHelper sendMailHelper;

	@Autowired
	public SampleController(CustomerService customerService, UserFormValidator userFormValidator,
			ModelMapper modelmapper, SendMailHelper sendMailHelper) {
		super();
		this.customerService = customerService;
		this.userFormValidator = userFormValidator;
		this.modelMapper = modelmapper;
		this.sendMailHelper = sendMailHelper;
	}

	@ModelAttribute
	public UserForm userForm() {
		return new UserForm();
	}

	// 相関チェックを行うためのバリデータを追加
	@InitBinder("userForm")
	public void validatorBinder(WebDataBinder binder) {
		binder.addValidators(userFormValidator);
	}

	@GetMapping
	public String getSample(Model model, @ModelAttribute UserForm userForm) {
		model.addAttribute("customers", customerService.findAllCustomer());
		return "sample";
	}

	@PostMapping("user")
	public String postSample(@Validated @ModelAttribute UserForm userForm, BindingResult result, Model model) {
		model.addAttribute("customers", customerService.findAllCustomer());
		log.info(userForm.toString());
		if (result.hasErrors()) {
			return "sample";
		}
		Customer customer = modelMapper.map(userForm, Customer.class);
		log.info(customer.toString());
		return "sample";
	}

	/**
	 * PDFダウンロード
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping(path = "download/{filename:.+\\.pdf}")
	public ModelAndView downloadPdf(@PathVariable String filename) {
		// 全件取得
		List<Customer> customers = customerService.findAllCustomer();
		// 帳票レイアウト、データ、ダウンロード時のファイル名指定
		PdfView view = new PdfView("reports/users.jrxml", customers, filename);
		return new ModelAndView(view);
	}

	/**
	 * CSVダウンロード
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping(path = "download/{filename:.+\\.csv}")
	public ModelAndView downloadCsv(@PathVariable String filename) {
		// 全件取得
		List<Customer> customers = customerService.findAllCustomer();

		// 詰め替え
		java.lang.reflect.Type targetListType = new TypeToken<List<CustomerCsv>>() {
		}.getType();
		List<CustomerCsv> csvList = modelMapper.map(customers, targetListType);
		// CSVスキーマクラス、データ、ファイル名を指定
		CsvView view = new CsvView(CustomerCsv.class, csvList, filename);

		return new ModelAndView(view);
	}

	/**
	 * Excelダウンロード
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping(path = "download/{filename:.+\\.xlsx}")
	public ModelAndView downloadExcel(@PathVariable String filename) {
		// 全件取得
		List<Customer> customers = customerService.findAllCustomer();
		ExcelView view = new ExcelView(filename, customers, new CustomerExcel());
		return new ModelAndView(view);
	}

	@GetMapping("sendmail1")
	public String sendMail1(Model model) {
		model.addAttribute("customers", customerService.findAllCustomer());
		sendMailHelper.sendMail("test@co.jp", "送り先のメールアドレス", "hello", "World!!");
		return "sample";
	}

	@GetMapping("sendmail2")
	public String sendMail2(Model model) {
		model.addAttribute("customers", customerService.findAllCustomer());
		String template = "[[${name}]]さん！！ [[${message}]]";
		Map<String, Object> map = new HashMap<>();
		map.put("name", "山田 太郎");
		map.put("message", "こんにちは！");
		String mailBody = sendMailHelper.getMailBody(template, map);
		sendMailHelper.sendMail("test@co.jp", "送り先のメールアドレス", "Hello!!!", mailBody);
		return "sample";
	}
}
