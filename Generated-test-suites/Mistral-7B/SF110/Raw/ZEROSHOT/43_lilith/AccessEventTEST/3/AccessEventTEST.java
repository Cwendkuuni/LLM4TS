package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters() {
        AccessEvent accessEvent = new AccessEvent();

        accessEvent.setTimeStamp(1L);
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setRequestURI("/test/path");
        accessEvent.setRequestURL("http://test.com/test/path");
        accessEvent.setRemoteHost("127.0.0.1");
        accessEvent.setRemoteUser("testUser");
        accessEvent.setProtocol("HTTP/1.1");
        accessEvent.setMethod("GET");
        accessEvent.setServerName("testServer.com");
        accessEvent.setRemoteAddress("127.0.0.2");
        accessEvent.setRequestHeaders(new HashMap<>());
        accessEvent.setResponseHeaders(new HashMap<>());
        accessEvent.setRequestParameters(new HashMap<>());
        accessEvent.setLocalPort(8080);
        accessEvent.setStatusCode(200);

        assertEquals(1L, accessEvent.getTimeStamp());
        assertEquals(new LoggerContext(), accessEvent.getLoggerContext());
        assertEquals("/test/path", accessEvent.getRequestURI());
        assertEquals("http://test.com/test/path", accessEvent.getRequestURL());
        assertEquals("127.0.0.1", accessEvent.getRemoteHost());
        assertEquals("testUser", accessEvent.getRemoteUser());
        assertEquals("HTTP/1.1", accessEvent.getProtocol());
        assertEquals("GET", accessEvent.getMethod());
        assertEquals("testServer.com", accessEvent.getServerName());
        assertEquals("127.0.0.2", accessEvent.getRemoteAddress());
        assertTrue(accessEvent.getRequestHeaders() instanceof Map);
        assertTrue(accessEvent.getResponseHeaders() instanceof Map);
        assertTrue(accessEvent.getRequestParameters() instanceof Map);
        assertEquals(8080, accessEvent.getLocalPort());
        assertEquals(200, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        AccessEvent accessEvent3 = new AccessEvent();

        accessEvent1.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/test/path");
        accessEvent1.setRequestURL("http://test.com/test/path");
        accessEvent1.setRemoteHost("127.0.0.1");
        accessEvent1.setRemoteUser("testUser");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent1.setServerName("testServer.com");
        accessEvent1.setRemoteAddress("127.0.0.2");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        accessEvent2.setTimeStamp(1L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("/test/path");
        accessEvent2.setRequestURL("http://test.com/test/path");
        accessEvent2.setRemoteHost("127.0.0.1");
        accessEvent2.setRemoteUser("testUser");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent2.setMethod("GET");
        accessEvent2.setServerName("testServer.com");
        accessEvent2.setRemoteAddress("127.0.0.2");
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent2.setLocalPort(8080);
        accessEvent2.setStatusCode(200);

        accessEvent3.setTimeStamp(2L);
        accessEvent3.setLoggerContext(accessEvent1.getLoggerContext());
        accessEvent3.setRequestURI("/test/path");
        accessEvent3.setRequestURL("http://test.com/test/path");
        accessEvent3.setRemoteHost("127.0.0.1");
        accessEvent3.setRemoteUser("testUser");
        accessEvent3.setProtocol("HTTP/1.1");
        accessEvent3.setMethod("GET");
        accessEvent3.setServerName("testServer.com");
        accessEvent3.setRemoteAddress("127.0.0.2");
        accessEvent3.setRequestHeaders(new HashMap<>());
        accessEvent3.setResponseHeaders(new HashMap<>());
        accessEvent3.setRequestParameters(new HashMap<>());
        accessEvent3.setLocalPort(8080);
        accessEvent3.setStatusCode(200);

        assertTrue(accessEvent1.equals(accessEvent2));
        assertFalse(accessEvent1.equals(accessEvent3));
    }
}