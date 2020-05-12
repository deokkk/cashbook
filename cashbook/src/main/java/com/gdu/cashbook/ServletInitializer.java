package com.gdu.cashbook;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*
 * servlet API
 * 1. servlet : 요청 처리
 * 2. filter : 요청 전후 처리
 * 3. listener : 이벤트 반응 처리
*/

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CashbookApplication.class);
	}

}
