package com.sunil.employee.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@SuppressWarnings("deprecation")
public class BaseDAO
{


	@PersistenceContext(unitName = "entityManager")
	@Qualifier("entityManagerFactoryBean")
	protected EntityManager entityManager;

	private static final Logger log = LoggerFactory.getLogger(BaseDAO.class);

	protected Integer getIntValue(Object val)
	{
		Integer intValue = null;
		if (val != null)
		{
			if (val instanceof BigDecimal)
			{
				intValue = ((BigDecimal) val).intValue();
			}
			else
			{
				intValue = (Integer) val;
			}
		}
		return intValue;
	}

	protected <U> U copyPropertiesWithNullCheck(Class<U> destinationClass, Object source)
	{
		U u = null;
		try
		{
			if (source != null)
			{
				u = destinationClass.newInstance();
				this.copyProperties(u, source);
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
	
	protected <U> U copyPropertiesCustom(Class<U> destinationClass, Object source)
	{
		U u = null;
		try
		{
			if (source != null)
			{
				u = destinationClass.newInstance();
				this.copyProperties(u, source);
			}
		}
		catch (RuntimeException rEx)
		{
			log.error("Error ", rEx);
			//throw rEx;
		}
		catch (Exception ex)
		{
			log.error("Error ", ex);
			//throw new RuntimeException(ex.getMessage(), ex);
		}
		return u;
	}
	
	protected <U> U copyProperties(Class<U> destinationClass, Object source)
	{
		U u = null;
		try
		{
			u = destinationClass.newInstance();
			this.copyProperties(u, source);
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

	protected void copyProperties(Object destination, Object source)
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

	protected <U> U getEntityFromDTO(Class<U> entityClass, Object dto, String... ignoreProperties)
	{
		U u = null;
		try
		{
			if (dto != null)
			{
				u = entityClass.newInstance();
				org.springframework.beans.BeanUtils.copyProperties(dto, u, ignoreProperties);
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

	protected <T> T getCustomSingleResult(TypedQuery<T> query)
	{
		query.setMaxResults(1);
		List<T> list = query.getResultList();
		if (list == null || list.isEmpty())
		{
			return null;
		}
		return list.get(0);
	}

	protected <T, U> List<U> toList(List<T> sourceList, Class<U> destinationClass)
	{
		List<U> anotherList = null;
		try
		{
			if (sourceList != null && !sourceList.isEmpty())
			{
				anotherList = new ArrayList<U>(sourceList.size());
				U u = null;
				for (T t : sourceList)
				{
					u = destinationClass.newInstance();
					this.copyProperties(u, t);
					anotherList.add(u);
				}
			}
		}
		catch (Exception ex)
		{
			log.error("Error ", ex);
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return anotherList;
	}

	protected <T, U> List<U> toList(List<T> sourceList, Class<U> destinationClass, String... ignoreProperties)
	{
		List<U> anotherList = null;
		try
		{
			if (sourceList != null && !sourceList.isEmpty())
			{
				anotherList = new ArrayList<U>(sourceList.size());
				U u = null;
				for (T t : sourceList)
				{
					u = destinationClass.newInstance();
					org.springframework.beans.BeanUtils.copyProperties(t, u, ignoreProperties);
					anotherList.add(u);
				}
			}
		}
		catch (Exception ex)
		{
			log.error("Error ", ex);
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return anotherList;
	}

	protected boolean isDetached(EntityManager manager, Object entityObject, Object id)
	{
		return id != null
				&& !manager.contains(entityObject) // must not be managed
				// now
				&& manager.find(entityObject.getClass(), id) != null; // must
	}

}
