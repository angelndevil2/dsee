package com.github.angelndevil2.dsee.context;

import lombok.Getter;
import lombok.Setter;

import java.lang.management.ThreadMXBean;

/**
 * @since 0.0.4
 * Created by k on 16. 6. 16.
 */
public class MBeanContext {

    private static class Singleton {
        private static final MBeanContext context = new MBeanContext();
    }
    public static MBeanContext getInstance() { return Singleton.context; }

    private MBeanContext() {}

    @Getter @Setter
    private ThreadMXBean threadMXBean;
}
