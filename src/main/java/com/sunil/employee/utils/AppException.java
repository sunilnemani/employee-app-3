package com.sunil.employee.utils;

public class AppException extends RuntimeException
{
	
	private static final long serialVersionUID = 1L;
	private String errCode;
	private String errMsg;
	private Throwable cause;
	

	public AppException(String errCode, String errMsg, Throwable cause)
	{
		super(errCode,cause);
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.cause = cause;
	}

	public AppException(String errCode, String errMsg)
	{
		super(errCode);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public String getErrCode()
	{
		return this.errCode;
	}
	
	public String getErrMsg()
	{
		return this.errMsg;
	}
	
	@Override
	public Throwable getCause()
	{
		return this.cause;
	}


}
