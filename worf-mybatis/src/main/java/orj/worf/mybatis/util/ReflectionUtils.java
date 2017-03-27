package orj.worf.mybatis.util;

import java.lang.reflect.Field;

public final class ReflectionUtils extends org.springframework.util.ReflectionUtils {

    public static Object getFieldValue(final Object target, final String fieldName) {
        Field field = findField(target.getClass(), fieldName);
        boolean accessible = field.isAccessible();
        makeAccessible(field);
        Object value = getField(field, target);
        field.setAccessible(accessible);
        return value;
    }
}
