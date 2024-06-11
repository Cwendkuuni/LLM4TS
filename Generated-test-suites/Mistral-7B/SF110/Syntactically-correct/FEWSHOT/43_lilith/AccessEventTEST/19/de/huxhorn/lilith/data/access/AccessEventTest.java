package org.apache.commons.cli; // make sure to replace the package name with the correct one for the AccessEvent class

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class AccessEventTest {

    @Test
    public void testAccessEventEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertFalse(event1.equals(event2));

        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);

        // test equals with same values
        assertTrue(event1.equals(event2));

        // test equals with different values
        event2.setTimeStamp(2L);
        assertFalse(event1.equals(event2));

        // test equals with null object
        AccessEvent nullEvent = null;
        assertFalse(event1.equals(nullEvent));

        // test equals with different classes
        Object obj = new Object();
        assertFalse(event1.equals(obj));

        // test equals with same logger context
        LoggerContext loggerContext = new LoggerContext();
        event1.setLoggerContext(loggerContext);
        event2.setLoggerContext(loggerContext);
        assertTrue(event1.equals(event2));

        // test equals with different logger context
        LoggerContext differentLoggerContext = new LoggerContext();
        event1.setLoggerContext(loggerContext);
        event2.setLoggerContext(differentLoggerContext);
        assertFalse(event1.equals(event2));

        // test equals with same method
        event1.setMethod("GET");
        event2.setMethod("GET");
        assertTrue(event1.equals(event2));

        // test equals with different method
        event2.setMethod("POST");
        assertFalse(event1.equals(event2));

        // test equals with same protocol
        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        assertTrue(event1.equals(event2));

        // test equals with different protocol
        event2.setProtocol("HTTPS/1.1");
        assertFalse(event1.equals(event2));

        // test equals with same remote address
        event1.setRemoteAddress("192.168.0.1");
        event2.setRemoteAddress("192.168.0.1");
        assertTrue(event1.equals(event2));

        // test equals with different remote address
        event2.setRemoteAddress("192.168.0.2");
        assertFalse(event1.equals(event2));

        // test equals with same remote host
        event1.setRemoteHost("localhost");
        event2.setRemoteHost("localhost");
        assertTrue(event1.equals(event2));

        // test equals with different remote host
        event2.setRemoteHost("remotehost");
        assertFalse(event1.equals(event2));

        // test equals with same remote user
        event1.setRemoteUser("user");
        event2.setRemoteUser("user");
        assertTrue(event1.equals(event2));

        // test equals with different remote user
        event2.setRemoteUser("anotherUser");
        assertFalse(event1.equals(event2));

        // test equals with same request headers
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("header1", "value1");
        event1.setRequestHeaders(requestHeaders1);
        Map<String, String> requestHeaders2 = new HashMap<>(requestHeaders1);
        event2.setRequestHeaders(requestHeaders2);
        assertTrue(event1.equals(event2));

        // test equals with different request headers
        requestHeaders2.put("header1", "value2");
        assertFalse(event1.equals(event2));

        // test equals with same request URI
        event1.setRequestURI("/test");
        event2.setRequestURI("/test");
        assertTrue(event1.equals(event2));

        // test equals with different request URI
        event2.setRequestURI("/other");
        assertFalse(event1.equals(event2));

        // test equals with same request URL
        event1.setRequestURL("http://test.com/test");
        event2.setRequestURL("http://test.com/test");
        assertTrue(event1.equals(event2));

        // test equals with different request URL
        event2.setRequestURL("http://other.com/test");
        assertFalse(event1.equals(event2));

        // test equals with same response headers
        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("header1", "value1");
        event1.setResponseHeaders(responseHeaders1);
        Map<String, String> responseHeaders2 = new HashMap<>(responseHeaders1);
        event2.setResponseHeaders(responseHeaders2);
        assertTrue(event1.equals(event2));

        // test equals with different response headers
        responseHeaders2.put("header1", "value2");
        assertFalse(event1.equals(event2));

        // test equals with same server name
        event1.setServerName("test.com");
        event2.setServerName("test.com");
        assertTrue(event1.equals(event2));

        // test equals with different server name
        event2.setServerName("other.com");
        assertFalse(event1.equals(event2));

        // test equals with same status code
        event1.setStatusCode(200);
        event2.setStatusCode(200);
        assertTrue(event1.equals(event2));

        // test equals with different status code
        event2.setStatusCode(404);
        assertFalse(event1.equals(event2));

        // test equals with same local port
        event1.setLocalPort(8080);
        event2.setLocalPort(8080);
        assertTrue(event1.equals(event2));

        // test equals with different local port
        event2.setLocalPort(80);
        assertFalse(event1.equals(event2));

        // test equals with same time stamp
        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        assertTrue(event1.equals(event2));

        // test equals with different time stamp
        event2.setTimeStamp(2L);
        assertFalse(event1.equals(event2));
    }

    @Test
    public void testAccessEventHashCode() {
        AccessEvent event = new AccessEvent();
        int hashCode = event.hashCode();

        event.setTimeStamp(1L);
        hashCode = 31 * hashCode + ((event.getTimeStamp() != null) ? event.getTimeStamp().hashCode() : 0);

        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        hashCode = 31 * hashCode + ((loggerContext != null) ? loggerContext.hashCode() : 0);

        event.setRequestURI("/test");
        hashCode = 31 * hashCode + ((event.getRequestURI() != null) ? event.getRequestURI().hashCode() : 0);

        event.setRequestURL("http://test.com/test");
        hashCode = 31 * hashCode + ((event.getRequestURL() != null) ? event.getRequestURL().hashCode() : 0);

        event.setRemoteHost("localhost");
        hashCode = 31 * hashCode + ((event.getRemoteHost() != null) ? event.getRemoteHost().hashCode() : 0);

        event.setRemoteUser("user");
        hashCode = 31 * hashCode + ((event.getRemoteUser() != null) ? event.getRemoteUser().hashCode() : 0);

        event.setProtocol("HTTP/1.1");
        hashCode = 31 * hashCode + ((event.getProtocol() != null) ? event.getProtocol().hashCode() : 0);

        event.setMethod("GET");
        hashCode = 31 * hashCode + ((event.getMethod() != null) ? event.getMethod().hashCode() : 0);

        event.setServerName("test.com");
        hashCode = 31 * hashCode + ((event.getServerName() != null) ? event.getServerName().hashCode() : 0);

        event.setRemoteAddress("192.168.0.1");
        hashCode = 31 * hashCode + ((event.getRemoteAddress() != null) ? event.getRemoteAddress().hashCode() : 0);

        event.setLocalPort(8080);
        hashCode = 31 * hashCode + event.getLocalPort();

        event.setStatusCode(200);
        hashCode = 31 * hashCode + event.getStatusCode();

        assertEquals(hashCode, event.hashCode());
    }
}