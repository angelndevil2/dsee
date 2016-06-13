package com.github.angelndevil2.dsee.server;

import lombok.Getter;

import javax.management.*;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Set;

/**
 * @author k, Created on 16. 2. 22.
 */
public class MBeanServer implements Serializable {

    private static final long serialVersionUID = -680104697021234725L;

    private transient WeakReference<javax.management.MBeanServer> server;
    @Getter
    private final String serverId;

    public MBeanServer(javax.management.MBeanServer server) {
        this.server = new WeakReference<javax.management.MBeanServer>(server);
        serverId = MBeanServerFactory.getMBeanServerId(server);

    }

    /**
     * @param name object name
     * @param queryExp
     * @return object name set
     */
    public Set<ObjectName> queryNames(final ObjectName name, final QueryExp queryExp) {
        return getMBeanServer().queryNames(name, queryExp);
    }

    /**
     * @return domain string array
     */
    public String[] getDomains() {
        return getMBeanServer().getDomains();
    }

    public MBeanInfo getMBeanInfo(final ObjectName name)
            throws IntrospectionException, InstanceNotFoundException, ReflectionException {
        return getMBeanServer().getMBeanInfo(name);
    }

    public Object getAttribute(final ObjectName oname, final String name)
            throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {
        return getMBeanServer().getAttribute(oname, name);
    }

    /**
     * @since 0.0.3
     * @return default domain string
     */
    public String getDefaultDomain() {
        return getMBeanServer().getDefaultDomain();
    }

    private javax.management.MBeanServer getMBeanServer() throws NullPointerException {
        return this.server.get();
    }
}
