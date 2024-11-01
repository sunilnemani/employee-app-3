/*
*
*N Sunil 
*
*/

package com.sunil.employee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunil.employee.dto.EmployeeDetailsDTO;
import com.sunil.employee.service.IEmployeeService;
import com.sunil.employee.utils.AppException;
import com.sunil.employee.utils.BaseResponseDTO;
import com.sunil.employee.utils.Commons;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController
{
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private IEmployeeService iEmployeeService;
	
	@GetMapping(value = "/getEmployeeById", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponseDTO getEmployeeById(@RequestParam(name = "id") Integer id)
	{
		logger.info("[EmployeeController][getEmployeeById]");
		BaseResponseDTO response = null;
		try
		{
			response = this.iEmployeeService.getEmployeeById(id);
		}
		catch (AppException e) 
		{
			logger.error("[EmployeeController][addEmployee]", e);
			response = Commons.getFailureResponse(BaseResponseDTO.FAILURE_STATUS, e.getErrMsg(), null);
		}
		catch (Exception e) 
		{
			logger.error("[EmployeeController][addEmployee]", e);
			response = Commons.getFailureResponse(BaseResponseDTO.FAILURE_STATUS, "Internal Error", null);
		}
		return response;
	}
	
	@GetMapping(value = "/getEmployeeByEmpNo", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponseDTO getEmployeeByEmpNo(@RequestParam(name = "empNo") String empNo)
	{
		logger.info("[EmployeeController][getEmployeeById]");
		BaseResponseDTO response = null;
		try
		{
			response = this.iEmployeeService.getEmployeeByEmpNo(empNo);
		}
		catch (AppException e) 
		{
			logger.error("[EmployeeController][addEmployee]", e);
			response = Commons.getFailureResponse(BaseResponseDTO.FAILURE_STATUS, e.getErrMsg(), null);
		}
		catch (Exception e) 
		{
			logger.error("[EmployeeController][addEmployee]", e);
			response = Commons.getFailureResponse(BaseResponseDTO.FAILURE_STATUS, "Internal Error", null);
		}
		logger.info("[EmployeeController][getEmployeeById] - response : "+response);
		return response;
	}
	
	
	
	@PostMapping(value = "/addEmployee", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponseDTO addEmployee(@RequestBody EmployeeDetailsDTO employeeDTO)
	{
		logger.info("[EmployeeController][addEmployee]");
		BaseResponseDTO response = null;
		try
		{
			response = this.iEmployeeService.addEmployee(employeeDTO);
		}
		catch (AppException e) 
		{
			logger.error("[EmployeeController][addEmployee]", e);
			response = Commons.getFailureResponse(BaseResponseDTO.FAILURE_STATUS, e.getErrMsg(), null);
		}
		catch (Exception e) 
		{
			logger.error("[EmployeeController][addEmployee]", e);
			response = Commons.getFailureResponse(BaseResponseDTO.FAILURE_STATUS, "Internal Error", null);
		}
		return response;
	}

}
