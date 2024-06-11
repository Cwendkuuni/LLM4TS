package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testAccessEvent() {
        // Test empty AccessEvent
        AccessEvent emptyEvent = new AccessEvent();
        assertNull(emptyEvent.getTimeStamp());
        assertNull(emptyEvent.getLoggerContext());
        assertNull(emptyEvent.getRequestURI());
        assertNull(emptyEvent.getRequestURL());
        assertNull(emptyEvent.getRemoteHost());
        assertNull(emptyEvent.getRemoteUser());
        assertNull(emptyEvent.getProtocol());
        assertNull(emptyEvent.getMethod());
        assertNull(emptyEvent.getServerName());
        assertNull(emptyEvent.getRemoteAddress());
        assertNull(emptyEvent.getRequestHeaders());
        assertNull(emptyEvent.getResponseHeaders());
        assertNull(emptyEvent.getRequestParameters());
        assertEquals(0, emptyEvent.getLocalPort());
        assertEquals(0, emptyEvent.getStatusCode());

        // Test AccessEvent with all fields set
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("header1", "value1");
        requestHeaders.put("header2", "value2");

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("header3", "value3");
        responseHeaders.put("header4", "value4");

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1_1", "value1_2"});
        requestParameters.put("param2", new String[]{"value2_1", "value2_2"});

        AccessEvent event = new AccessEvent();
        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test/uri");
        event.setRequestURL("http://test.com/test/uri?param1=value1&param2=value2");
        event.setRemoteHost("127.0.0.1");
        event.setRemoteUser("testUser");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("testServer.com");
        event.setRemoteAddress("127.0.0.1");
        event.setRequestHeaders(requestHeaders);
        event.setResponseHeaders(responseHeaders);
        event.setRequestParameters(requestParameters);
        event.setLocalPort(8080);
        event.setStatusCode(200);

        assertEquals(1L, event.getTimeStamp());
        assertEquals(new LoggerContext(), event.getLoggerContext());
        assertEquals("/test/uri", event.getRequestURI());
        assertEquals("http://test.com/test/uri?param1=value1&param2=value2", event.getRequestURL());
        assertEquals("127.0.0.1", event.getRemoteHost());
        assertEquals("testUser", event.getRemoteUser());
        assertEquals("HTTP/1.1", event.getProtocol());
        assertEquals("GET", event.getMethod());
        assertEquals("testServer.com", event.getServerName());
        assertEquals("127.0.0.1", event.getRemoteAddress());
        assertEquals(requestHeaders, event.getRequestHeaders());
        assertEquals(responseHeaders, event.getResponseHeaders());
        assertEquals(requestParameters, event.getRequestParameters());
        assertEquals(8080, event.getLocalPort());
        assertEquals(200, event.getStatusCode());

        // Test equals and hashCode
        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test/uri");
        event2.setRequestURL("http://test.com/test/uri?param1=value1&param2=value2");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testUser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("testServer.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(requestHeaders);
        event2.setResponseHeaders(responseHeaders);
        event2.setRequestParameters(requestParameters);
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(1L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("/test/uri");
        event3.setRequestURL("http://test.com/test/uri?param1=value1&param2=value2");
        event3.setRemoteHost("127.0.0.1");
        event3.setRemoteUser("testUser");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("testServer.com");
        event3.setRemoteAddress("127.0.0.1");
        event3.setRequestHeaders(requestHeaders);
        event3.setResponseHeaders(responseHeaders);
        event3.setRequestParameters(requestParameters);
        event3.setLocalPort(8080);
        event3.setStatusCode(200);

        assertTrue(event.equals(event2));
        assertTrue(event2.equals(event3));
        assertTrue(event.hashCode() == event2.hashCode());
        assertTrue(event.hashCode() == event3.hashCode());

        // Test not equals
        AccessEvent event4 = new AccessEvent();
        event4.setTimeStamp(1L);
        event4.setLoggerContext(new LoggerContext());
        event4.setRequestURI("/test/uri");
        event4.setRequestURL("http://test.com/test/uri?param1=value1&param2=value2");
        event4.setRemoteHost("127.0.0.1");
        event4.setRemoteUser("testUser");
        event4.setProtocol("HTTP/1.1");
        event4.setMethod("GET");
        event4.setServerName("testServer.com");
        event4.setRemoteAddress("127.0.0.2");
        event4.setRequestHeaders(requestHeaders);
        event4.setResponseHeaders(responseHeaders);
        event4.setRequestParameters(requestParameters);
        event4.setLocalPort(8081);
        event4.setStatusCode(201);

        assertFalse(event.equals(event4));
        assertFalse(event4.equals(event));
        assertFalse(event.hashCode() == event4.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test/uri");
        event.setRequestURL("http://test.com/test/uri?param1=value1&param2=value2");
        event.setRemoteHost("127.0.0.1");
        event.setRemoteUser("testUser");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("testServer.com");
        event.setRemoteAddress("127.0.0.1");
        event.setRequestHeaders(new HashMap<>());
        event.setResponseHeaders(new HashMap<>());
        event.setRequestParameters(new HashMap<>());
        event.setLocalPort(8080);
        event.setStatusCode(200);

        String expected = "AccessEvent[loggerContext=LoggerContext{}, timeStamp=1]";
        String actual = event.toString();
        assertEquals(expected, actual);
    }
}