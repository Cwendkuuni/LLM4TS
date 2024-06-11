package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testEquals_SameObject() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = event1;
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testEquals_DifferentObjects_SameValues() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentValues() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event2.setTimeStamp(2L);
        event2.setLoggerContext(new LoggerContext());
        assertFalse(event1.equals(event2));
    }

    @Test
    public void testEquals_DifferentClass() {
        AccessEvent event = new AccessEvent();
        Object other = new Object();
        assertFalse(event.equals(other));
    }

    @Test
    public void testHashCode_SameObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = event1;
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_DifferentObjects_SameValues() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentValues() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event2.setTimeStamp(2L);
        event2.setLoggerContext(new LoggerContext());
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        String toString = event.toString();
        assertTrue(toString.contains("AccessEvent["));
        assertTrue(toString.contains("loggerContext="));
        assertTrue(toString.contains("timeStamp="));
    }

    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com";
        String remoteHost = "127.0.0.1";
        String remoteUser = "user";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "test.com";
        String remoteAddress = "127.0.0.1";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 80;
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

        assertEquals(timeStamp, event.getTimeStamp());
        assertEquals(loggerContext, event.getLoggerContext());
        assertEquals(requestURI, event.getRequestURI());
        assertEquals(requestURL, event.getRequestURL());
        assertEquals(remoteHost, event.getRemoteHost());
        assertEquals(remoteUser, event.getRemoteUser());
        assertEquals(protocol, event.getProtocol());
        assertEquals(method, event.getMethod());
        assertEquals(serverName, event.getServerName());
        assertEquals(remoteAddress, event.getRemoteAddress());
        assertEquals(requestHeaders, event.getRequestHeaders());
        assertEquals(responseHeaders, event.getResponseHeaders());
        assertEquals(requestParameters, event.getRequestParameters());
        assertEquals(localPort, event.getLocalPort());
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testGettersAndSetters_EmptyMaps() {
        AccessEvent event = new AccessEvent();

        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();

        event.setRequestHeaders(requestHeaders);
        event.setResponseHeaders(responseHeaders);
        event.setRequestParameters(requestParameters);

        assertNull(event.getRequestHeaders());
        assertNull(event.getResponseHeaders());
        assertNull(event.getRequestParameters());

        event.setRequestHeaders(Collections.emptyMap());
        event.setResponseHeaders(Collections.emptyMap());
        event.setRequestParameters(Collections.emptyMap());

        assertEquals(Collections.emptyMap(), event.getRequestHeaders());
        assertEquals(Collections.emptyMap(), event.getResponseHeaders());
        assertEquals(Collections.emptyMap(), event.getRequestParameters());
    }
}