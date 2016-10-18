package com.github.angelndevil2.dsee.server;

import com.github.angelndevil2.dsee.ClassInspector;
import com.github.angelndevil2.dsee.DseeException;
import com.github.angelndevil2.dsee.dstruct.ClassList;
import com.github.angelndevil2.dsee.util.FileUtil;
import com.github.angelndevil2.dsee.util.JVMUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @since 0.0.4
 *
 * @author  k on 16. 10. 17.
 */
@Slf4j
public class ClassFileManager {

    public ClassFileManager() {
        setBootClasses();
        setUserClasses();
        setEndorsedClasses();
        setExtClasses();
    }

    /**
     * get class fiile name from path.
     *
     * <ul>
     *     <li>
     *         if path is directory, list .class file names, not include directory and not recursive.
     *     </li>
     *     <li>
     *         if path is jar file(only name end with .jar), list .class file names in that jar file.
     *     </li>
     * </ul>
     *
     * @param path class path
     *
     * @return class file name array
     */
    public static ArrayList<String> getClassFileNames(String path) {

        ArrayList<String> ret = new ArrayList<String>();

        try {
            checkArgument(path != null);
        } catch (Throwable t) {
            log.error("path argument is null", t);
        }

        File file = new File(path);
        if (file.exists()) {

            if (!file.isDirectory() && file.getName().endsWith(".jar")) {

                try {
                    JarInputStream jar = new JarInputStream(new FileInputStream(path));

                    for (JarEntry entry = jar.getNextJarEntry(); entry != null; entry = jar.getNextJarEntry()) {
                        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                            // This ZipEntry represents a class. Now, what class does it represent?
                            ret.add(entry.getName());
                        }
                    }

                } catch (IOException e) {
                    log.error("{} : {}", path, e);
                }

            } else if (file.isDirectory()){

                String[] list = file.list();
                if (list != null) for (String name : list) if (name.endsWith(".class")) ret.add(name);
            }
        }

        return ret;
    }

    private void setBootClasses() {
        for (String path : JVMUtil.getBootClassPath()) {
            putClassInspectorFromPathToClassList(path, bootClasses);
        }
    }

    private void setUserClasses() {
        for (String path : JVMUtil.getClassPath()) {
            putClassInspectorFromPathToClassList(path, userClasses);
        }
    }

    private void setEndorsedClasses() {
        for (String path : JVMUtil.getEndorsedPath()) {
            putClassInspectorFromPathToClassList(path, endorsedClasses);
        }
    }

    private void setExtClasses() {
        for (String path : JVMUtil.getExtPath()) {
            putClassInspectorFromPathToClassList(path, extClasses);
        }
    }

    private void putClassInspectorFromPathToClassList(String path, ClassList map) {
        File file = new File(path);

        if (file.exists()) {
            if (!file.isDirectory() && file.getName().endsWith(".jar")) { // if jar

                try {
                    FileUtil.putClassInspectorFromJarPathToClassList(path, map);
                } catch (DseeException e) {
                    log.error("error while read {}.\n{}", path, e);
                }

            } else if (file.isDirectory()) { // if dir

                try {
                    FileUtil.putClassInspectorFromDirectoryToClassList(path, map);
                } catch (DseeException e) {
                    log.error("error while read {}.\n{}", path, e);
                }
            }
        }
    }

    /**
     * key : file name
     * value : {@link ClassInspector}
     */
    @Getter

    private ClassList bootClasses = new ClassList();
    /**
     * key : file name
     * value : {@link ClassInspector}
     */
    @Getter

    private ClassList userClasses = new ClassList();
    /**
     * key : file name
     * value : {@link ClassInspector}
     */
    @Getter

    private ClassList endorsedClasses = new ClassList();
    /**
     * key : file name
     * value : {@link ClassInspector}
     */
    @Getter
    private ClassList extClasses = new ClassList();
}
