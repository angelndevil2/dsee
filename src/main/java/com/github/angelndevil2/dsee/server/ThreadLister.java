package com.github.angelndevil2.dsee.server;

import com.github.angelndevil2.dsee.Agent;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONAware;

import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

/**
 *
 * @since 0.0.4
 * List all threads in jvm
 *
 * Created by k on 16. 6. 16.
 */
@Slf4j
public class ThreadLister {

    private final ThreadMXBean threadMXBean = Agent.getBeanContext().getThreadMXBean();

    public static class ThreadIdName {
        public long id;
        public String name;

        public ThreadIdName(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /**
     *
     * @return all thread id & name array
     */
    public ArrayList<ThreadIdName> getThreadList() {

        ArrayList<ThreadIdName> threads = new ArrayList<ThreadIdName>();

        for (long id : threadMXBean.getAllThreadIds()) {
            ThreadInfo ti = threadMXBean.getThreadInfo(id);
            threads.add(new ThreadIdName(ti.getThreadId(), ti.getThreadName()));
        }

        return threads;
    }
}
