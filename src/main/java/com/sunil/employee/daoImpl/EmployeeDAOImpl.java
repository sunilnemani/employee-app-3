/*
*
*N Sunil 
*
*/

package com.sunil.employee.daoImpl;

import org.springframework.stereotype.Repository;

import com.sunil.employee.dao.BaseDAO;
import com.sunil.employee.dao.IEmployeeDAO;
import com.sunil.employee.dto.EmployeeDetailsDTO;
import com.sunil.employee.model.EmployeeDetails;

import jakarta.persistence.TypedQuery;

@Repository
public class EmployeeDAOImpl extends BaseDAO implements IEmployeeDAO
{
	
	@Override
	public EmployeeDetailsDTO getEmployeeByEmpNo(String empNo)
	{
		TypedQuery<EmployeeDetails> query = this.entityManager.createQuery("select e from EmployeeDetails e where e.empNo = :empNo", EmployeeDetails.class).setParameter("empNo", empNo);
		EmployeeDetailsDTO employee = this.copyPropertiesWithNullCheck(EmployeeDetailsDTO.class, this.getCustomSingleResult(query));
		return employee;
	}
	
	@Override
	public void addEmployee(EmployeeDetailsDTO employeeDTO)
	{
		EmployeeDetails empEntity = this.copyProperties(EmployeeDetails.class, employeeDTO);
		this.entityManager.persist(empEntity);
		employeeDTO.setId(empEntity.getId());
	}
	
}
