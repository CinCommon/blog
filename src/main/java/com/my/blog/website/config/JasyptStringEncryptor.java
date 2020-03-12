package com.my.blog.website.config;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Configuration;

import com.my.blog.website.constant.WebConst;
import com.my.blog.website.utils.EncryptedUtil;

@Configuration
public class JasyptStringEncryptor implements StringEncryptor{
	
	@Override
	public String encrypt(String message) {
		return EncryptedUtil.encrypt(message, WebConst.GCM_SALT);
	}

	@Override
	public String decrypt(String encryptedMessage) {
		return EncryptedUtil.decrypt(encryptedMessage, WebConst.GCM_SALT);
	}
}