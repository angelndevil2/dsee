package com.github.angelndevil2.dsee.util;

import java.io.File;

/**
 * @since 1.4.0
 *
 * Created by k on 16. 10. 17.
 */
public class JVMUtil {

    public static final String BOOT_CLASS_PATH = "sun.boot.class.path";
    public static final String CLASS_PATH = "java.class.path";
    public static final String EXT_PATHS = "java.ext.dirs";
    public static final String ENDORSED_PATHS  = "java.endorsed.dirs";


    public static String[] getBootClassPath() {
        return returnPath(System.getProperty(BOOT_CLASS_PATH));
    }

    public static String[] getClassPath() {
        return returnPath(System.getProperty(CLASS_PATH));
    }

    public static String[] getExtPath() {
        return returnPath(System.getProperty(EXT_PATHS));
    }

    public static String[] getEndorsedPath() {
        return returnPath(System.getProperty(ENDORSED_PATHS));
    }

    private static String[] returnPath(String path) {
        if (path == null) return null;
        return path.split(File.pathSeparator);
    }
}
