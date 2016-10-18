package com.github.angelndevil2.dsee.iface;

import org.json.simple.JSONAware;

/**
 * @author k on 16. 10. 19.
 */
public interface IClassDb extends JSONAware{

    IClassSearchResult findName(String name);
}
