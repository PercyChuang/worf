package orj.worf.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import orj.worf.exception.FastCheckedException;

/**
 * 基于JSR-303规范验证参数.
 */
public class ValidationUtils extends org.springframework.validation.ValidationUtils {

    public static Map<String, String> getValidationInfo(final Set<?> vals) {
        Map<String, String> infoMap = new HashMap<String, String>(vals == null ? 0 : vals.size());
        for (Object violation : vals) {
            if (!(violation instanceof ConstraintViolation)) {
                continue;
            }
            ConstraintViolation<?> constraintViolation = (ConstraintViolation<?>) violation;
            infoMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        }
        return infoMap;
    }

    private static FastCheckedException throwFastException(final Set<?> vals) throws FastCheckedException {
        StringBuilder result = new StringBuilder(64);
        for (Object violation : vals) {
            if (!(violation instanceof ConstraintViolation)) {
                continue;
            }
            if (result.length() > 0) {
                result.append("|");
            }
            ConstraintViolation<?> constraintViolation = (ConstraintViolation<?>) violation;
            result.append(constraintViolation.getPropertyPath().toString());
            result.append(":");
            result.append(constraintViolation.getMessage());
        }
        throw new FastCheckedException(result.toString());
    }

    public static <T> void validate(final T object, final Class<?>... groups) throws FastCheckedException {
        Validator validator = ValidatorHolder.getValidator();
        Set<ConstraintViolation<T>> result;
        try {
            result = validator.validate(object, groups.length == 0 ? new Class[0] : groups);
        } catch (Exception e) {
            throw new FastCheckedException(e.getMessage());
        }
        if (!result.isEmpty()) {
            throwFastException(result);
        }
    }
}