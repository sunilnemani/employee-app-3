/*
*
*N Sunil 
*
*/

package com.sunil.employee.dao;

import com.sunil.employee.dto.EmployeeDetailsDTO;

public interface IEmployeeDAO
{

	public EmployeeDetailsDTO getEmployeeByEmpNo(String id);
	
	public void addEmployee(EmployeeDetailsDTO employeeDTO);
	
}
