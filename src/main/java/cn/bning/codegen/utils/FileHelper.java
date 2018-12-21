package cn.bning.codegen.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Bernix Ning
 * @date 2018-12-18
 */
public class FileHelper {

    public static File getFile(String file) {
        if (StringUtils.isBlank(file)) {
            throw new IllegalArgumentException("'file' must be not blank");
        }
        try {
            return getFileByClassLoader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.toString(), e);
        } catch (IOException e) {
            throw new RuntimeException("getFile() error,file:" + file, e);
        }
    }

    public static File getFileByClassLoader(String resourceName) throws IOException {
        String pathToUse = resourceName;
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        Enumeration<URL> urls = getDefaultClassLoader().getResources(pathToUse);
        while (urls.hasMoreElements()) {
            return new File(urls.nextElement().getFile());
        }
        urls = FileHelper.class.getClassLoader().getResources(pathToUse);
        while (urls.hasMoreElements()) {
            return new File(urls.nextElement().getFile());
        }
        urls = ClassLoader.getSystemResources(pathToUse);
        while (urls.hasMoreElements()) {
            return new File(urls.nextElement().getFile());
        }
        throw new FileNotFoundException("classpath:" + resourceName);
    }

    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p>Call this method if you intend to use the thread context ClassLoader
     * in a scenario where you absolutely need a non-null ClassLoader reference:
     * for example, for class path resource loading (but not necessarily for
     * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
     * reference as well).
     *
     * @return the default ClassLoader (never <code>null</code>)
     * @see java.lang.Thread#getContextClassLoader()
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = FileHelper.class.getClassLoader();
        }
        return cl;
    }
}
