package com.github.angelndevil2.dsee;

import com.github.angelndevil2.dsee.jetty.JettyServer;
import com.github.angelndevil2.dsee.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @since 1.4.0
 *
 * Created by k on 16. 10. 17.
 */
@Slf4j
public class Jetty {
    private Properties jettyProp;
    private final JettyServer server = new JettyServer();

    public Jetty() {
        initValues();
    }

    public void initValues() {
        try {
            PropertiesUtil.setDirs("src/dist");
        } catch (IOException e) {
            log.error("base directory setting error.", e);
        }

        jettyProp = new Properties();

        try {
            jettyProp.load(new FileInputStream(PropertiesUtil.getJettyPropertiesFile()));
        } catch (IOException e) {
            log.error("error loading jetty properties.", e);
        }
    }

    public int getPort() { return Integer.valueOf(jettyProp.getProperty("http.port")); }

    public void stop() throws Exception {
        server.stop();
    }

    public void start() throws Exception {
        server.run();
    }
}
