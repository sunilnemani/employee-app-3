/*
*
*N Sunil 
*
*/

package com.sunil.employee.model;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee_details")
@Getter
@Setter
public class EmployeeDetails implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "emp_no")
	private String empNo;
	
	@Column(name = "emp_name")
	private String empName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "blood_group")
	private String bloodGroup;
	
}
