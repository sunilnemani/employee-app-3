/*
*
*N Sunil 
*
*/

package com.sunil.employee.config.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sunil.employee.utils.BaseResponseDTO;

@FeignClient()
public interface AddressClient
{
	
	@GetMapping(value = "/address/getAddressByEmpNo", produces = MediaType.APPLICATION_JSON_VALUE)
	BaseResponseDTO getAddressByEmpNo(@RequestParam(name = "empNo") String empNo);

}
