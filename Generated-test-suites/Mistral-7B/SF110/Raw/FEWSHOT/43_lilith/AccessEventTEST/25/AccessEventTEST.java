package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class AccessEventTest {

    private AccessEvent event;

    @Before
    public void setup() {
        event = new AccessEvent();
        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test");
        event.setRequestURL("http://test.com/test");
        event.setRemoteHost("127.0.0.1");
        event.setRemoteUser("user");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("test.com");
        event.setRemoteAddress("127.0.0.1");
        event.setRequestHeaders(new HashMap<>());
        event.setResponseHeaders(new HashMap<>());
        event.setRequestParameters(new HashMap<>());
        event.setLocalPort(8080);
        event.setStatusCode(200);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1L, event.getTimeStamp());
        assertEquals(event, event.setLoggerContext(new LoggerContext()));
        assertEquals(new LoggerContext(), event.getLoggerContext());
        assertEquals("/test", event.getRequestURI());
        assertEquals("http://test.com/test", event.getRequestURL());
        assertEquals("127.0.0.1", event.getRemoteHost());
        assertEquals("user", event.getRemoteUser());
        assertEquals("HTTP/1.1", event.getProtocol());
        assertEquals("GET", event.getMethod());
        assertEquals("test.com", event.getServerName());
        assertEquals("127.0.0.1", event.getRemoteAddress());
        assertSame(new HashMap<>(), event.getRequestHeaders());
        assertSame(new HashMap<>(), event.getResponseHeaders());
        assertSame(new HashMap<>(), event.getRequestParameters());
        assertEquals(8080, event.getLocalPort());
        assertEquals(200, event.getStatusCode());
    }

    @Test
    public void testEquals() {
        final AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("test.com");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        final AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://test.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("user");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("test.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertTrue(event1.equals(event2));
        assertTrue(event2.equals(event1));

        final AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(2L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("/test");
        event3.setRequestURL("http://test.com/test");
        event3.setRemoteHost("127.0.0.1");
        event3.setRemoteUser("user");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("test.com");
        event3.setRemoteAddress("127.0.0.1");
        event3.setRequestHeaders(new HashMap<>());
        event3.setResponseHeaders(new HashMap<>());
        event3.setRequestParameters(new HashMap<>());
        event3.setLocalPort(8080);
        event3.setStatusCode(200);

        assertFalse(event1.equals(event3));
        assertFalse(event3.equals(event1));

        final AccessEvent event4 = new AccessEvent();
        event4.setTimeStamp(1L);
        event4.setLoggerContext(new LoggerContext());
        event4.setRequestURI("/test");
        event4.setRequestURL("http://test.com/test");
        event4.setRemoteHost("127.0.0.1");
        event4.setRemoteUser("user2");
        event4.setProtocol("HTTP/1.1");
        event4.setMethod("GET");
        event4.setServerName("test.com");
        event4.setRemoteAddress("127.0.0.1");
        event4.setRequestHeaders(new HashMap<>());
        event4.setResponseHeaders(new HashMap<>());
        event4.setRequestParameters(new HashMap<>());
        event4.setLocalPort(8080);
        event4.setStatusCode(200);

        assertFalse(event1.equals(event4));
        assertFalse(event4.equals(event1));
    }

    @Test
    public void testHashCode() {
        final AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("test.com");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        final long hashCode1 = event1.hashCode();
        final AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://test.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("user");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("test.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertEquals(hashCode1, event2.hashCode());

        final AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(2L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("/test");
        event3.setRequestURL("http://test.com/test");
        event3.setRemoteHost("127.0.0.1");
        event3.setRemoteUser("user");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("test.com");
        event3.setRemoteAddress("127.0.0.1");
        event3.setRequestHeaders(new HashMap<>());
        event3.setResponseHeaders(new HashMap<>());
        event3.setRequestParameters(new HashMap<>());
        event3.setLocalPort(8080);
        event3.setStatusCode(200);

        assertNotEquals(hashCode1, event3.hashCode());

        final AccessEvent event4 = new AccessEvent();
        event4.setTimeStamp(1L);
        event4.setLoggerContext(new LoggerContext());
        event4.setRequestURI("/test");
        event4.setRequestURL("http://test.com/test");
        event4.setRemoteHost("127.0.0.1");
        event4.setRemoteUser("user2");
        event4.setProtocol("HTTP/1.1");
        event4.setMethod("GET");
        event4.setServerName("test.com");
        event4.setRemoteAddress("127.0.0.1");
        event4.setRequestHeaders(new HashMap<>());
        event4.setResponseHeaders(new HashMap<>());
        event4.setRequestParameters(new HashMap<>());
        event4.setLocalPort(8080);
        event4.setStatusCode(200);

        assertNotEquals(hashCode1, event4.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("AccessEvent[loggerContext=de.huxhorn.lilith.data.LoggerContext@43b46833, timeStamp=1]", event.toString());
    }
}