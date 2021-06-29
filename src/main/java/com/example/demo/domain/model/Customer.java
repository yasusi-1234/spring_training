package com.example.demo.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	// 名前
	String firstName;

	// 苗字
	String lastName;

	String password;

	// メールアドレス
	String email;

	// 電話番号
	String tel;

	// 郵便番号
	String zip;

	// 住所
	String address;

}
