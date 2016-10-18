package com.github.angelndevil2.dsee;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 *
 * Created by k on 16. 10. 17.
 */
public class ClassFilesTest {

    static Jetty jetty = new Jetty();
    static HttpClient client = new HttpClient();
    static ContentExchange exchange = new ContentExchange(true);

    @BeforeClass
    public static void startJettyServer() throws Exception {
        jetty.start();
        client.start();
    }

    @AfterClass
    public static void stopJettyServer() throws Exception {
        jetty.stop();
    }

    @Test
    public void getClassFiles() throws Exception {

        exchange.reset();
        exchange.setURL("http://localhost:"+jetty.getPort()+"/class-files/user");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) print();

        exchange.reset();
    }

    @Test
    public void getBootClassPath() throws Exception {

        exchange.reset();
        exchange.setURL("http://localhost:"+jetty.getPort()+"/class-files/boot");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) print();

        exchange.reset();
    }

    @Test
    public void getEndorsedClassPath() throws Exception {

        exchange.reset();
        exchange.setURL("http://localhost:"+jetty.getPort()+"/class-files/endorsed");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) print();

        exchange.reset();

    }

    @Test
    public void getExtClassPath() throws Exception {

        exchange.reset();
        exchange.setURL("http://localhost:"+jetty.getPort()+"/class-files/ext");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) print();

        exchange.reset();
    }

    private void print() throws UnsupportedEncodingException, ParseException {
        assertEquals(200, exchange.getResponseStatus());

        JSONParser parser = new JSONParser();

        JSONObject ja = (JSONObject) parser.parse(exchange.getResponseContent());

        for (Object k : ja.keySet()) {
            System.out.println("-------------------------------------");
            System.out.println(k);
            System.out.println("-------------------------------------");
            for (Object o : (JSONArray)ja.get(k)) System.out.println("\t"+o);
            System.out.println("-------------------------------------");
        }
    }
}
