package timelineapi.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import timelineapi.common.exception.CustomErrorException;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

public abstract class SelfValidating<T> {

    private static final Map<Class<?>, Errors> errors = new HashMap<>();

    static {
        errors.put(NotNull.class, Errors.ERR0001);
        errors.put(NotBlank.class, Errors.ERR0001);
        errors.put(NotEmpty.class, Errors.ERR0006);
        errors.put(Positive.class, Errors.ERR0002);
        errors.put(PositiveOrZero.class, Errors.ERR0002);
        errors.put(Size.class, Errors.ERR0002);
        errors.put(Min.class, Errors.ERR0002);
        errors.put(Max.class, Errors.ERR0002);
    }

    private final Validator validator;

    public SelfValidating() {
        final var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void validate() {
        final var violations = validator.validate((T) this);

        if (!violations.isEmpty()) {
            final var violation = violations.stream().findFirst().get();
            final var errorAnnotation = ((ConstraintDescriptorImpl) violation.getConstraintDescriptor())
                    .getAnnotationDescriptor().getType();
            final var error = errors.get(errorAnnotation);

            if (error == null)
                throw new RuntimeException();

            final var propertyName = ((PropertyNamingStrategies.SnakeCaseStrategy) PropertyNamingStrategies.SNAKE_CASE)
                    .translate(violation.getPropertyPath().toString());

            throw new CustomErrorException(error, propertyName);
        }
    }
}
