package rws.easierjava.validation;

import java.util.Set;
import java.util.function.Consumer;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class BeanValidator {

	private final Consumer<Violation> violationHandler;

	public BeanValidator(Consumer<Violation> violationHandler) {
		this.violationHandler = violationHandler;
	}

	public boolean isValid(Object obj) {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<Object>> violations = validator.validate(obj);
		for (ConstraintViolation<Object> violation : violations) {
			Violation v = new Violation(violation.getRootBeanClass(), violation.getLeafBean().getClass(),
					violation.getPropertyPath(), violation.getMessage(), violation.getInvalidValue(),
					violation.getMessageTemplate());
			violationHandler.accept(v);
		}
		return violations.isEmpty();
	}

	public static class Violation {
		private final Class<?> rootType;
		private final Class<?> type;
		private final Path propertyPath;
		private final String message;
		private final Object invalidValue;
		private final String messageTemplate;

		public Violation(Class<?> rootType, Class<?> type, Path propertyPath, String message, Object invalidValue,
				String messageTemplate) {
			this.rootType = rootType;
			this.type = type;
			this.propertyPath = propertyPath;
			this.message = message;
			this.invalidValue = invalidValue;
			this.messageTemplate = messageTemplate;
		}

		public Class<?> getRootType() {
			return rootType;
		}

		public Class<?> getType() {
			return type;
		}

		public Path getPropertyPath() {
			return propertyPath;
		}

		public String getMessage() {
			return this.message;
		}

		public Object getInvalidValue() {
			return invalidValue;
		}

		public String getMessageTemplate() {
			return messageTemplate;
		}

		@Override
		public String toString() {
			return "Violation [rootType=" + rootType + ", type=" + type + ", propertyPath=" + propertyPath
					+ ", message=" + message + ", invalidValue=" + invalidValue + ", messageTemplate=" + messageTemplate
					+ "]";
		}

	}
}
