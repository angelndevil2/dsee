package com.github.angelndevil2.dsee.iface;

import org.json.simple.JSONAware;

/**
 * @author k on 16. 10. 19.
 */
public interface IClassList extends JSONAware {

    public static final String BOOT_CLASS = "boot";
    public static final String EXT_CLASS = "ext";
    public static final String ENDORSED_CLASS = "user";
    public static final String USER_CLASS = "user";

    IClassSearchResult findName(String name);
    void put(String fn, IClassInspector value);
}
