package com.sunil.employee.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDTO
{

	public static final String SUCCESS_STATUS = "0";
	public static final String FAILURE_STATUS = "1";
	public static final int SESSION_EXPIRED = 1;

	private String status;
	private String respCode;
	private String respMessage;
	private Object data;

	public BaseResponseDTO(String status, String respCode, String respMessage)
	{
		super();
		this.status = status;
		this.respCode = respCode;
		this.respMessage = respMessage;
	}
	
	public boolean isSuccess()
	{
		return this.status == null ? false : this.status.equals(SUCCESS_STATUS);
	}
}
