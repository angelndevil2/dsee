package com.github.angelndevil2.dsee.context;

import com.github.angelndevil2.dsee.server.clazz.ClassFileManager;
import lombok.Getter;

/**
 *
 * Created by k on 16. 10. 17.
 */
public class GlobalContext {

    private static class Singleton {
        private static final GlobalContext context = new GlobalContext();
    }
    public static GlobalContext getInstance() { return Singleton.context; }


    private GlobalContext() {
    }

    @Getter
    private final ClassFileManager classFileManager = new ClassFileManager();
}
