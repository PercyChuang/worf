package orj.worf.validation;

import javax.validation.Validator;

public class ValidatorHolder {

    private static Validator validator;

    public void setValidator(final Validator validator) {
        ValidatorHolder.validator = validator;
    }

    public static Validator getValidator() {
        if (validator == null) {
            throw new IllegalAccessError("no Validator has bean defined.");
        }
        return validator;
    }

    public void destroy() {
        setValidator(null);
    }
}