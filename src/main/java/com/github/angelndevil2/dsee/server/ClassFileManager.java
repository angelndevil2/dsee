package com.github.angelndevil2.dsee.server;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @since 0.0.4
 *
 * Created by k on 16. 10. 17.
 */
@Slf4j
public class ClassFileManager {

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
    public ArrayList<String> getClassFileNames(String path) {

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
                            log.debug(entry.getName());
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
}
