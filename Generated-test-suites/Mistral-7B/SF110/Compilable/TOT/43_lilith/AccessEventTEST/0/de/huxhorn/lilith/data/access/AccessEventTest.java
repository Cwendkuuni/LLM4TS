package de.huxhorn.lilith.data.access;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class AccessEventTest {

    @Test
    public void testAccessEventWithValidProperties() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer";
        String remoteAddress = "192.168.1.1";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
        int statusCode = 200;

        event.setTimeStamp(timeStamp);
        event.setLoggerContext(loggerContext);
        event.setRequestURI(requestURI);
        event.setRequestURL(requestURL);
        event.setRemoteHost(remoteHost);
        event.setRemoteUser(remoteUser);
        event.setProtocol(protocol);
        event.setMethod(method);
        event.setServerName(serverName);
        event.setRemoteAddress(remoteAddress);
        event.setRequestHeaders(requestHeaders);
        event.setResponseHeaders(responseHeaders);
        event.setRequestParameters(requestParameters);
        event.setLocalPort(localPort);
        event.setStatusCode(statusCode);

        Assert.assertEquals(timeStamp, event.getTimeStamp());
        Assert.assertEquals(loggerContext, event.getLoggerContext());
        Assert.assertEquals(requestURI, event.getRequestURI());
        Assert.assertEquals(requestURL, event.getRequestURL());
        Assert.assertEquals(remoteHost, event.getRemoteHost());
        Assert.assertEquals(remoteUser, event.getRemoteUser());
        Assert.assertEquals(protocol, event.getProtocol());
        Assert.assertEquals(method, event.getMethod());
        Assert.assertEquals(serverName, event.getServerName());
        Assert.assertEquals(remoteAddress, event.getRemoteAddress());
        Assert.assertEquals(requestHeaders, event.getRequestHeaders());
        Assert.assertEquals(responseHeaders, event.getResponseHeaders());
        Assert.assertEquals(requestParameters, event.getRequestParameters());
        Assert.assertEquals(localPort, event.getLocalPort());
        Assert.assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testAccessEventWithEmptyProperties() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(null);
        event.setLoggerContext(null);
        event.setRequestURI(null);
        event.setRequestURL(null);
        event.setRemoteHost(null);
        event.setRemoteUser(null);
        event.setProtocol(null);
        event.setMethod(null);
        event.setServerName(null);
        event.setRemoteAddress(null);
        event.setRequestHeaders(null);
        event.setResponseHeaders(null);
        event.setRequestParameters(null);
        event.setLocalPort(0);
        event.setStatusCode(0);

        Assert.assertNull(event.getTimeStamp());
        Assert.assertNull(event.getLoggerContext());
        Assert.assertNull(event.getRequestURI());
        Assert.assertNull(event.getRequestURL());
        Assert.assertNull(event.getRemoteHost());
        Assert.assertNull(event.getRemoteUser());
        Assert.assertNull(event.getProtocol());
        Assert.assertNull(event.getMethod());
        Assert.assertNull(event.getServerName());
        Assert.assertNull(event.getRemoteAddress());
        Assert.assertNull(event.getRequestHeaders());
        Assert.assertNull(event.getResponseHeaders());
        Assert.assertNotNull(event.getRequestParameters());
        Assert.assertEquals(0, event.getLocalPort());
        Assert.assertEquals(0, event.getStatusCode());
    }

    @Test
    public void testAccessEventWithInvalidProperties() {
        AccessEvent event = new AccessEvent();

        // Test null loggerContext
        event.setLoggerContext(null);
        try {
            event.setTimeStamp(1L);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test empty map
        event.setRequestHeaders(new HashMap<>());
        event.setResponseHeaders(new HashMap<>());
        event.setRequestParameters(new HashMap<>());

        // Test invalid status code
        event.setStatusCode(-1);
        try {
            event.setStatusCode(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test equals method
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLocalPort(8080);
        event2.setTimeStamp(1L);
        event2.setLocalPort(8081);

        Assert.assertFalse(event1.equals(event2));

        event1.setLocalPort(8080);
        Assert.assertTrue(event1.equals(event1));
        Assert.assertTrue(event1.equals(event1));

        // Test hashcode method
        AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(2L);
        event3.setLocalPort(8080);

        int hashcode1 = event1.hashCode();
        int hashcode2 = event2.hashCode();
        int hashcode3 = event3.hashCode();

        Assert.assertEquals(hashcode1, hashcode1);
        Assert.assertEquals(hashcode1, hashcode2);
        Assert.assertEquals(hashcode2, hashcode2);
        Assert.assertNotEquals(hashcode1, hashcode3);
    }
}