package com.github.angelndevil2.dsee.dstruct;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.lang.management.LockInfo;

/**
 * Delegate {@link LockInfo}
 *
 * @since 0.0.4
 * @author angelndevil2 on 16. 10. 24.
 */
public class InfoLock implements JSONAware {

    private final LockInfo lockInfo;

    public InfoLock(final LockInfo lockInfo) {
        this.lockInfo = lockInfo;
    }

    /**
     * @return JSON Object
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject() {
        JSONObject ret = new JSONObject();

        ret.put("class_name", lockInfo.getClassName());
        ret.put("hash_code", lockInfo.getIdentityHashCode());

        return ret;
    }

    /**
     * @return JSON text
     */
    @Override
    public String toJSONString() {
        return toJSONObject().toJSONString();
    }
}
