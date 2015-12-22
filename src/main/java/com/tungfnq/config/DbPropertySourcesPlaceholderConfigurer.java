package com.tungfnq.config;

import java.util.Iterator;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.DatabaseConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author tunguyen
 *
 */
public class DbPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {
	private static Logger logger = Logger.getLogger(DbPropertySourcesPlaceholderConfigurer.class);
	
	private static final String DEFAULT_DATASOURCENAME = "dataSource";
	private static final String DEFAULT_DBTABLENAME = "property";
	private static final String DEFAULT_DBKEYCOLUMNNAME = "key";
	private static final String DEFAULT_DBVALUECOLUMNNAME = "value";
	
	private String dataSourceName = DEFAULT_DATASOURCENAME;
	private String dbTableName = DEFAULT_DBTABLENAME;
	private String dbKeyColumnName = DEFAULT_DBKEYCOLUMNNAME;
	private String dbValueColumnName = DEFAULT_DBVALUECOLUMNNAME;
	
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
	{
		DataSource ds  = null;
		
		if(ds == null) {
			String ds_name = (dataSourceName == null ? DEFAULT_DATASOURCENAME : dataSourceName);
			ds = (DataSource) beanFactory.getBean(ds_name);
		}
		
	    if(ds  == null) {
	    	ds = beanFactory.getBean(DataSource.class);
	    }
		Properties dbProps = loadPropFromDB(ds);
		
		if(logger.isInfoEnabled()) {
			logger.info("dbProps:" + dbProps);
		}
		
		setProperties(dbProps);
		super.postProcessBeanFactory(beanFactory);
	}
	
	/**
	 * 
	 * @return
	 * @throws BeansException
	 */
	protected Properties loadPropFromDB(DataSource dataSource) throws BeansException {
		Properties prop = new Properties();
		try {
			String tableName =  (dbTableName == null ? DEFAULT_DBTABLENAME : dbTableName);
			String keyColumnName = (dbKeyColumnName == null ? DEFAULT_DBKEYCOLUMNNAME : dbKeyColumnName);
			String valueColumnName = (dbValueColumnName == null ? DEFAULT_DBVALUECOLUMNNAME : dbValueColumnName);
			
			if(logger.isInfoEnabled()) {
				logger.info("loadPropFromDB():"
								+ "dataSource: " + dataSource
								+ "tableName: " + tableName
								+ "keyColumnName: " + keyColumnName
								+ "valueColumnName: " + valueColumnName
							);
			}
			
			Configuration config = new DatabaseConfiguration(dataSource, tableName, keyColumnName, valueColumnName);
			
			for(Iterator<String>  iKey = config.getKeys(); iKey.hasNext(); ) {
				String key = iKey.next();
				String value = config.getString(key, "");
				prop.put(key, value);
			}
			
			return prop;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanCreationException("loadPropFromDB()", e);
		}
	}


	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getDbTableName() {
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	public String getDbKeyColumnName() {
		return dbKeyColumnName;
	}

	public void setDbKeyColumnName(String dbKeyColumnName) {
		this.dbKeyColumnName = dbKeyColumnName;
	}

	public String getDbValueColumnName() {
		return dbValueColumnName;
	}

	public void setDbValueColumnName(String dbValueColumnName) {
		this.dbValueColumnName = dbValueColumnName;
	}

}