/*
*
*N Sunil 
*
*/

package com.sunil.employee.serviceImpl;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.sunil.employee.config.feignClient.AddressClient;
import com.sunil.employee.dao.IEmployeeDAO;
import com.sunil.employee.daoImpl.EmployeeRepo;
import com.sunil.employee.dto.AddressDetailsDTO;
import com.sunil.employee.dto.EmployeeDetailsDTO;
import com.sunil.employee.model.EmployeeDetails;
import com.sunil.employee.service.IEmployeeService;
import com.sunil.employee.utils.AppException;
import com.sunil.employee.utils.BaseResponseDTO;
import com.sunil.employee.utils.Commons;

import jakarta.transaction.Transactional;

@Service
public class EmployeeServiceImpl implements IEmployeeService
{
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	private static final Gson gson = new Gson();
	
	@Autowired
	private IEmployeeDAO iEmployeeDAO;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	private AddressClient addressClient;
	
//	@Autowired
//	private RestTemplate restTemplate;
	
//	@Value("${GET_ADDRESS_APP_BASE_URL}")
//	private String getAddressAppBaseUrl;
	
	@Value("${GET_ADDRESS_BY_EMP_NO_URL}")
	private String getAddressByEmpNoUrl;
	
	public EmployeeServiceImpl(@Value("${GET_ADDRESS_APP_BASE_URL}") String getAddressAppBaseUrl, RestTemplateBuilder builder)
	{
		logger.info("getAddressAppBaseUrl : "+getAddressAppBaseUrl);
		logger.info("getAddressByEmpNoUrl : "+getAddressByEmpNoUrl);
//		this.restTemplate = builder.rootUri(getAddressAppBaseUrl).build();
	}
	
	@Override
	public BaseResponseDTO getEmployeeById(Integer id)
	{
		BaseResponseDTO response = null;
		try
		{
			if(id!=null)
			{
				Optional<EmployeeDetails> optional = employeeRepo.findById(id);
				if(optional.isPresent())
				{
					EmployeeDetailsDTO employee = Commons.copyPropertiesWithNullCheck(EmployeeDetailsDTO.class, optional.get());
					response = Commons.getSuccessResponse(BaseResponseDTO.SUCCESS_STATUS, null, employee);
				}
				else
				{
					throw new AppException(BaseResponseDTO.FAILURE_STATUS, "Employee details not found");
				}
			}
			else
			{
				throw new AppException(BaseResponseDTO.FAILURE_STATUS, "Employee Id Cannot be Null");
			}
		}
		catch(AppException e)
		{
			logger.error("[EmployeeServiceImpl][getEmployeeById]",e);
			throw e;
		}
		catch(Exception e)
		{
			logger.error("[EmployeeServiceImpl][getEmployeeById]",e);
			throw e;
		}
		return response;
	}
	
	@Override
	public BaseResponseDTO getEmployeeByEmpNo(String empNo)
	{
		logger.info("[EmployeeServiceImpl][getEmployeeByEmpNo]");
		BaseResponseDTO response = null;
		try
		{
			if(!StringUtils.isBlank(empNo))
			{
				EmployeeDetailsDTO employee = this.iEmployeeDAO.getEmployeeByEmpNo(empNo);
				if(employee!=null)
				{
					
//					response = this.restTemplate.getForObject(getAddressByEmpNoUrl, BaseResponseDTO.class, empNo);
					
					//Using WebClient instead of RestTemplate
//					response = webClient.get().uri(getAddressByEmpNoUrl, empNo).retrieve().bodyToMono(BaseResponseDTO.class).block();
					
					//Using Open Fiegn Client
					response = addressClient.getAddressByEmpNo(empNo);
					
					AddressDetailsDTO address = gson.fromJson(gson.toJson(response.getData()) , AddressDetailsDTO.class);
					employee.setAddressDetails(address);
					response = Commons.getSuccessResponse(BaseResponseDTO.SUCCESS_STATUS, null, employee);
				}
				else
				{
					throw new AppException(BaseResponseDTO.FAILURE_STATUS, "Employee details not found");
				}
			}
			else
			{
				throw new AppException(BaseResponseDTO.FAILURE_STATUS, "Employee No Cannot be Null");
			}
		}
		catch(AppException e)
		{
			logger.error("[EmployeeServiceImpl][getEmployeeById]",e);
			throw e;
		}
		catch(Exception e)
		{
			logger.error("[EmployeeServiceImpl][getEmployeeById]",e);
			throw e;
		}
		return response;
	}
	
	@Override
	@Transactional
	public BaseResponseDTO addEmployee(EmployeeDetailsDTO newEmployeeDTO)
	{
		BaseResponseDTO response = null;
		logger.info("[EmployeeServiceImpl][addEmployee]");
		try
		{
			if(newEmployeeDTO != null)
			{
				EmployeeDetailsDTO employee = this.iEmployeeDAO.getEmployeeByEmpNo(newEmployeeDTO.getEmpNo());
				if(employee == null)
				{
					this.iEmployeeDAO.addEmployee(newEmployeeDTO);
					response = Commons.getSuccessResponse(BaseResponseDTO.SUCCESS_STATUS, newEmployeeDTO.getEmpName()+"["+newEmployeeDTO.getEmpNo()+"] Employee Details Added Successfully", null);
				}
				else
				{
					throw new AppException(BaseResponseDTO.FAILURE_STATUS, "Employee with "+employee.getEmpNo()+" employee ID Already exists.");
				}
			}
			else
			{
				throw new AppException(BaseResponseDTO.FAILURE_STATUS, "Invalid Employee Data");
			}
		}
		catch(AppException e)
		{
			logger.error("[EmployeeServiceImpl][addEmployee]",e);
			throw e;
		}
		catch(Exception e)
		{
			logger.error("[EmployeeServiceImpl][addEmployee]",e);
			throw e;
		}
		return response;
	}

}
