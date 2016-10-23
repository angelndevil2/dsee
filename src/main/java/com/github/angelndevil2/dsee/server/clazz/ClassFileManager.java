package com.github.angelndevil2.dsee.server.clazz;

import com.github.angelndevil2.dsee.DseeException;
import com.github.angelndevil2.dsee.dstruct.ClassList;
import com.github.angelndevil2.dsee.dstruct.ClassSearchResult;
import com.github.angelndevil2.dsee.iface.IClassDb;
import com.github.angelndevil2.dsee.iface.IClassList;
import com.github.angelndevil2.dsee.iface.IClassSearchResult;
import com.github.angelndevil2.dsee.util.FileUtil;
import com.github.angelndevil2.dsee.util.JVMUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static com.github.angelndevil2.dsee.dstruct.ClassList.*;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * @since 0.0.4
 *
 * @author  k on 16. 10. 17.
 */
@Slf4j
public class ClassFileManager implements IClassDb {

    public ClassFileManager() {
        setBootClasses();
        setUserClasses();
        setEndorsedClasses();
        setExtClasses();
    }

    @Override
    public IClassSearchResult findName(String name) {

        IClassSearchResult result = new ClassSearchResult();
        for (String key : classes.keySet()) {
            result.add(classes.get(key).findName(name));
        }
        return result;
    }

    /**
     * @return JSON text
     */
    @Override
    public String toJSONString() {
        return JSONObject.toJSONString(classes);
    }

    /**
     * get class file name from path.
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
            putClassInspectorFromPathToClassList(IClassList.BOOT_CLASS, path, bootClasses);
        }

        classes.put(BOOT_CLASS, bootClasses);
    }

    private void setUserClasses() {
        for (String path : JVMUtil.getClassPath()) {
            putClassInspectorFromPathToClassList(IClassList.USER_CLASS, path, userClasses);
        }

        classes.put(USER_CLASS, userClasses);
    }

    private void setEndorsedClasses() {
        for (String path : JVMUtil.getEndorsedPath()) {
            putClassInspectorFromPathToClassList(IClassList.ENDORSED_CLASS, path, endorsedClasses);
        }

        classes.put(ENDORSED_CLASS, endorsedClasses);
    }

    private void setExtClasses() {
        for (String path : JVMUtil.getExtPath()) {
            putClassInspectorFromPathToClassList(IClassList.EXT_CLASS, path, extClasses);
        }

        classes.put(EXT_CLASS, extClasses);
    }

    private void putClassInspectorFromPathToClassList(String category, String path, IClassList map) {
        File file = new File(path);

        if (file.exists()) {
            if (!file.isDirectory() && file.getName().endsWith(".jar")) { // if jar

                try {
                    FileUtil.putClassInspectorFromJarPathToClassList(category, path, map);
                } catch (DseeException e) {
                    log.error("error while read {}.\n{}", path, e);
                }

            } else if (file.isDirectory()) { // if dir

                try {
                    FileUtil.putClassInspectorFromDirectoryToClassList(category, path, map);
                } catch (DseeException e) {
                    log.error("error while read {}.\n{}", path, e);
                }
            }
        }
    }

    @Getter

    private IClassList bootClasses = new ClassList(IClassList.BOOT_CLASS);
    @Getter

    private IClassList userClasses = new ClassList(IClassList.USER_CLASS);
    @Getter

    private IClassList endorsedClasses = new ClassList(IClassList.ENDORSED_CLASS);
    @Getter
    private IClassList extClasses = new ClassList(IClassList.EXT_CLASS);

    /**
     * key : {@link IClassList#BOOT_CLASS}, {@link IClassList#EXT_CLASS}, {@link IClassList#USER_CLASS}, {@link IClassList#ENDORSED_CLASS}
     * value : {@link IClassList}
     */
    @Getter
    private HashMap<String, IClassList> classes = new HashMap<String, IClassList>();

}
