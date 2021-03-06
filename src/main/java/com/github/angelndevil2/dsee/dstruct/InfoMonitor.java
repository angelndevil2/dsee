package com.github.angelndevil2.dsee.dstruct;

import lombok.Getter;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;

/**
 * Delegate {@link MonitorInfo}
 *
 * @since 0.0.4
 * @author angelndevil2 on 16. 10. 23.
 */
public class InfoMonitor extends InfoLock implements JSONAware {

    private final MonitorInfo monitorInfo;

    public InfoMonitor(final MonitorInfo monitorInfo){
        super(monitorInfo);
        this.monitorInfo = monitorInfo;
    }

    public String toString() {
        return super.toString() + "'\nlocked_stack_depth : " + monitorInfo.getLockedStackDepth() + "\n" +
                "locked_stack_frame : " + monitorInfo.getLockedStackFrame();
    }

    /**
     * @return JSON text
     */
    @Override
    public String toJSONString() {
        return toJSONObject().toJSONString();
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject() {
        JSONObject ret = new JSONObject();
        ret.putAll(super.toJSONObject());
        ret.put("locked_stack_depth", monitorInfo.getLockedStackDepth());
        ret.put("locked_stack_frame", monitorInfo.getLockedStackFrame().toString());

        return ret;
    }

}
