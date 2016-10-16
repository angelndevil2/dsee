package com.github.angelndevil2.dsee;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.4.0
 *
 * Created by k on 16. 10. 17.
 */
public class JVMPathTest {

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
    public void getClassPath() throws Exception {

        exchange.reset();
        exchange.setURL("http://localhost:"+jetty.getPort()+"/path/class");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            assertEquals(200, exchange.getResponseStatus());
            System.out.println(exchange.getResponseContent());
        }
    }

    @Test
    public void getBootClassPath() throws Exception {

        exchange.reset();
        exchange.setURL("http://localhost:"+jetty.getPort()+"/path/boot");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            assertEquals(200, exchange.getResponseStatus());
            System.out.println(exchange.getResponseContent());
        }

        exchange.reset();
    }

    @Test
    public void getEndorsedClassPath() throws Exception {

        exchange.reset();
        exchange.setURL("http://localhost:"+jetty.getPort()+"/path/endorsed");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            assertEquals(200, exchange.getResponseStatus());
            System.out.println(exchange.getResponseContent());
        }
    }

    @Test
    public void getExtClassPath() throws Exception {

        exchange.reset();
        exchange.setURL("http://localhost:"+jetty.getPort()+"/path/ext");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            assertEquals(200, exchange.getResponseStatus());
            System.out.println(exchange.getResponseContent());
        }
    }
}
