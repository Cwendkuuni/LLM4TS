package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertNotEquals(event1, event2);

        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        assertEquals(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        assertEquals(event1, event2);

        event1.setRequestURI("/test/uri");
        event2.setRequestURI("/test/uri");
        assertEquals(event1, event2);

        event1.setRequestURL("http://test.com/test/uri");
        event2.setRequestURL("http://test.com/test/uri");
        assertEquals(event1, event2);

        event1.setRemoteHost("127.0.0.1");
        event2.setRemoteHost("127.0.0.1");
        assertEquals(event1, event2);

        event1.setRemoteUser("testUser");
        event2.setRemoteUser("testUser");
        assertEquals(event1, event2);

        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        assertEquals(event1, event2);

        event1.setMethod("GET");
        event2.setMethod("GET");
        assertEquals(event1, event2);

        event1.setServerName("testServerName");
        event2.setServerName("testServerName");
        assertEquals(event1, event2);

        event1.setRemoteAddress("127.0.0.1");
        event2.setRemoteAddress("127.0.0.1");
        assertEquals(event1, event2);

        event1.setRequestHeaders(new HashMap<>());
        event2.setRequestHeaders(new HashMap<>());
        assertEquals(event1, event2);

        event1.setResponseHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        assertEquals(event1, event2);

        event1.setRequestParameters(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        assertEquals(event1, event2);

        event1.setLocalPort(8080);
        event2.setLocalPort(8080);
        assertEquals(event1, event2);

        event1.setStatusCode(200);
        event2.setStatusCode(200);
        assertEquals(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setTimeStamp(1L);
        event2.setTimeStamp(2L);
        assertNotEquals(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setMethod("GET");
        event2.setMethod("POST");
        assertNotEquals(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setRemoteAddress("127.0.0.1");
        event2.setRemoteAddress("127.0.0.2");
        assertNotEquals(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event2.setRequestURI("/test2");
        assertNotEquals(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setRequestHeaders(new HashMap<>());
        event2.setRequestHeaders(new HashMap<>());
        event1.getRequestHeaders().put("key", "value");
        assertNotEquals(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setResponseHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event1.getResponseHeaders().put("key", "value");
        assertNotEquals(event1, event2);
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        int hashcode1 = event1.hashCode();
        int hashcode2 = event2.hashCode();
        assertNotEquals(hashcode1, hashcode2);

        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestURI("/test/uri");
        event2.setRequestURI("/test/uri");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestURL("http://test.com/test/uri");
        event2.setRequestURL("http://test.com/test/uri");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteHost("127.0.0.1");
        event2.setRemoteHost("127.0.0.1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteUser("testUser");
        event2.setRemoteUser("testUser");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setMethod("GET");
        event2.setMethod("GET");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setServerName("testServerName");
        event2.setServerName("testServerName");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteAddress("127.0.0.1");
        event2.setRemoteAddress("127.0.0.1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestHeaders(new HashMap<>());
        event2.setRequestHeaders(new HashMap<>());
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setResponseHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestParameters(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setLocalPort(8080);
        event2.setLocalPort(8080);
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setStatusCode(200);
        event2.setStatusCode(200);
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setTimeStamp(1L);
        event2.setTimeStamp(2L);
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setMethod("GET");
        event2.setMethod("POST");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setRemoteAddress("127.0.0.1");
        event2.setRemoteAddress("127.0.0.2");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event2.setRequestURI("/test2");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setRequestHeaders(new HashMap<>());
        event2.setRequestHeaders(new HashMap<>());
        event1.getRequestHeaders().put("key", "value");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setResponseHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event1.getResponseHeaders().put("key", "value");
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        String toString = event.toString();
        assertTrue(toString.contains("AccessEvent["));
        assertTrue(toString.contains("loggerContext="));
        assertTrue(toString.contains("timeStamp="));
    }
}