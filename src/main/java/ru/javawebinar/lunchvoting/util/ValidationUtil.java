package ru.javawebinar.lunchvoting.util;

import ru.javawebinar.lunchvoting.util.exception.NotFoundException;

//import javax.validation.*;


public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String arg) {
        if (!found) {
            throw new NotFoundException(arg);
        }
    }


  //  private static final Validator validator;

//    static {
//        //  From Javadoc: implementations are thread-safe and instances are typically cached and reused.
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        //  From Javadoc: implementations of this interface must be thread-safe
//        validator = factory.getValidator();
//    }

//    public static <T> void validate(T bean, Class<?>... groups) {
//        // https://alexkosarev.name/2018/07/30/bean-validation-api/
//        Set<ConstraintViolation<T>> violations = validator.validate(bean, groups);
//        if (!violations.isEmpty()) {
//            throw new ConstraintViolationException(violations);
//        }
//    }
}