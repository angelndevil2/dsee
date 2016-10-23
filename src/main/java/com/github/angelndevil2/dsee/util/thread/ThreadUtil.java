package com.github.angelndevil2.dsee.util.thread;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @since 0.0.4
 * @author angelndevil2 on 16. 10. 23.
 */
public class ThreadUtil {

    public static String getStackTrace(final Throwable t) {
        if (t != null) {
            StringWriter sr = new StringWriter();
            PrintWriter pr = new PrintWriter(sr);
            t.printStackTrace(pr);
            return sr.toString();
        }
        return null;
    }

    public static String getStackTrace(final StackTraceElement[] se) {
        if (se != null) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement e: se) sb.append("\t").append(e.toString()).append("\n");
            return sb.toString();
        }
        return null;
    }

}
