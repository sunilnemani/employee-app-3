/*
*
*N Sunil 
*
*/

package com.sunil.employee.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EmployeeBeanInitializer
{
	
	@Value("${GET_ADDRESS_APP_BASE_URL}") 
	private String getAddressAppBaseUrl;
	
//	@Bean
//	public RestTemplate getRestTemplate()
//	{
//		return new RestTemplate();
//	}
	
	@Bean
	public WebClient getWebClient()
	{
		return WebClient.builder().baseUrl(getAddressAppBaseUrl).build();
	}

}
