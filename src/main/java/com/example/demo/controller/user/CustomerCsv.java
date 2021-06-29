package com.example.demo.controller.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティーを無視してマッピング
@JsonPropertyOrder({ "ユーザーID", "苗字", "名前", "メールアドレス", "電話番号", "郵便番号", "住所" }) // CSVヘッダ順
@Setter
@Getter
public class CustomerCsv implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("ユーザーID")
	Long id;

	// 名前
	@JsonProperty("名前")
	String firstName;

	// 苗字
	@JsonProperty("苗字")
	String lastName;

	@JsonIgnore // CSV出力無し
	String password;

	// メールアドレス
	@JsonProperty("メールアドレス")
	String email;

	// 電話番号
	@JsonProperty("電話番号")
	String tel;

	// 郵便番号
	@JsonProperty("郵便番号")
	String zip;

	// 住所
	@JsonProperty("住所")
	String address;
}
