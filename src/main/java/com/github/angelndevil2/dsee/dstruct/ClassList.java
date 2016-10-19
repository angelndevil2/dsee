package com.github.angelndevil2.dsee.dstruct;

import com.github.angelndevil2.dsee.server.ClassInspector;
import com.github.angelndevil2.dsee.iface.IClassInspector;
import com.github.angelndevil2.dsee.iface.IClassList;
import com.github.angelndevil2.dsee.iface.IClassSearchResult;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author k on 16. 10. 18.
 */
public class ClassList implements IClassList {

    public ClassList(String category) { this.category = category; }
    /**
     * key : file name
     * value : {@link ClassInspector}
     */
    @Getter
    private HashMap<String, ArrayList<IClassInspector>> classes = new HashMap<String, ArrayList<IClassInspector>>();

    @Override
    public IClassSearchResult findName(String name) {

        IClassSearchResult result = new ClassSearchResult();

        for (String fn : classes.keySet()) {
            for (IClassInspector ci : classes.get(fn)) if (ci.getName().contains(name)) result.put(ci);
        }
        return result;
    }

    public void put(String fn, IClassInspector ci) {

        ArrayList<IClassInspector> list = classes.get(fn);

        if (list == null) {
            list = new ArrayList<IClassInspector>();
            classes.put(fn, list);
        }

        list.add(ci);

        inspectorCount++;
    }

    /**
     *
     * @return count of ClassInspectors
     */
    public int size() {
        return inspectorCount;
    }

    /**
     * @return JSON text
     */
    @Override
    public String toJSONString() {
        return JSONObject.toJSONString(classes);
    }

    private int inspectorCount = 0;
    private String category;

}
