package com.github.angelndevil2.dsee.server;

import com.github.angelndevil2.dsee.Bootstrap;
import com.github.angelndevil2.dsee.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import javax.management.*;
import javax.management.MBeanServer;
import java.io.Serializable;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author k, Created on 16. 2. 22.
 */
@Slf4j
public class MBeanServerFactory implements Serializable {

    private static final long serialVersionUID = -2881069740095622627L;
    private final HashMap<String, com.github.angelndevil2.dsee.server.MBeanServer> servers
            = new HashMap<String, com.github.angelndevil2.dsee.server.MBeanServer>();

    public MBeanServerFactory() {
        restoreMBeanServers();
    }

    /**
     *
     * @param agentId
     * @return
     */
    public ArrayList<com.github.angelndevil2.dsee.server.MBeanServer> findMBeanServer(final String agentId) {
        restoreMBeanServers();
        if (agentId == null) {
            return newArrayList(servers.values());
        }
        return newArrayList(servers.get(agentId));
    }

    /**
     * @see java.lang.management.ManagementFactory
     * @return mbean server id array
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getAllMBeanServerId() {
        ArrayList<String> ids = new ArrayList<String>();
        for (com.github.angelndevil2.dsee.server.MBeanServer ms : findMBeanServer(null)) {
            ids.add(ms.getServerId());
        }

        return ids;
    }

    /**
     *
     * @param serverId mbean server id
     * @param name object name
     * @param queryExp
     * @return object name set
     * @throws InstanceNotFoundException
     */
    public Set<ObjectName> queryNames(final String serverId, final ObjectName name, final QueryExp queryExp)
            throws InstanceNotFoundException {
        return getMBeanServer(serverId).queryNames(name, queryExp);
    }

    /**
     *
     * @param serverId mbean server id
     * @param name object name
     * @param queryExp
     * @return object name set
     * @throws MalformedObjectNameException
     * @throws InstanceNotFoundException
     */
    public Set<ObjectName> queryNames(final String serverId, final String name, final QueryExp queryExp)
            throws MalformedObjectNameException, InstanceNotFoundException {
        return getMBeanServer(serverId).queryNames(new ObjectName(name), queryExp);

    }

    /**
     * Get all domains from mbean server whose id is serverId
     * @param serverId mbean server id
     * @return domain string array
     * @throws InstanceNotFoundException
     */
    public String[] getDomains(final String serverId) throws InstanceNotFoundException {
        return getMBeanServer(serverId).getDomains();
    }

    /**
     *
     * @param serverId mbean server id
     * @param name object name
     * @return attribute object
     * @throws InstanceNotFoundException
     * @throws IntrospectionException
     */
    public MBeanInfo getMBeanInfo(final String serverId, final ObjectName name)
            throws InstanceNotFoundException, IntrospectionException, ReflectionException {
        return getMBeanServer(serverId).getMBeanInfo(name);
    }

    public Object getMBeanAttribute(final String serverId, final ObjectName name, final String attribute)
            throws InstanceNotFoundException, MBeanException, AttributeNotFoundException, ReflectionException {

        return getMBeanServer(serverId).getAttribute(name, attribute);

    }

    /**
     * Get the MBeanServerId of Agent ID for the provided MBeanServer.
     *
     * @param aMBeanServer MBeanServer whose Server ID/Agent ID is desired.
     * @return MBeanServerId/Agent ID of provided MBeanServer.
     */
    static String getMBeanServerId(final MBeanServer aMBeanServer)
    {
        String serverId = null;
        final String SERVER_DELEGATE = "JMImplementation:type=MBeanServerDelegate";
        final String MBEAN_SERVER_ID_KEY = "MBeanServerId";
        try
        {
            ObjectName delegateObjName = new ObjectName(SERVER_DELEGATE);
            serverId = (String) aMBeanServer.getAttribute( delegateObjName,
                    MBEAN_SERVER_ID_KEY );
        }
        catch (MalformedObjectNameException malformedObjectNameException)
        {
            log.error(  "Problems constructing MBean ObjectName: {}",
                    malformedObjectNameException.getMessage() );
        }
        catch (AttributeNotFoundException noMatchingAttrException)
        {
            log.error(  "Unable to find attribute {} in MBean {} : {}",MBEAN_SERVER_ID_KEY,
                    SERVER_DELEGATE, noMatchingAttrException );
        }
        catch (MBeanException mBeanException)
        {
            log.error(  "Exception thrown by MBean's ({}'s {}) getter: {}",SERVER_DELEGATE
                    ,MBEAN_SERVER_ID_KEY, mBeanException.getMessage() );
        }
        catch (ReflectionException reflectionException)
        {
            log.error(  "Exception thrown by MBean's ({}'s ) setter: {}", SERVER_DELEGATE
                    ,MBEAN_SERVER_ID_KEY, reflectionException.getMessage() );
        }
        catch (InstanceNotFoundException noMBeanInstance)
        {
            log.error(  "No instance of MBean {} found in MBeanServer: {}", SERVER_DELEGATE
                    ,noMBeanInstance.getMessage() );
        }
        return serverId;
    }

    private com.github.angelndevil2.dsee.server.MBeanServer
    getMBeanServer(final String serverId) throws InstanceNotFoundException {
        com.github.angelndevil2.dsee.server.MBeanServer server = servers.get(serverId);
        if (server == null) {
            ArrayList<com.github.angelndevil2.dsee.server.MBeanServer> list = findMBeanServer(serverId);
            if (list == null || list.size() < 1) {
                servers.remove(serverId);
                throw new InstanceNotFoundException("mbean server(" + serverId + ") is not exist.");
            }
            if (list.size() > 1) {
                log.debug("{} has more than one mbean server. is it possible?", serverId);
            }
            server = list.get(0);
            servers.put(serverId, server);
        }
        return server;
    }

    private void restoreMBeanServers() {
        for (MBeanServer ms : javax.management.MBeanServerFactory.findMBeanServer(null)) {
            servers.put(getMBeanServerId(ms),
                    new com.github.angelndevil2.dsee.server.MBeanServer(ms));
        }

        if (PropertiesUtil.isWebLogic()) {
            log.debug("may be webLogic ?");
            //
            // check mbean server registered
            //
            boolean registered = false;
            for (com.github.angelndevil2.dsee.server.MBeanServer ms : servers.values()) {
                for (String domain:ms.getDomains()) {
                    if (domain.startsWith("com.bea")) {
                        registered = true;
                        break;
                    }
                }
                if (registered) break;
            }

            if (!registered) {

                try {

                    MBeanServer ms = Bootstrap.getWasMBeanServer();
                    servers.put(getMBeanServerId(ms),
                            new com.github.angelndevil2.dsee.server.MBeanServer(ms));

                } catch (Throwable t) {

                    log.debug("context lookup failed. sure webLogic ?", t);
                }
            }
        }

        if (servers.size() == 0) log.info("no mbean server exist. 'com.sun.management.jmxremote' property will be help.");
    }

}
