package com.github.angelndevil2.dsee.server.thread;

import com.github.angelndevil2.dsee.Agent;

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
     * dump thread info with thread id
     * @param id thread id
     * @return thread info string
     */
    public String dump(final long id) {

        ThreadInfo ti = null;
        try {
            ThreadInfo[] t = threadMXBean.getThreadInfo(new long[]{id}, true, true);
            if (t != null) ti = t[0];
        } catch (UnsupportedOperationException e) {
            ti = threadMXBean.getThreadInfo(id, Integer.MAX_VALUE);
        }

        return (ti == null ? null:ti.toString());
    }
}
