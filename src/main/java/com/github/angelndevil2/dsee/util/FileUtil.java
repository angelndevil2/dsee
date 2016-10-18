package com.github.angelndevil2.dsee.util;

import com.github.angelndevil2.dsee.ClassInspector;
import com.github.angelndevil2.dsee.DseeException;
import com.github.angelndevil2.dsee.iface.IClassList;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author k on 16. 10. 18.
 */
public class FileUtil {

    public static byte[] toByte(InputStream in) throws DseeException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[4096];

        try {
            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        } catch (IOException e) {
            throw new DseeException("error while reading InputStream", e);
        }

        return buffer.toByteArray();
    }

    public static void putClassInspectorFromJarPathToClassList(String category, String path, IClassList cList) throws DseeException {

        JarFile file;
        try {
            file = new JarFile(path);
        } catch (IOException e) {
            throw new DseeException("can not read " + path, e);
        }

        Enumeration<JarEntry> entries = file.entries();
        if (entries != null) {
            while (entries.hasMoreElements()) {

                JarEntry entry = entries.nextElement();

                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    InputStream is;
                    try {
                         is = file.getInputStream(entry);
                        cList.put(file.getName(), new ClassInspector(category, path, FileUtil.toByte(is)));
                        is.close();
                    } catch (IOException e) {
                        throw new DseeException("can not read "+entry.getName()+" in "+file.getName(), e);
                    }
                }
            }
        }
    }


    public static void putClassInspectorFromDirectoryToClassList(String category, String dir, IClassList cList) throws DseeException {

        File file = new File(dir);

        File[] list = file.listFiles();
        if (list != null) for (File f : list) {
            if (f.getName().endsWith(".class")) putClassInspectorFromFileToClassList(category, f, cList);
            else if (f.isDirectory()) putClassInspectorFromDirectoryToClassList(category, f.getAbsolutePath(), cList);
        }
    }

    public static void putClassInspectorFromFileToClassList(String category, File file, IClassList cList) throws DseeException {

        InputStream in;
        try {
            in = new FileInputStream(file);
            cList.put(file.getAbsolutePath(), new ClassInspector(category, file.getAbsolutePath(), FileUtil.toByte(in)));
            in.close();
        } catch (FileNotFoundException e) {
            throw new DseeException("can not find " + file.getName(), e);
        } catch (IOException e) {
            throw new DseeException("can not close " + file.getName(), e);
        }
    }
}
