package com.tungfnq.jasypt;

import org.jasypt.commons.CommonUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * <p>
 * Subclass of
 * <tt>org.springframework.beans.factory.config.PropertyPlaceholderConfigurer</tt>
 * which can make use of a {@link org.jasypt.encryption.StringEncryptor} or
 * {@link org.jasypt.util.text.TextEncryptor} object to decrypt property values
 * if they are encrypted in the loaded resource locations.
 * </p>
 * <p>
 * A value is considered "encrypted" when it appears surrounded by
 * <tt>ENC(...)</tt>, like:
 * </p>
 * <p>
 * <center> <tt>my.value=ENC(!"DGAS24FaIO$)</tt> </center>
 * </p>
 * <p>
 * Encrypted and unencrypted objects can be combined in the same resources file.
 * </p>
 * 
 * @since 1.9.0
 * 
 * @author tunguyen
 * 
 */
public final class EncryptablePropertyPlaceholderConfigurer 
        extends PropertyPlaceholderConfigurer {
	/*
	 * Only one of these instances will be initialized, the other one will be
	 * null.
	 */
	private final StringEncryptor stringEncryptor;
	private final TextEncryptor textEncryptor;

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
	public EncryptablePropertyPlaceholderConfigurer(
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
	public EncryptablePropertyPlaceholderConfigurer(final TextEncryptor textEncryptor) {
		super();
		CommonUtils.validateNotNull(textEncryptor, "Encryptor cannot be null");
		this.stringEncryptor = null;
		this.textEncryptor = textEncryptor;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @since 1.8
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolveSystemProperty(java.lang.String)
	 */
    @Override
    protected String resolveSystemProperty(final String key) {
        return convertPropertyValue(super.resolveSystemProperty(key));
    }
    
}
