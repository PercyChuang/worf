package orj.worf.util;

import com.google.common.collect.Lists;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class ReflectionUtil {
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    private static final ConcurrentMap<MethodDescriptor, WeakReference<Method>> methodCache = new ConcurrentHashMap();
    private static final int CACHE_THRESHOLD = 30;

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] methodArgumentTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, methodArgumentTypes);
        } catch (Exception ignored) {
        }
        return null;
    }

    private static Method findMethod(Class<?> type, String methodName, Object[] arguments) {
        List methodArgumentTypesList = Lists.newArrayListWithCapacity(arguments.length);
        for (Object argument : arguments) {
            methodArgumentTypesList.add(argument.getClass());
        }
        return findMethod(type, methodName, (Class[]) methodArgumentTypesList.toArray(new Class[0]));
    }

    public static <T> T invokeMethod(Object source, String methodName, Object[] arguments) {
        Class type = source.getClass();
        Method method = findMethod(type, methodName, arguments);
        if (method == null) {
            return null;
        }
        return (T) invokeMethod(source, method, arguments);
    }

    public static <T> T invokeStaticMethod(Class<?> type, String methodName, Object[] arguments) {
        Method method = findMethod(type, methodName, arguments);
        if (method == null) {
            return null;
        }
        return (T) invokeMethod(null, method, arguments);
    }

    private static Object invokeMethod(Object source, Method method, Object[] arguments)
            throws IllegalArgumentException {
        boolean accessible = method.isAccessible();
        Object value = null;
        try {
            method.setAccessible(true);
            value = method.invoke(source, arguments);
        } catch (Exception e) {
        } finally {
            method.setAccessible(accessible);
        }
        return value;
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>[] methodArgumentTypes) {
        Method method = null;
        Class classToFind = clazz;
        do {
            if (classToFind == null)
                break;
            try {
                method = classToFind.getDeclaredMethod(methodName, methodArgumentTypes);
            } catch (Exception ignored) {
                method = null;
                classToFind = classToFind.getSuperclass();
            }
        } while (method == null);

        return method;
    }

    public static Map<String, Object> describe(Object object) {
        if (object == null) {
            return new HashMap(0);
        }
        Field[] declaredFields = object.getClass().getDeclaredFields();
        Map props = new HashMap(declaredFields.length);
        for (Field field : declaredFields) {
            ReflectionUtils.makeAccessible(field);
            try {
                props.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
            }
        }
        return props;
    }

    public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
        Assert.notNull(object);
        Assert.hasText(propertyName);
        return getDeclaredField(object.getClass(), propertyName);
    }

    public static Field getDeclaredField(Class<?> clazz, String propertyName) throws NoSuchFieldException {
        Assert.notNull(clazz);
        Assert.hasText(propertyName);
        for (Class currentClz = clazz; currentClz != null; currentClz = currentClz.getSuperclass())
            try {
                return currentClz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
            }
        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
    }

    public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
        Assert.notNull(object);
        Assert.hasText(propertyName);
        Field field = getDeclaredField(object, propertyName);
        ReflectionUtils.makeAccessible(field);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public static void forceSetProperty(Object object, String propertyName, Object newValue)
            throws NoSuchFieldException {
        Assert.notNull(object);
        Assert.hasText(propertyName);
        Field field = getDeclaredField(object, propertyName);
        ReflectionUtils.makeAccessible(field);
        try {
            field.set(object, newValue);
        } catch (IllegalAccessException e) {
        }
    }

    public static void forceSetProperties(Object object, Map<String, Object> props) throws NoSuchFieldException {
        for (Map.Entry prop : props.entrySet())
            forceSetProperty(object, (String) prop.getKey(), prop.getValue());
    }

    public static Object invokeMethod(Object obj, String methodName, Object[] args, Class<?>[] argTypes)
            throws NoSuchMethodException, IllegalArgumentException {
        Assert.notNull(obj);
        Assert.hasText(methodName);
        argumentSanityCheck(args, argTypes);
        Class clazz = obj.getClass();
        Method method = findMatchingMethod(clazz, methodName, argTypes);
        if (method == null) {
            throw new NoSuchMethodException(String.format("No such method: %s.%s(%s)",
                    new Object[] { clazz.getSimpleName(), methodName, StringUtils.join(argTypes, ", ") }));
        }

        ReflectionUtils.makeAccessible(method);
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return null;
    }

    private static void argumentSanityCheck(Object[] args, Class<?>[] argTypes) throws IllegalArgumentException {
        Assert.notNull(args, "args should not be null");
        Assert.notNull(argTypes, "argTypes should not be null");
        Assert.isTrue(args.length == argTypes.length, "the arrays of arguments and types should have the same length");
        for (int i = 0; i < args.length; ++i) {
            Class argType = argTypes[i];
            Object arg = args[i];
            if (argType == null)
                Assert.isTrue((arg == null) || (!(arg.getClass().isPrimitive())));
            else
                Assert.isTrue((arg == null) || (argType.isInstance(arg)));
        }
    }

    public static Object invokeMethod(Class<?> clazz, String methodName, Object[] args) throws NoSuchMethodException {
        Class[] types = null;
        if (args != null) {
            types = new Class[args.length];
            for (int i = 0; i < args.length; ++i) {
                if (args[i] == null)
                    types[i] = null;
                else {
                    types[i] = args[i].getClass();
                }
            }
        }
        return invokeMethod(clazz, methodName, args, types);
    }

    public static Object invokeMethod(Class<?> clazz, String methodName, Object[] args, Class<?>[] argTypes)
            throws NoSuchMethodException {
        Assert.hasText(methodName);
        argumentSanityCheck(args, argTypes);
        Method method = findMatchingMethod(clazz, methodName, argTypes);
        if (method == null) {
            throw new NoSuchMethodException(String.format("No such method: %s.%s(%s)",
                    new Object[] { clazz.getSimpleName(), methodName, StringUtils.join(argTypes, ", ") }));
        }

        Object obj = null;
        if (!(Modifier.isStatic(method.getModifiers()))) {
            try {
                obj = clazz.newInstance();
            } catch (Exception e) {
                ReflectionUtils.handleReflectionException(e);
            }
        }
        ReflectionUtils.makeAccessible(method);
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return null;
    }

    private static Method findMatchingMethod(Class<?> clazz, String methodName, Class<?>[] argTypes) {
        Assert.notNull(clazz);
        Assert.hasText(methodName);
        if (argTypes == null)
            argTypes = EMPTY_CLASS_ARRAY;
        Method theMethod;
        try {
            return clazz.getDeclaredMethod(methodName, argTypes);
        } catch (NoSuchMethodException e) {
            MethodDescriptor md = new MethodDescriptor(clazz, methodName, argTypes, false);
            theMethod = getCachedMethod(md);
            if (theMethod != null) {
                return theMethod;
            }
            int minMatchCost = 2147483647;
            while (clazz != null) {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (!(method.getName().equals(methodName))) {
                        continue;
                    }
                    Class[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length != argTypes.length) {
                        continue;
                    }
                    int currentCost = getTypeMatchCost(paramTypes, argTypes);
                    if (currentCost < minMatchCost) {
                        minMatchCost = currentCost;
                        theMethod = method;
                    }
                }
                clazz = clazz.getSuperclass();
            }
            if (theMethod != null)
                cacheMethod(md, theMethod);
        }
        return theMethod;
    }

    public static int getTypeMatchCost(Class<?>[] paramTypes, Class<?>[] argTypes) {
        int cost = 0;
        for (int i = 0; i < paramTypes.length; ++i) {
            if (!(isAssignmentCompatible(paramTypes[i], argTypes[i]))) {
                return 2147483647;
            }
            if (argTypes[i] != null) {
                Class paramType = paramTypes[i];
                Class superClass = argTypes[i].getSuperclass();
                while (superClass != null) {
                    if (paramType.equals(superClass)) {
                        cost += 2;
                        superClass = null;
                    }

                    if (isAssignmentCompatible(paramType, superClass)) {
                        cost += 2;
                        superClass = superClass.getSuperclass();
                    }

                    superClass = null;
                }
                if (paramType.isInterface()) {
                    ++cost;
                }
            }
        }
        return cost;
    }

    public static final boolean isAssignmentCompatible(Class<?> destType, Class<?> srcType) {
        Assert.notNull(destType);
        if (srcType == null) {
            return (!(destType.isPrimitive()));
        }
        if (destType.isAssignableFrom(srcType)) {
            return true;
        }
        if (destType.isPrimitive()) {
            Class destWrapperClazz = getPrimitiveWrapper(destType);
            if (destWrapperClazz != null) {
                return destWrapperClazz.equals(srcType);
            }
        }
        return false;
    }

    public static Class<?> getPrimitiveType(Class<?> wrapperType) {
        if (Integer.class.equals(wrapperType)) {
            return Integer.TYPE;
        }
        if (Long.class.equals(wrapperType)) {
            return Long.TYPE;
        }
        if (Float.class.equals(wrapperType)) {
            return Float.TYPE;
        }
        if (Double.class.equals(wrapperType)) {
            return Double.TYPE;
        }
        if (Short.class.equals(wrapperType)) {
            return Short.TYPE;
        }
        if (Boolean.class.equals(wrapperType)) {
            return Boolean.TYPE;
        }
        if (Character.class.equals(wrapperType)) {
            return Character.TYPE;
        }
        if (Byte.class.equals(wrapperType)) {
            return Byte.TYPE;
        }
        return null;
    }

    public static Class<?> getPrimitiveWrapper(Class<?> primitiveType) {
        if (Boolean.TYPE.equals(primitiveType)) {
            return Boolean.class;
        }
        if (Float.TYPE.equals(primitiveType)) {
            return Float.class;
        }
        if (Long.TYPE.equals(primitiveType)) {
            return Long.class;
        }
        if (Integer.TYPE.equals(primitiveType)) {
            return Integer.class;
        }
        if (Short.TYPE.equals(primitiveType)) {
            return Short.class;
        }
        if (Byte.TYPE.equals(primitiveType)) {
            return Byte.class;
        }
        if (Double.TYPE.equals(primitiveType)) {
            return Double.class;
        }
        if (Character.TYPE.equals(primitiveType)) {
            return Character.class;
        }
        return null;
    }

    public static List<Field> getFieldsByType(Object object, Class<?> type) {
        List list = new ArrayList();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(type)) {
                list.add(field);
            }
        }
        return list;
    }

    public static Class<?> getPropertyType(Class<?> type, String name) throws NoSuchFieldException {
        return getDeclaredField(type, name).getType();
    }

    public static String getGetterName(Class<?> type, String fieldName) {
        Assert.notNull(type, "Type required");
        Assert.hasText(fieldName, "FieldName required");
        if ("boolean".equals(type.getName())) {
            return "is" + StringUtils.capitalize(fieldName);
        }
        return "get" + StringUtils.capitalize(fieldName);
    }

    public static Method getGetterMethod(Class<?> type, String fieldName) {
        try {
            return type.getMethod(getGetterName(type, fieldName), EMPTY_CLASS_ARRAY);
        } catch (NoSuchMethodException e) {
        }
        return null;
    }

    private static Method getCachedMethod(MethodDescriptor md) {
        Reference ref = (Reference) methodCache.get(md);
        if (ref != null) {
            return ((Method) ref.get());
        }
        return null;
    }

    private static void cacheMethod(MethodDescriptor md, Method method) {
        if (methodCache.size() > 30) {
            methodCache.clear();
        }
        methodCache.put(md, new WeakReference(method));
    }

    private static class MethodDescriptor {
        private Class<?> clazz;
        private String methodName;
        private Class<?>[] paramTypes;
        private boolean exact;
        private int hashCode;

        public MethodDescriptor(Class<?> clazz, String methodName, Class<?>[] paramTypes, boolean exact) {
            if (clazz == null) {
                throw new IllegalArgumentException("Class cannot be null");
            }
            if (methodName == null) {
                throw new IllegalArgumentException("Method Name cannot be null");
            }
            if (paramTypes == null) {
                paramTypes = ReflectionUtil.EMPTY_CLASS_ARRAY;
            }
            this.clazz = clazz;
            this.methodName = methodName;
            this.paramTypes = paramTypes;
            this.exact = exact;
            this.hashCode = (methodName.length() << 16 | paramTypes.length);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof MethodDescriptor)) {
                return false;
            }
            MethodDescriptor md = (MethodDescriptor) obj;
            return ((this.exact == md.exact) && (this.methodName.equals(md.methodName))
                    && (this.clazz.equals(md.clazz)) && (Arrays.equals(this.paramTypes, md.paramTypes)));
        }

        public int hashCode() {
            return this.hashCode;
        }
    }
}
