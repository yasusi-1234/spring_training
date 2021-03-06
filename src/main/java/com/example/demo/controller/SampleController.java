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

	// ????????????????????????????????????????????????????????????
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
	 * PDF??????????????????
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping(path = "download/{filename:.+\\.pdf}")
	public ModelAndView downloadPdf(@PathVariable String filename) {
		// ????????????
		List<Customer> customers = customerService.findAllCustomer();
		// ?????????????????????????????????????????????????????????????????????????????????
		PdfView view = new PdfView("reports/users.jrxml", customers, filename);
		return new ModelAndView(view);
	}

	/**
	 * CSV??????????????????
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping(path = "download/{filename:.+\\.csv}")
	public ModelAndView downloadCsv(@PathVariable String filename) {
		// ????????????
		List<Customer> customers = customerService.findAllCustomer();

		// ????????????
		java.lang.reflect.Type targetListType = new TypeToken<List<CustomerCsv>>() {
		}.getType();
		List<CustomerCsv> csvList = modelMapper.map(customers, targetListType);
		// CSV????????????????????????????????????????????????????????????
		CsvView view = new CsvView(CustomerCsv.class, csvList, filename);

		return new ModelAndView(view);
	}

	/**
	 * Excel??????????????????
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping(path = "download/{filename:.+\\.xlsx}")
	public ModelAndView downloadExcel(@PathVariable String filename) {
		// ????????????
		List<Customer> customers = customerService.findAllCustomer();
		ExcelView view = new ExcelView(filename, customers, new CustomerExcel());
		return new ModelAndView(view);
	}

	@GetMapping("sendmail1")
	public String sendMail1(Model model) {
		model.addAttribute("customers", customerService.findAllCustomer());
		sendMailHelper.sendMail("test@co.jp", "?????????????????????????????????", "hello", "World!!");
		return "sample";
	}

	@GetMapping("sendmail2")
	public String sendMail2(Model model) {
		model.addAttribute("customers", customerService.findAllCustomer());
		String template = "[[${name}]]???????????? [[${message}]]";
		Map<String, Object> map = new HashMap<>();
		map.put("name", "?????? ??????");
		map.put("message", "??????????????????");
		String mailBody = sendMailHelper.getMailBody(template, map);
		sendMailHelper.sendMail("test@co.jp", "?????????????????????????????????", "Hello!!!", mailBody);
		return "sample";
	}
}
