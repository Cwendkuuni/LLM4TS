package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testAccessEvent_AllFields() {
        AccessEvent accessEvent = new AccessEvent();

        // Test setters and getters for all fields
        accessEvent.setTimeStamp(1L);
        assertEquals(1L, accessEvent.getTimeStamp());

        accessEvent.setLoggerContext(new LoggerContext());
        assertEquals(accessEvent.getLoggerContext(), accessEvent.getLoggerContext());

        accessEvent.setRequestURI("/test/uri");
        assertEquals("/test/uri", accessEvent.getRequestURI());

        accessEvent.setRequestURL("http://test.com/test/url");
        assertEquals("http://test.com/test/url", accessEvent.getRequestURL());

        accessEvent.setRemoteHost("127.0.0.1");
        assertEquals("127.0.0.1", accessEvent.getRemoteHost());

        accessEvent.setRemoteUser("testUser");
        assertEquals("testUser", accessEvent.getRemoteUser());

        accessEvent.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", accessEvent.getProtocol());

        accessEvent.setMethod("GET");
        assertEquals("GET", accessEvent.getMethod());

        accessEvent.setServerName("localhost");
        assertEquals("localhost", accessEvent.getServerName());

        accessEvent.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", accessEvent.getRemoteAddress());

        Map<String, String> requestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());

        Map<String, String[]> requestParameters = new HashMap<>();
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());

        accessEvent.setLocalPort(8080);
        assertEquals(8080, accessEvent.getLocalPort());

        accessEvent.setStatusCode(200);
        assertEquals(200, accessEvent.getStatusCode());
    }

    @Test
    public void testAccessEvent_Equals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        // Test equals with same object
        assertEquals(accessEvent1, accessEvent1);

        // Test equals with null
        assertFalse(accessEvent1.equals(null));

        // Test equals with different class
        assertFalse(accessEvent1.equals(new Object()));

        // Test equals with same fields, but different instances
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(1L);
        assertEquals(accessEvent1, accessEvent2);

        // Test equals with different fields
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(2L);
        assertFalse(accessEvent1.equals(accessEvent2));

        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        assertFalse(accessEvent1.equals(accessEvent2));

        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/test/uri1");
        accessEvent2.setRequestURI("/test/uri2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testAccessEvent_HashCode() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        // Test hashCode with same fields, but different instances
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(1L);
        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());

        // Test hashCode with different fields
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(2L);
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());

        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testAccessEvent_ToString() {
        AccessEvent accessEvent = new AccessEvent();
        assertEquals("AccessEvent[loggerContext=null, timeStamp=null]", accessEvent.toString());

        accessEvent.setTimeStamp(1L);
        accessEvent.setLoggerContext(new LoggerContext());
        assertEquals("AccessEvent[loggerContext=LoggerContext@2352b56, timeStamp=1]", accessEvent.toString());
    }
}