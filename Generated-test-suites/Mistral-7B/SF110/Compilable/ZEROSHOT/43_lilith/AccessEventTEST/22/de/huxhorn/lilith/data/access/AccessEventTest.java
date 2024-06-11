package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testAccessEventConstruction() {
        // Create an AccessEvent object with default values
        AccessEvent event = new AccessEvent();

        // Verify default values
        assertNull(event.getTimeStamp());
        assertNull(event.getLoggerContext());
        assertNull(event.getRequestURI());
        assertNull(event.getRequestURL());
        assertNull(event.getRemoteHost());
        assertNull(event.getRemoteUser());
        assertNull(event.getProtocol());
        assertNull(event.getMethod());
        assertNull(event.getServerName());
        assertNull(event.getRemoteAddress());
        assertNull(event.getRequestHeaders());
        assertNull(event.getResponseHeaders());
        assertNull(event.getRequestParameters());
        assertEquals(0, event.getLocalPort());
        assertEquals(0, event.getStatusCode());

        // Create an AccessEvent object with custom values
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testuser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testserver.com";
        String remoteAddress = "192.168.1.1";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("User-Agent", "Mozilla/5.0");
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        int localPort = 8080;
        int statusCode = 200;

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(timeStamp);
        event2.setLoggerContext(loggerContext);
        event2.setRequestURI(requestURI);
        event2.setRequestURL(requestURL);
        event2.setRemoteHost(remoteHost);
        event2.setRemoteUser(remoteUser);
        event2.setProtocol(protocol);
        event2.setMethod(method);
        event2.setServerName(serverName);
        event2.setRemoteAddress(remoteAddress);
        event2.setRequestHeaders(requestHeaders);
        event2.setResponseHeaders(responseHeaders);
        event2.setRequestParameters(requestParameters);
        event2.setLocalPort(localPort);
        event2.setStatusCode(statusCode);

        // Verify custom values
        assertEquals(timeStamp, event2.getTimeStamp());
        assertSame(loggerContext, event2.getLoggerContext());
        assertEquals(requestURI, event2.getRequestURI());
        assertEquals(requestURL, event2.getRequestURL());
        assertEquals(remoteHost, event2.getRemoteHost());
        assertEquals(remoteUser, event2.getRemoteUser());
        assertEquals(protocol, event2.getProtocol());
        assertEquals(method, event2.getMethod());
        assertEquals(serverName, event2.getServerName());
        assertEquals(remoteAddress, event2.getRemoteAddress());
        assertEquals(requestHeaders, event2.getRequestHeaders());
        assertEquals(responseHeaders, event2.getResponseHeaders());
        assertEquals(requestParameters, event2.getRequestParameters());
        assertEquals(localPort, event2.getLocalPort());
        assertEquals(statusCode, event2.getStatusCode());
    }

    @Test
    public void testAccessEventEquals() {
        // Create AccessEvent objects with identical values
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testuser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("testserver.com");
        event1.setRemoteAddress("192.168.1.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://test.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testuser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("testserver.com");
        event2.setRemoteAddress("192.168.1.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        // Verify that AccessEvent objects are equal
        assertTrue(event1.equals(event2));
        assertTrue(event2.equals(event1));

        // Create AccessEvent objects with different values
        event1.setLocalPort(8081);
        event1.setStatusCode(201);

        // Verify that AccessEvent objects are not equal
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));

        // Create AccessEvent objects with null values
        event2 = new AccessEvent();
        event2.setTimeStamp(null);
        event2.setLoggerContext(null);
        event2.setRequestURI(null);
        event2.setRequestURL(null);
        event2.setRemoteHost(null);
        event2.setRemoteUser(null);
        event2.setProtocol(null);
        event2.setMethod(null);
        event2.setServerName(null);
        event2.setRemoteAddress(null);
        event2.setRequestHeaders(null);
        event2.setResponseHeaders(null);
        event2.setRequestParameters(null);
        event2.setLocalPort(0);
        event2.setStatusCode(0);

        // Verify that AccessEvent objects with null values are equal
        assertTrue(event1.equals(event1));
        assertTrue(event2.equals(event2));

        // Verify that AccessEvent objects with null values are not equal to non-null objects
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));
    }

    @Test
    public void testAccessEventHashCode() {
        // Create AccessEvent objects with identical values
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testuser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("testserver.com");
        event1.setRemoteAddress("192.168.1.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://test.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testuser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("testserver.com");
        event2.setRemoteAddress("192.168.1.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        // Verify that AccessEvent objects have identical hash codes
        assertEquals(event1.hashCode(), event2.hashCode());

        // Create AccessEvent objects with different values
        event1.setLocalPort(8081);
        event1.setStatusCode(201);

        // Verify that AccessEvent objects have different hash codes
        assertNotSame(event1.hashCode(), event2.hashCode());

        // Create AccessEvent objects with null values
        event2 = new AccessEvent();
        event2.setTimeStamp(null);
        event2.setLoggerContext(null);
        event2.setRequestURI(null);
        event2.setRequestURL(null);
        event2.setRemoteHost(null);
        event2.setRemoteUser(null);
        event2.setProtocol(null);
        event2.setMethod(null);
        event2.setServerName(null);
        event2.setRemoteAddress(null);
        event2.setRequestHeaders(null);
        event2.setResponseHeaders(null);
        event2.setRequestParameters(null);
        event2.setLocalPort(0);
        event2.setStatusCode(0);

        // Verify that AccessEvent objects have different hash codes when one is null
        assertNotSame(event1.hashCode(), event2.hashCode());
    }
}