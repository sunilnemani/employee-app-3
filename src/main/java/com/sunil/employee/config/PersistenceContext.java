/*
*
*N Sunil 
*
*/

package com.sunil.employee.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class PersistenceContext
{
	
	private static final Logger logger = LoggerFactory.getLogger(PersistenceContext.class);
	
	private static final String FILE_NAME = "db.properties";
	private static final String ENCODED_SEED_KEY = "aWZpZ3MxMzVAJF4kXiYpIw==";
	private static final String ALGO = "AES";
	private static Cipher eCipher;
	private static Cipher dCipher;
	
	private static final String DIALECT = "hibernate.dialect";
	private static final String HBM_2_DDL = "hibernate.hbm2ddl.auto";
	private static final String SHOW_SQL = "hibernate.show_sql";
	private static final String FORMAT_SQL = "hibernate.format_sql";

	private static final String DATA_SOURCE_PREFIX = "data_source_";
	private static final String DECRYPT_DATA_SOURCE_PREFIX = "decrypt_data_source_";
	
	@Bean(destroyMethod = "close")
	@Qualifier("dataSourceBean")
	@Primary
	public DataSource getDataSource() throws Exception
	{
		logger.info("[PersistenceContext][getDataSource] : loading DataSource");
		ComboPooledDataSource dataSource = null;
		dataSource = new ComboPooledDataSource();
		Properties properties = loadProperties(FILE_NAME);
		for (String property : properties.stringPropertyNames())
		{
			BeanUtils.setProperty(dataSource, property, properties.get(property));
		}
		dataSource.setProperties(properties);
		return dataSource;
	}
	
	@Bean
	@Qualifier("entityManagerFactoryBean")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired @Qualifier("dataSourceBean") DataSource dataSource) throws Exception
	{
		logger.info("[PersistenceContext][getEntityManager] : loading EntityManager");
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setPersistenceUnitName("entityManager");
		Properties properties = loadProperties(FILE_NAME);
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setPackagesToScan("com.sunil.employee.model");
		Properties jpaProperties = new Properties();
		jpaProperties.put(DIALECT, properties.getProperty(DIALECT));
		jpaProperties.put(HBM_2_DDL, properties.getProperty(HBM_2_DDL));
		jpaProperties.put(SHOW_SQL, Boolean.parseBoolean(properties.getProperty(SHOW_SQL)));
		jpaProperties.put(FORMAT_SQL, Boolean.parseBoolean(properties.getProperty(FORMAT_SQL)));
		entityManagerFactoryBean.setJpaProperties(jpaProperties);
		return entityManagerFactoryBean;
	}
	
	@Bean
	@Qualifier("transactionManager")
	@Primary
	JpaTransactionManager transactionManager(@Autowired @Qualifier("entityManagerFactoryBean") EntityManagerFactory entityManagerFactory)
	{
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
	
	private Properties loadProperties(String fileName) throws Exception
	{
		InputStream stream = null;
		Properties props = new Properties();
		Properties validatedProperties = null;

		try
		{
			stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if(stream == null)
			{
				stream = new FileInputStream(fileName);
			}
			props.load(stream);
			
			if (props != null)
			{
				validatedProperties = new Properties();
				String dataSourceKey = null;
				String dataSourceValue = null;
				for (String key : props.stringPropertyNames())
				{
					if (key.startsWith(DECRYPT_DATA_SOURCE_PREFIX))
					{
						dataSourceKey = key.substring(DECRYPT_DATA_SOURCE_PREFIX.length());
						dataSourceValue = new String(PersistenceContext.decryptPasswordUsingSeed(Base64.decodeBase64(props.getProperty(key).trim())));
						validatedProperties.put(dataSourceKey, dataSourceValue);
					}
					else if (key.startsWith(DATA_SOURCE_PREFIX))
					{
						dataSourceKey = key.substring(DATA_SOURCE_PREFIX.length());
						dataSourceValue = props.getProperty(key);
						validatedProperties.put(dataSourceKey, dataSourceValue);
					}
				}
			}
		}
		finally
		{
			if(stream!=null)
			{
				try
				{
					stream.close();
				}
				catch(Exception e)
				{
					
				}
			}
		}
		return validatedProperties;
	}
	
	private static byte[] decryptPasswordUsingSeed(byte[] paswd) throws Exception
	{
		SecretKeySpec key = new SecretKeySpec(Base64.decodeBase64(ENCODED_SEED_KEY), ALGO);
		dCipher = Cipher.getInstance(ALGO);
		dCipher.init(Cipher.DECRYPT_MODE, key);
		return dCipher.doFinal(paswd);
	}

	private static byte[] encryptPasswordUsingSeed(byte[] paswd) throws Exception
	{
		SecretKeySpec key = new SecretKeySpec(Base64.decodeBase64(ENCODED_SEED_KEY), ALGO);
		eCipher = Cipher.getInstance(ALGO);
		eCipher.init(Cipher.ENCRYPT_MODE, key);
		return eCipher.doFinal(paswd);
	}
	
	public static void main(String[] args) 
	{
		String encData="";
		try
		{
			logger.info("ENCRYPT_RES : " + encData);
			logger.info("Data : "+encryptPasswordUsingSeed(encData.getBytes()));
		}
		catch (Exception e)
		{
			logger.error("Decrypt Exception : " ,e);
		}
	}
	
	
}
