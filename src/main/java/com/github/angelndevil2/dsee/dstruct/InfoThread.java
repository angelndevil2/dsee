package com.github.angelndevil2.dsee.dstruct;

import com.github.angelndevil2.dsee.util.thread.ThreadUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;

/**
 * Delegate {@link ThreadInfo}
 *
 * @since 0.0.4
 * @author angelndevil2 on 16. 10. 23.
 */
public class InfoThread implements JSONAware {

    private final ThreadInfo threadInfo;

    public InfoThread(final ThreadInfo ti) {
        threadInfo = ti;
    }

    /**
     *
     * @return {@link MonitorInfo}'s string representation
     */
    public String toStringLockedMonitors() {

        final StringBuilder builder = new StringBuilder();

        final InfoMonitor[] infoMonitors;
        final MonitorInfo[] lockedMonitors = threadInfo.getLockedMonitors();
        if (lockedMonitors.length > 0) {

            infoMonitors = new InfoMonitor[lockedMonitors.length];
            int idx = 0;
            for (MonitorInfo mi : lockedMonitors) {
                infoMonitors[idx] = new InfoMonitor(mi);
                builder.append(infoMonitors[idx].toString()).append("\n");
                idx++;
            }

            return builder.toString();
        }

        return null;
    }

    /**
     * @return JSON Object
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject() {


        final JSONObject ret = new JSONObject();

        ret.put("id", threadInfo.getThreadId());
        ret.put("name", threadInfo.getThreadName());
        ret.put("state", threadInfo.getThreadState().toString());

        ret.put("blocked_count",threadInfo.getBlockedCount());
        ret.put("blocked_time", threadInfo.getBlockedTime());

        ret.put("lock_name", threadInfo.getLockName());
        ret.put("lock_owner_id",threadInfo.getLockOwnerId());
        ret.put("lock_owner_name",threadInfo.getLockOwnerName());

        ret.put("waited_count", threadInfo.getWaitedCount());
        ret.put("waited_time", threadInfo.getWaitedTime());

        ret.put("in_native",threadInfo.isInNative());
        ret.put("suspended",threadInfo.isSuspended());

        ret.put("locked_monitors", getLockedMonitors());
        ret.put("locked_synchronizers", getLockedSynchronizers());

        JSONArray st = new JSONArray();
        for (StackTraceElement se : threadInfo.getStackTrace()) {
            st.add(se.toString());
        }
        ret.put("stack_trace", st);

        return ret;
    }

    /**
     * @return JSON text
     */
    public String toJSONString() {
        return toJSONObject().toJSONString();
    }

    /**
     *
     * @return {@link InfoMonitor} array list
     */
    @SuppressWarnings("unchecked")
    public JSONArray getLockedMonitors() {

        final JSONArray infoMonitors = new JSONArray();
        final MonitorInfo[] lockedMonitors = threadInfo.getLockedMonitors();
        for (MonitorInfo mi : lockedMonitors) infoMonitors.add(new InfoMonitor(mi));
        return infoMonitors;
    }

    /**
     *
     * @return {@link InfoLock} array  list, ach of which represents an ownable synchronizer currently locked by the thread associated with this ThreadInfo
     */
    @SuppressWarnings("unchecked")
    public JSONArray getLockedSynchronizers() {
        final JSONArray infoLocks = new JSONArray();
        final LockInfo[] locks = threadInfo.getLockedSynchronizers();
        for (LockInfo li : locks) infoLocks.add(new InfoLock(li));
        return infoLocks;
    }

}
