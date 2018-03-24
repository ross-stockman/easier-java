package rws.easierjava.validation;

import java.util.Set;
import java.util.function.Consumer;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanValidator.class);

	private final Consumer<Violation> violationHandler;

	public BeanValidator(Consumer<Violation> violationHandler) {
		this.violationHandler = violationHandler;
	}

	public boolean isValid(Object obj) {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<Object>> violations = validator.validate(obj);
		for (ConstraintViolation<Object> violation : violations) {

			Violation v = new Violation(violation.getRootBeanClass(), violation.getPropertyPath(),
					violation.getMessage(), violation.getInvalidValue(), violation.getMessageTemplate());
			violationHandler.accept(v);
		}
		return violations.isEmpty();
	}

	public static class Violation {
		private final Class<?> type;
		private final Path field;
		private final String message;
		private final Object invalidValue;
		private final String messageTemplate;

		public Violation(Class<?> type, Path field, String message, Object invalidValue, String messageTemplate) {
			this.type = type;
			this.field = field;
			this.message = message;
			this.invalidValue = invalidValue;
			this.messageTemplate = messageTemplate;
		}

		public Class<?> getType() {
			return type;
		}

		public Path getField() {
			return field;
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

	}
}
