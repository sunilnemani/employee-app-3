/*
*
*N Sunil 
*
*/

package com.sunil.employee.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Commons
{
	
	private static final Logger log=LoggerFactory.getLogger(Commons.class);
	
	public static boolean isNullOrEmpty(String val)
	{
		return val == null ? true : val.trim().isEmpty();
	}
	
	public static String removePlaceHolders(String data)
	{
		String returnVal = data;
		if(data != null)
		{
			if(data.startsWith("${") && data.endsWith("}"))
			{
				returnVal = data.substring(2, data.length()-1);
			}
		}
		return returnVal;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T convert(String data,Class<T> type)
	{
		T t = null;
		if(type.equals(Integer.class))
		{
			t = (T) toInteger(data);
		}
		else if(type.equals(String.class))
		{
			t = (T) data;
		}
		else if(type.equals(Boolean.class))
		{
			t = (T)toBoolean(data);
		}
		else
		{
			log.error("Error. Support for type "+type+" is not implemented");
			throw new RuntimeException("Error. Support for type "+type+" is not implemented");
		}
		return t;
	}
	
	private static Boolean toBoolean(String value)
	{
		return value == null ? false : value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("TRUE");
	}

	private static Integer toInteger(String value)
	{
		Integer intVal = null;
		try
		{
			intVal = Integer.parseInt(value);
		}
		catch (Exception ex)
		{
			log.error("Error ",ex);
			throw new RuntimeException("Invalid value for Integer "+value);
		}
		return intVal;
	}
	
	public static <T> void readAndFillValues(T t,Map<String,String> propsMap)
	{
		try
		{
			Class<?> tClass = t.getClass();
			Field[] declaredFields = tClass.getDeclaredFields();
			if(declaredFields != null)
			{
				Value value = null;
				String propertyValue = null;
				String key = null;
				for(Field field : declaredFields)
				{
					if(field.isAnnotationPresent(Value.class))
					{
						value = field.getAnnotation(Value.class);
						key = removePlaceHolders(value.value());
						propertyValue = propsMap.get(key);
						if(isNullOrEmpty(propertyValue))
						{
							log.error("No value found for property "+key);
							throw new RuntimeException("No value found for property "+key);
						}
						propertyValue.trim();
						try
						{
							BeanUtils.copyProperty(t, field.getName(), convert(propertyValue, field.getType()));
						}
						catch(Exception ex)
						{
							log.error("Error Unable to parse value for "+key,ex);
							throw new RuntimeException("Error Unable to parse value for "+key);
						}
					}
				}
			}
			log.info(t.toString());
		}
		catch (Exception ex)
		{
			log.error("Error ",ex);
			throw new RuntimeException("Error initialising beans",ex);
		}
	}
	
	public static Date trunacteTime(Date inDate)
	{
		Date retVal = null;
		if(inDate != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			try
			{
				retVal = sdf.parse(sdf.format(inDate));
			}
			catch (Exception ex)
			{
				throw new RuntimeException(ex.getMessage(),ex);
			}
		}
		return retVal;
	}
	
	public static Date trunacteTime(Date inDate, String format)
	{
		Date retVal = null;
		if(inDate != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try
			{
				retVal = sdf.parse(sdf.format(inDate));
			}
			catch (Exception ex)
			{
				throw new RuntimeException(ex.getMessage(),ex);
			}
		}
		return retVal;
	}
	
	public static BaseResponseDTO getSuccessResponse(String code,String message, Object data)
	{
		return getBaseResponseDTO(code, message,BaseResponseDTO.SUCCESS_STATUS, data);
	}

	public static BaseResponseDTO getFailureResponse(String code,String message, Object data)
	{
		return getBaseResponseDTO(code, message, BaseResponseDTO.FAILURE_STATUS, data);
	}

	public static BaseResponseDTO getBaseResponseDTO(String respCode,String respDesc,String STATUS, Object data)
	{
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
		baseResponseDTO.setRespCode(respCode);
		baseResponseDTO.setStatus(STATUS);
		baseResponseDTO.setRespMessage(respDesc);
		baseResponseDTO.setData(data);
		return baseResponseDTO;
	}
	
	public static String encodeHeaders(String userName, String password)
	{
		String data= userName+":"+password;
		String encodeDate = Base64.encodeBase64String(data.getBytes());
		return encodeDate;
	}
	
	public static <U> U copyPropertiesWithNullCheck(Class<U> destinationClass, Object source)
	{
		U u = null;
		try
		{
			if (source != null)
			{
				u = destinationClass.newInstance();
				copyProperties(u, source);
			}

		}
		catch (RuntimeException rEx)
		{
			log.error("Error ", rEx);
			throw rEx;
		}
		catch (Exception ex)
		{
			log.error("Error ", ex);
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return u;
	}
	
	public static void copyProperties(Object destination, Object source)
	{
		try
		{
			BeanUtils.copyProperties(destination, source);
		}
		catch (IllegalAccessException ex)
		{
			throw new RuntimeException(ex.getMessage(), ex);
		}
		catch (InvocationTargetException ex)
		{
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

}
