package com.github.angelndevil2.dsee;

import com.github.angelndevil2.dsee.server.ClassFileManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

/**
 * @author k on 16. 10. 18.
 */
public class ClassFileManagerTest {

    private static ClassFileManager cm = new ClassFileManager();

    @Test
    public void printBootClasses() throws ParseException {


        JSONParser parser = new JSONParser();

        JSONObject ja = (JSONObject) parser.parse(cm.getBootClasses().toJSONString());

        for (Object k : ja.keySet()) {
            System.out.println("-------------------------------------");
            System.out.println(k);
            System.out.println("-------------------------------------");
            for (Object o : (JSONArray)ja.get(k)) System.out.println("\t"+o);
            System.out.println("-------------------------------------");

            break;
        }
    }
}
