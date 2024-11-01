/*
*
*N Sunil 
*
*/

package com.sunil.employee.daoImpl;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunil.employee.model.EmployeeDetails;

public interface EmployeeRepo extends JpaRepository<EmployeeDetails, Integer>
{

}
