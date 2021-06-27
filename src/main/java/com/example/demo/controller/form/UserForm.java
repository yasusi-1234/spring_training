package com.example.demo.controller.form;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserForm implements Serializable {

	private static final long serialVersionUID = 1L;

	Long id;

	// 名前
	@NotEmpty
	String firstName;

	// 苗字
	@NotEmpty
	String lastName;

	@NotEmpty
	String password;

	@NotEmpty
	String passwordConfirm;

	// メールアドレス
	@NotEmpty
	@Email
	String email;

	// 電話番号
	@Digits(fraction = 0, integer = 10)
	String tel;

	// 郵便番号
	String zip;

	// 住所
	String address;

	// 添付ファイル
//    @ContentType(allowed = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE })
//    transient MultipartFile userImage; // serializableではないのでtransientにする

}
