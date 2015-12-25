
package com.tungfnq.jasypt;

import java.io.IOException;
import java.util.Properties;

import org.jasypt.commons.CommonUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * <p>
 * </p>
 * 
 * @since 1.9.0
 * 
 * @author tunguyen
 * 
 */
public final class EncryptablePropertySourcesPlaceholderConfigurer 
        extends PropertySourcesPlaceholderConfigurer {

	/*
	 * Only one of these instances will be initialized, the other one will be
	 * null.
	 */
	private final StringEncryptor stringEncryptor;
	private final TextEncryptor textEncryptor;

	
	/*
	 * This flag will keep track of whether the "convertProperties()" method
	 * (which decrypts encrypted property entries) has already been called
	 * or not. 
	 * 
	 * This is needed because of a bug in Spring 3.1.0.RELEASE:
	 * https://jira.springsource.org/browse/SPR-8928
	 * 
	 * This flag will avoid calling "convertProperties()" twice once this
	 * bug has been solved.
	 */
	private boolean alreadyConverted = false;
	
	
	/**
	 * <p>
	 * Creates an <tt>EncryptablePropertyPlaceholderConfigurer</tt> instance
	 * which will use the passed {@link StringEncryptor} object to decrypt
	 * encrypted values.
	 * </p>
	 * 
	 * @param stringEncryptor
	 *            the {@link StringEncryptor} to be used do decrypt values. It
	 *            can not be null.
	 */
	public EncryptablePropertySourcesPlaceholderConfigurer(
	        final StringEncryptor stringEncryptor) {
		super();
		CommonUtils.validateNotNull(stringEncryptor, "Encryptor cannot be null");
		this.stringEncryptor = stringEncryptor;
		this.textEncryptor = null;
	}

	/**
	 * <p>
	 * Creates an <tt>EncryptablePropertyPlaceholderConfigurer</tt> instance which will use the
	 * passed {@link TextEncryptor} object to decrypt encrypted values.
	 * </p>
	 * 
	 * @param textEncryptor
	 *            the {@link TextEncryptor} to be used do decrypt values. It can
	 *            not be null.
	 */
	public EncryptablePropertySourcesPlaceholderConfigurer(final TextEncryptor textEncryptor) {
		super();
		CommonUtils.validateNotNull(textEncryptor, "Encryptor cannot be null");
		this.stringEncryptor = null;
		this.textEncryptor = textEncryptor;
	}

	
	
	
	

	/*
	 * This is needed because of https://jira.springsource.org/browse/SPR-8928
	 */
	@Override
    protected Properties mergeProperties() throws IOException {
        final Properties mergedProperties = super.mergeProperties();
        convertProperties(mergedProperties);
        return mergedProperties;
    }


	
	
    /*
     * This is needed because of https://jira.springsource.org/browse/SPR-8928
     */
    @Override
    protected void convertProperties(final Properties props) {
        if (!this.alreadyConverted) {
            super.convertProperties(props);
            this.alreadyConverted = true;
        }
    }

    
    
    /*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.PropertyResourceConfigurer#convertPropertyValue(java.lang.String)
	 */
    @Override
	protected String convertPropertyValue(final String originalValue) {
		if (!PropertyValueEncryptionUtils.isEncryptedValue(originalValue)) {
			return originalValue;
		}
		if (this.stringEncryptor != null) {
			return PropertyValueEncryptionUtils.decrypt(originalValue,
					this.stringEncryptor);

		}
		return PropertyValueEncryptionUtils.decrypt(originalValue, this.textEncryptor);
	}
    
}
