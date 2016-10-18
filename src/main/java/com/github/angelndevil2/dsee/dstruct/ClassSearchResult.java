package com.github.angelndevil2.dsee.dstruct;

import com.github.angelndevil2.dsee.iface.IClassInspector;
import com.github.angelndevil2.dsee.iface.IClassSearchResult;
import org.json.simple.JSONArray;

import java.util.ArrayList;

/**
 * @author k on 16. 10. 19.
 */
public class ClassSearchResult implements IClassSearchResult {
    /**
     * @return JSON text
     */
    @Override
    public String toJSONString() { return JSONArray.toJSONString(cList); }

    @Override
    public void put(IClassInspector value) { cList.add(value); }

    @Override
    public boolean add(IClassSearchResult result) {
        return cList.addAll(result.toArrayList());
    }

    @Override
    public ArrayList<IClassInspector> toArrayList() {
        return cList;
    }

    private ArrayList<IClassInspector> cList = new ArrayList<IClassInspector>();
}
