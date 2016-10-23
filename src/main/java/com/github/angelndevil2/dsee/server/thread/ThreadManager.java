package com.github.angelndevil2.dsee.server.thread;

import com.github.angelndevil2.dsee.Agent;
import com.github.angelndevil2.dsee.dstruct.InfoThread;

import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

/**
 *
 * Created by k on 16. 6. 16.
 */
public class ThreadManager {

    public static class ThreadIdName {
        public final long id;
        public final String name;

        public ThreadIdName(final long id, final String name) {
            this.id = id;
            this.name = name;
        }
    }

    private final ThreadMXBean threadMXBean = Agent.getBeanContext().getThreadMXBean();


    /**
     *
     * @return all thread id & name array
     */
    public ArrayList<ThreadIdName> getThreadList() {

        final ArrayList<ThreadIdName> threads = new ArrayList<ThreadIdName>();

        for (long id : threadMXBean.getAllThreadIds()) {
            final ThreadInfo ti = threadMXBean.getThreadInfo(id);
            threads.add(new ThreadIdName(ti.getThreadId(), ti.getThreadName()));
        }

        return threads;
    }

    /**
     * String of thread info with thread id
     *
     * @since 0.0.4
     * @param id thread id
     * @return thread info string
     */
    public String toString(final long id) {

        ThreadInfo ti = getThreadInfo(id);
        return (ti == null ? null:ti.toString());
    }

    /**
     * JSONString of thread info with thread id
     *
     * @since 0.0.4
     * @param id thread id
     * @return thread info json string
     */
    public String toJSONString(final long id) {
        ThreadInfo ti = getThreadInfo(id);
        if (ti == null) return null;

        InfoThread it = new InfoThread(ti);
        return it.toJSONString();
    }


    /**
     * get thread info with lock & monitor & stack trace if possible
     *
     * @since 0.0.4
     * @param id thread id
     * @return thread info with lock & monitor & stack trace if possible
     */
    public ThreadInfo getThreadInfo(final long id) {
        ThreadInfo ti = null;
        try {
            ThreadInfo[] t = threadMXBean.getThreadInfo(new long[]{id}, true, true);
            if (t != null) ti = t[0];
        } catch (UnsupportedOperationException e) {
            ti = threadMXBean.getThreadInfo(id, Integer.MAX_VALUE);
        }

        return ti;
    }
}
