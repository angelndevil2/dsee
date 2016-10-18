package com.github.angelndevil2.dsee.dstruct;

import com.github.angelndevil2.dsee.ClassInspector;
import lombok.Getter;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author k on 16. 10. 18.
 */
public class ClassList implements JSONAware {

    /**
     * key : file name
     * value : {@link ClassInspector}
     */
    @Getter
    private HashMap<String, ArrayList<ClassInspector>> classes = new HashMap<String, ArrayList<ClassInspector>>();

    public void put(String fn, ClassInspector ci) {

        ArrayList<ClassInspector> list = classes.get(fn);

        if (list == null) {
            list = new ArrayList<ClassInspector>();
            classes.put(fn, list);
        }

        list.add(ci);
    }

    /**
     * @return JSON text
     */
    @Override
    public String toJSONString() {
        return JSONObject.toJSONString(classes);
    }

}
