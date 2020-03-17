package com.my.blog.website.config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.my.blog.website.constant.WebConst;
import com.my.blog.website.modal.Vo.OptionVo;
import com.my.blog.website.service.IOptionService;

@Component
public class CacheApplicationRunner implements ApplicationRunner{

	@Autowired
	private IOptionService optionService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<OptionVo> options = optionService.getOptions(WebConst.SOCIAL_PREFIX);
		options.forEach(x -> {
			WebConst.initConfig.put(x.getName().replace(WebConst.SOCIAL_PREFIX, StringUtils.EMPTY), x.getValue());
		});
	}

}
