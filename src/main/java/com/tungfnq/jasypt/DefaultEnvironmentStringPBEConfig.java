/**
 * 
 */
package com.tungfnq.jasypt;

import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author tungnq
 *
 */
public class DefaultEnvironmentStringPBEConfig extends EnvironmentStringPBEConfig implements InitializingBean {
	private String defaultPassword = null;

	/**
	 * @return the defaultPassword
	 */
	public String getDefaultPassword() {
		return defaultPassword;
	}

	/**
	 * @param defaultPassword the defaultPassword to set
	 */
	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}

	public void afterPropertiesSet() throws Exception {
		if(defaultPassword != null) {
			boolean hasPassword = true;
			try {
				hasPassword = (super.getPassword() != null);
			} catch (Exception e) {
				hasPassword = false;
			}
			if(!hasPassword) {
				super.setPassword(defaultPassword);
			}
		}
	}
}
