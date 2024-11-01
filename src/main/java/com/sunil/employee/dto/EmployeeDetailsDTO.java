/*
*
*N Sunil 
*
*/

package com.sunil.employee.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class EmployeeDetailsDTO implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String empNo;
	
	private String empName;
	
	private String email;
	
	private String mobileNumber;
	
	private String bloodGroup;
	
	private AddressDetailsDTO addressDetails;
	
}
