package rws.easierjava.core;

import java.util.regex.Pattern;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;

public class EncryptablePropertyResolver extends PropertyResolver {

	private final StringEncryptor stringEncryptor;

	public EncryptablePropertyResolver(StringEncryptor stringEncryptor) {
		super();
		this.stringEncryptor = stringEncryptor;
	}

	public EncryptablePropertyResolver(StringEncryptor stringEncryptor, Mode mode) {
		super(mode);
		this.stringEncryptor = stringEncryptor;
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		String value = super.getProperty(key, defaultValue);
		if (value != null && decryptable(value)) {
			return PropertyValueEncryptionUtils.decrypt(value, stringEncryptor);
		} else {
			return value;
		}
	}

	private boolean decryptable(String value) {
		return Pattern.matches("^ENC\\([\\w-\\/=+]*\\)$", value);
	}

}
