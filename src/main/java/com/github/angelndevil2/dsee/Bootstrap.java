package com.github.angelndevil2.dsee;

import lombok.Getter;

import javax.management.MBeanServer;
import javax.naming.InitialContext;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author k, Created on 16. 2. 29.
 */
public class Bootstrap {

    /**
     * save for latter use, original system class loader
     */
    @Getter
    private static ClassLoader orginalSystemClassLoader;
    /**
     * save for latter use, our bootstrapped class loader
     */
    @Getter
    private static ClassLoader bootstrapClassLoader;

    /**
     * Thread to find separated mbean server by Was
     */
    private static final WasContextThread wasContextThread = new WasContextThread();

    private static class WasContextThread extends Thread {

        private final ArrayBlockingQueue<String> q = new ArrayBlockingQueue<String>(1);
        @Getter
        private MBeanServer ms;

        @Override
        public void run() {

            boolean found = false;

            while (!found) {

                try {
                    InitialContext context = new InitialContext();
                    MBeanServer server = (MBeanServer) context.lookup("java:comp/jmx/runtime");
                    if (server == null) server = (MBeanServer) context.lookup("java:comp/env/jmx/runtime");
                    if (server != null) {

                        System.out.println("mbean server found for webLogic with context lookup");
                        ms = server;
                        found = true;
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (found) {
                try {
                    q.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * used with -javaagent:
     * @param options
     * @param instrumentation
     */
    public static void premain(String options, Instrumentation instrumentation) {

        try {

            bootStrap(options);

        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }

    }

    /**
     * used by dynamic attach
     * @param options
     * @param instrumentation
     */
    public static void agentmain(String options, Instrumentation instrumentation) {

        try {

            bootStrap(options);

        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    public static MBeanServer getWasMBeanServer() {
        return wasContextThread.getMs();
    }

    @SuppressWarnings("unchecked")
    private static void bootStrap(String options) throws IOException, NullPointerException {

        // find separated mbean server by was
        wasContextThread.setDaemon(true);
        wasContextThread.start();

        // save original system class loader
        orginalSystemClassLoader = ClassLoader.getSystemClassLoader();

        //
        // Bootstrap
        //

        // add jar file url
        ArrayList<URL> urls = new ArrayList<URL>();

        String jarName = findPathJar(null);
        File[] files = new File(jarName.substring(0, jarName.lastIndexOf("/"))).listFiles();
        for (File f : files) {
            urls.add(f.getAbsoluteFile().toURI().toURL());
        }

        // feed your URLs to a URLClassLoader!
        bootstrapClassLoader =
                new URLClassLoader(
                        urls.toArray(new URL[1]),
                        orginalSystemClassLoader.getParent());

        // well-behaved Java packages work relative to the
        // context classloader.  Others don't (like commons-logging)
        Thread.currentThread().setContextClassLoader(bootstrapClassLoader);

        try {
           Class agent = Class.forName("com.github.angelndevil2.dsee.Agent", false, bootstrapClassLoader);
            Object i = agent.newInstance();
            agent.getMethod("runAgent", String.class).invoke(i, options);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * If the provided class has been loaded from a jar file that is on the local file system, will find the absolute path to that jar file.
     *
     * @param context The jar file that contained the class file that represents this class will be found. Specify {@code null} to let Agent find its own jar.
     * @throws IllegalStateException If the specified class was loaded from a directory or in some other way (such as via HTTP, from a database, or some
     *                               other custom classloading device).
     */
    public static String findPathJar(Class<?> context) throws IllegalStateException {
        if (context == null) context = Bootstrap.class;
        String rawName = context.getName();
        String classFileName;
        /* rawName is something like package.name.ContainingClass$ClassName. We need to turn this into ContainingClass$ClassName.class. */
        {
            int idx = rawName.lastIndexOf('.');
            classFileName = (idx == -1 ? rawName : rawName.substring(idx+1)) + ".class";
        }

        String uri = context.getResource(classFileName).toString();
        if (uri.startsWith("file:")) throw new IllegalStateException("This class has been loaded from a directory and not from a jar file.");
        if (!uri.startsWith("jar:file:")) {
            int idx = uri.indexOf(':');
            String protocol = idx == -1 ? "(unknown)" : uri.substring(0, idx);
            throw new IllegalStateException("This class has been loaded remotely via the " + protocol +
                    " protocol. Only loading from a jar on the local file system is supported.");
        }

        int idx = uri.indexOf('!');
        //As far as I know, the if statement below can't ever trigger, so it's more of a sanity check thing.
        if (idx == -1) throw new IllegalStateException("You appear to have loaded this class from a local jar file, but I can't make sense of the URL!");

        try {
            String fileName = URLDecoder.decode(uri.substring("jar:file:".length(), idx), Charset.defaultCharset().name());
            return new File(fileName).getAbsolutePath();
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("default charset doesn't exist. Your VM is borked.");
        }
    }
}
