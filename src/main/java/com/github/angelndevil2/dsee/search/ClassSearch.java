package com.github.angelndevil2.dsee.search;

import com.github.angelndevil2.dsee.context.GlobalContext;
import com.github.angelndevil2.dsee.iface.IClassDb;
import com.github.angelndevil2.dsee.iface.IClassSearch;
import com.github.angelndevil2.dsee.iface.IClassSearchResult;

/**
 * @author k on 16. 10. 19.
 */
public class ClassSearch implements IClassSearch{
    @Override
    public IClassSearchResult findName(String name) {
        return db.findName(name);
    }

    private IClassDb db = GlobalContext.getInstance().getClassFileManager();
}
