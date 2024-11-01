/*
*
*N Sunil 
*
*/

package com.sunil.employee.service;

import com.sunil.employee.dto.EmployeeDetailsDTO;
import com.sunil.employee.utils.BaseResponseDTO;

public interface IEmployeeService
{
	
	public BaseResponseDTO getEmployeeById(Integer id);
	
	public BaseResponseDTO getEmployeeByEmpNo(String empNo);

	public BaseResponseDTO addEmployee(EmployeeDetailsDTO employeeDTO);
	
}
