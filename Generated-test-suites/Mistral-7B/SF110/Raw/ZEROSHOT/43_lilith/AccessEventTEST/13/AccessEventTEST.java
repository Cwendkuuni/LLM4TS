package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testAccessEvent() {
        // Create an AccessEvent object with default values and verify its getters
        AccessEvent event = new AccessEvent();
        assertEquals(0L, event.getTimeStamp());
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

        // Test setters and getters for all fields
        event.setTimeStamp(1L);
        assertEquals(1L, event.getTimeStamp());

        LoggerContext loggerContext = mock(LoggerContext.class);
        event.setLoggerContext(loggerContext);
        assertSame(loggerContext, event.getLoggerContext());

        event.setRequestURI("/test");
        assertEquals("/test", event.getRequestURI());

        event.setRequestURL("http://test.com/test");
        assertEquals("http://test.com/test", event.getRequestURL());

        event.setRemoteHost("test.com");
        assertEquals("test.com", event.getRemoteHost());

        event.setRemoteUser("testuser");
        assertEquals("testuser", event.getRemoteUser());

        event.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", event.getProtocol());

        event.setMethod("GET");
        assertEquals("GET", event.getMethod());

        event.setServerName("testserver.com");
        assertEquals("testserver.com", event.getServerName());

        event.setRemoteAddress("192.168.0.1");
        assertEquals("192.168.0.1", event.getRemoteAddress());

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header2", "Value2");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());

        event.setLocalPort(8080);
        assertEquals(8080, event.getLocalPort());

        event.setStatusCode(200);
        assertEquals(200, event.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        // Test equal AccessEvent objects
        event1.setTimeStamp(1L);
        event1.setLocalPort(8080);
        event2.setTimeStamp(1L);
        event2.setLocalPort(8080);

        AccessEvent equalEvent = new AccessEvent();
        equalEvent.setTimeStamp(1L);
        equalEvent.setLocalPort(8080);

        assertTrue(event1.equals(event1));
        assertTrue(event1.equals(event2));
        assertTrue(event1.equals(equalEvent));

        // Test not equal AccessEvent objects with different fields
        AccessEvent notEqualEvent1 = new AccessEvent();
        notEqualEvent1.setTimeStamp(2L);
        notEqualEvent1.setLocalPort(8081);

        AccessEvent notEqualEvent2 = new AccessEvent();
        notEqualEvent2.setTimeStamp(1L);
        notEqualEvent2.setLocalPort(8081);

        assertFalse(event1.equals(notEqualEvent1));
        assertFalse(event1.equals(notEqualEvent2));
        assertFalse(notEqualEvent1.equals(notEqualEvent2));

        // Test not equal AccessEvent objects with null fields
        AccessEvent nullLoggerContextEvent1 = new AccessEvent();
        nullLoggerContextEvent1.setLoggerContext(null);

        AccessEvent nullLoggerContextEvent2 = new AccessEvent();
        nullLoggerContextEvent2.setLoggerContext(null);

        AccessEvent nonNullLoggerContextEvent = new AccessEvent();
        nonNullLoggerContextEvent.setLoggerContext(mock(LoggerContext.class));

        assertFalse(nullLoggerContextEvent1.equals(nullLoggerContextEvent2));
        assertFalse(nullLoggerContextEvent1.equals(nonNullLoggerContextEvent));
        assertFalse(nonNullLoggerContextEvent.equals(nullLoggerContextEvent1));
    }

    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        long timeStamp = 1L;
        int localPort = 8080;

        int hashCode = event.hashCode();

        event.setTimeStamp(timeStamp);
        event.setLocalPort(localPort);

        AccessEvent equalEvent = new AccessEvent();
        equalEvent.setTimeStamp(timeStamp);
        equalEvent.setLocalPort(localPort);

        assertEquals(hashCode, event.hashCode());
        assertEquals(hashCode, equalEvent.hashCode());

        AccessEvent notEqualEvent = new AccessEvent();
        notEqualEvent.setTimeStamp(timeStamp + 1);
        notEqualEvent.setLocalPort(localPort + 1);

        assertNotEquals(hashCode, notEqualEvent.hashCode());
    }
}