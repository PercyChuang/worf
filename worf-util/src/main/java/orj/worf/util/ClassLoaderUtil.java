package orj.worf.util;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public abstract class ClassLoaderUtil {
    public static final String CLASS_FILE_EXTENSION = ".class";

    public static URL getResource(ClassLoader classLoader, String resourcePath) {
        URL resourceURL = null;
        String resolvedName = normalizeResourcePath(resourcePath);
        if (resolvedName != null) {
            resourceURL = classLoader.getResource(resolvedName);
        }
        return resourceURL;
    }

    public static List<URL> getResourcesList(String resourcePath) throws IOException {
        return getResourcesList(currentClassLoader(), resourcePath);
    }

    public static List<URL> getResourcesList(ClassLoader classLoader, String resourcePath) throws IOException {
        String resolvedName = normalizeResourcePath(resourcePath);
        if (resolvedName != null) {
            Enumeration resources = classLoader.getResources(resolvedName);
            List resourcesList = Lists.newLinkedList();
            while (resources.hasMoreElements()) {
                URL resource = (URL) resources.nextElement();
                resourcesList.add(resource);
            }
            return Collections.unmodifiableList(resourcesList);
        }
        return Collections.emptyList();
    }

    public static URL getResource(String resourcePath) {
        return getResource(currentClassLoader(), resourcePath);
    }

    public static String normalizeResourcePath(String resourcePath) {
        return normalizae(resourcePath, 1);
    }

    public static String normalizeClassName(String classCanonicalName) {
        return normalizae(classCanonicalName, 2);
    }

    public static String normalizePackageName(String packageName) {
        return normalizae(packageName, 3);
    }

    static String normalizae(String name, int mode) {
        String normalizedName = name;
        if (normalizedName == null) {
            return normalizedName;
        }
        normalizedName = normalizedName.trim();
        while (normalizedName.contains("\\")) {
            normalizedName = normalizedName.replace('\\', '/');
        }
        while (normalizedName.contains("//")) {
            normalizedName = StringUtils.replace(normalizedName, "//", "/");
        }
        while (normalizedName.startsWith("/")) {
            normalizedName = normalizedName.substring(1);
        }
        switch (mode) {
            case 1:
                break;
            case 2:
                if (!(normalizedName.endsWith(".class"))) {
                    break;
                }
                normalizedName = StringUtils.substringBefore(normalizedName, ".class");
                break;
            case 3:
                normalizedName = StringUtils.replace(normalizedName, "/", ".");

                if (!(normalizedName.endsWith(".")))
                    break;
                normalizedName = normalizedName.substring(0, normalizedName.length() - 1);
        }

        return normalizedName;
    }

    public static ClassLoader currentClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = getThreadContextClassLoader();
        } catch (Exception ignored) {
        }
        if (classLoader == null) {
            Class currentClass = ClassLoaderUtil.class;
            classLoader = currentClass.getClassLoader();
        }
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }

    public static ClassLoader getThreadContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Package getPackage(String packageName) throws RuntimeException {
        String name = normalizePackageName(packageName);
        return Package.getPackage(name);
    }

    public static File findClassFile(Class<?> sourceClass) {
        ClassLoader classLoader = sourceClass.getClassLoader();
        String classFileRelativePath = getClassFileRelativePath(sourceClass);
        if (classLoader == null) {
            return null;
        }
        URL resourceURL = getResource(classLoader, classFileRelativePath);
        if (!("file".equals(resourceURL.getProtocol()))) {
            return null;
        }
        return new File(resourceURL.getPath());
    }

    public static String findClassFileAbsolutePath(Class<?> sourceClass) {
        File classFile = findClassFile(sourceClass);
        return ((classFile == null) ? null : classFile.getAbsolutePath());
    }

    public static String getClassFileRelativePath(Class<?> sourceClass) {
        String className = sourceClass.getName();
        String relativePath = "/" + className.replace('.', '/') + ".class";
        return relativePath;
    }

    public static Class<?> loadClass(ClassLoader classLoader, String className) {
        String resolvedClassName = normalizeClassName(className);
        try {
            return classLoader.loadClass(resolvedClassName);
        } catch (Throwable ignored) {
        }
        return null;
    }

    public static Class<?> findLoadedClass(ClassLoader classLoader, String className) {
        String normalizedClassName = normalizeClassName(className);
        Method method = ReflectionUtil.getMethod(ClassLoader.class, "findLoadedClass", new Class[] { String.class });
        Class loadedClass = null;
        try {
            method.setAccessible(true);
            loadedClass = (Class) method.invoke(classLoader, new Object[] { normalizedClassName });
        } catch (Exception ignored) {
        }
        return loadedClass;
    }

    public static Class<?> findClass(ClassLoader classLoader, File classFile) {
        Class klass = null;
        ClassLoader targetClassLoader = classLoader;
        while (targetClassLoader != null) {
            URL classPath = getResource(targetClassLoader, "/");
            if (classPath != null) {
                String protocol = classPath.getProtocol();
                if ("file".equals(protocol)) {
                    String classFileAbsolutePath = classFile.getAbsolutePath();
                    String classPathAbsolutePath = new File(classPath.getFile()).getAbsolutePath();
                    if (classFileAbsolutePath.contains(classPathAbsolutePath)) {
                        String classFileRelativePath = classFileAbsolutePath.replace(classPathAbsolutePath, "");
                        String className = normalizeClassName(classFileRelativePath);
                        try {
                            klass = classLoader.loadClass(className);
                        } catch (ClassNotFoundException ignored) {
                        }
                    }
                }
            }
            if (klass != null) {
                break;
            }
            targetClassLoader = targetClassLoader.getParent();
        }

        return klass;
    }

    public static String asPackageResourceName(String packageName) {
        String normalizedPackageName = normalizePackageName(packageName);
        String packagePathInJar = normalizedPackageName.replace('.', '/').concat("/");
        return packagePathInJar;
    }
}
