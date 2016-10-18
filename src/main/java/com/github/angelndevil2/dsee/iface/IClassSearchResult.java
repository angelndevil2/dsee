package com.github.angelndevil2.dsee.iface;

import org.json.simple.JSONAware;

import java.util.ArrayList;

/**
 * @author k on 16. 10. 19.
 */
public interface IClassSearchResult extends JSONAware {

    void put(IClassInspector value);
    boolean add(IClassSearchResult result);
    ArrayList<IClassInspector> toArrayList();
}
