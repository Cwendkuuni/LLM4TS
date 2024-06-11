package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertNotEquals(event1, event2);

        AccessEvent event3 = new AccessEvent();
        event3.setLoggerContext(new LoggerContext());
        event3.setTimeStamp(1L);
        event3.setRequestURI("/test");
        event3.setRequestURL("http://test.com/test");
        event3.setRemoteHost("127.0.0.1");
        event3.setRemoteUser("test");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("test.com");
        event3.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("header1", "value1");
        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("header1", "value2");
        Map<String, String[]> requestParameters1 = new HashMap<>();
        requestParameters1.put("param1", new String[]{"value1"});
        Map<String, String[]> requestParameters2 = new HashMap<>();
        requestParameters2.put("param2", new String[]{"value2"});
        event3.setRequestHeaders(requestHeaders1);
        event3.setRequestParameters(requestParameters1);
        event3.setLocalPort(80);
        event3.setStatusCode(200);

        AccessEvent event4 = new AccessEvent();
        event4.setLoggerContext(new LoggerContext());
        event4.setTimeStamp(1L);
        event4.setRequestURI("/test");
        event4.setRequestURL("http://test.com/test");
        event4.setRemoteHost("127.0.0.1");
        event4.setRemoteUser("test");
        event4.setProtocol("HTTP/1.1");
        event4.setMethod("GET");
        event4.setServerName("test.com");
        event4.setRemoteAddress("127.0.0.1");
        event4.setRequestHeaders(requestHeaders1);
        event4.setRequestParameters(requestParameters1);
        event4.setLocalPort(80);
        event4.setStatusCode(200);

        AccessEvent event5 = new AccessEvent();
        event5.setLoggerContext(new LoggerContext());
        event5.setTimeStamp(1L);
        event5.setRequestURI("/test");
        event5.setRequestURL("http://test.com/test");
        event5.setRemoteHost("127.0.0.1");
        event5.setRemoteUser("test");
        event5.setProtocol("HTTP/1.1");
        event5.setMethod("GET");
        event5.setServerName("test.com");
        event5.setRemoteAddress("127.0.0.2");
        event5.setRequestHeaders(requestHeaders1);
        event5.setRequestParameters(requestParameters1);
        event5.setLocalPort(80);
        event5.setStatusCode(200);

        AccessEvent event6 = new AccessEvent();
        event6.setLoggerContext(new LoggerContext());
        event6.setTimeStamp(1L);
        event6.setRequestURI("/test");
        event6.setRequestURL("http://test.com/test");
        event6.setRemoteHost("127.0.0.1");
        event6.setRemoteUser("test");
        event6.setProtocol("HTTP/1.1");
        event6.setMethod("GET");
        event6.setServerName("test.com");
        event6.setRemoteAddress("127.0.0.1");
        event6.setRequestHeaders(Collections.emptyMap());
        event6.setRequestParameters(Collections.emptyMap());
        event6.setLocalPort(80);
        event6.setStatusCode(200);

        AccessEvent event7 = new AccessEvent();
        event7.setLoggerContext(new LoggerContext());
        event7.setTimeStamp(2L);
        event7.setRequestURI("/test");
        event7.setRequestURL("http://test.com/test");
        event7.setRemoteHost("127.0.0.1");
        event7.setRemoteUser("test");
        event7.setProtocol("HTTP/1.1");
        event7.setMethod("GET");
        event7.setServerName("test.com");
        event7.setRemoteAddress("127.0.0.1");
        event7.setRequestHeaders(requestHeaders1);
        event7.setRequestParameters(requestParameters1);
        event7.setLocalPort(80);
        event7.setStatusCode(200);

        assertEquals(event3, event4);
        assertEquals(event3, event6);
        assertNotEquals(event3, event5);
        assertNotEquals(event3, event7);

        event5.setRemoteAddress("127.0.0.1");
        assertEquals(event3, event5);

        event4.setRemoteAddress("127.0.0.2");
        assertNotEquals(event3, event4);

        event5.setRemoteAddress("127.0.0.1");
        assertEquals(event3, event5);
    }

    @Test
    public void testHashcode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        int hashcode1 = event1.hashCode();
        int hashcode2 = event2.hashCode();

        AccessEvent event3 = new AccessEvent();
        event3.setLoggerContext(new LoggerContext());
        event3.setTimeStamp(1L);
        event3.setRequestURI("/test");
        event3.setRequestURL("http://test.com/test");
        event3.setRemoteHost("127.0.0.1");
        event3.setRemoteUser("test");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("test.com");
        event3.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("header1", "value1");
        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("header1", "value2");
        Map<String, String[]> requestParameters1 = new HashMap<>();
        requestParameters1.put("param1", new String[]{"value1"});
        Map<String, String[]> requestParameters2 = new HashMap<>();
        requestParameters2.put("param2", new String[]{"value2"});
        event3.setRequestHeaders(requestHeaders1);
        event3.setRequestParameters(requestParameters1);
        event3.setLocalPort(80);
        event3.setStatusCode(200);

        int hashcode3 = event3.hashCode();

        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);

        AccessEvent event4 = new AccessEvent();
        event4.setLoggerContext(new LoggerContext());
        event4.setTimeStamp(1L);
        event4.setRequestURI("/test");
        event4.setRequestURL("http://test.com/test");
        event4.setRemoteHost("127.0.0.1");
        event4.setRemoteUser("test");
        event4.setProtocol("HTTP/1.1");
        event4.setMethod("GET");
        event4.setServerName("test.com");
        event4.setRemoteAddress("127.0.0.1");
        event4.setRequestHeaders(requestHeaders1);
        event4.setRequestParameters(requestParameters1);
        event4.setLocalPort(80);
        event4.setStatusCode(200);

        int hashcode4 = event4.hashCode();

        assertEquals(hashcode3, hashcode4);
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

        event.setLoggerContext(new LoggerContext());
        LoggerContext loggerContext = event.getLoggerContext();
        assertSame(loggerContext, event.getLoggerContext());

        event.setTimeStamp(1L);
        Long timeStamp = event.getTimeStamp();
        assertEquals(timeStamp, event.getTimeStamp());

        event.setRequestURI("/test");
        String requestURI = event.getRequestURI();
        assertEquals(requestURI, event.getRequestURI());

        event.setRequestURL("http://test.com/test");
        String requestURL = event.getRequestURL();
        assertEquals(requestURL, event.getRequestURL());

        event.setRemoteHost("127.0.0.1");
        String remoteHost = event.getRemoteHost();
        assertEquals(remoteHost, event.getRemoteHost());

        event.setRemoteUser("test");
        String remoteUser = event.getRemoteUser();
        assertEquals(remoteUser, event.getRemoteUser());

        event.setProtocol("HTTP/1.1");
        String protocol = event.getProtocol();
        assertEquals(protocol, event.getProtocol());

        event.setMethod("GET");
        String method = event.getMethod();
        assertEquals(method, event.getMethod());

        event.setServerName("test.com");
        String serverName = event.getServerName();
        assertEquals(serverName, event.getServerName());

        event.setRemoteAddress("127.0.0.1");
        String remoteAddress = event.getRemoteAddress();
        assertEquals(remoteAddress, event.getRemoteAddress());

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("header1", "value1");
        event.setRequestHeaders(requestHeaders);
        Map<String, String> requestHeaders2 = event.getRequestHeaders();
        assertEquals(requestHeaders, requestHeaders2);

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1"});
        event.setRequestParameters(requestParameters);
        Map<String, String[]> requestParameters2 = event.getRequestParameters();
        assertEquals(requestParameters, requestParameters2);

        event.setLocalPort(80);
        int localPort = event.getLocalPort();
        assertEquals(localPort, event.getLocalPort());

        event.setStatusCode(200);
        int statusCode = event.getStatusCode();
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testEmptyMaps() {
        AccessEvent event = new AccessEvent();

        Map<String, String> requestHeaders = Collections.emptyMap();
        event.setRequestHeaders(requestHeaders);
        Map<String, String> requestHeaders2 = event.getRequestHeaders();
        assertTrue(requestHeaders.isEmpty());
        assertTrue(requestHeaders2.isEmpty());

        Map<String, String[]> requestParameters = Collections.emptyMap();
        event.setRequestParameters(requestParameters);
        Map<String, String[]> requestParameters2 = event.getRequestParameters();
        assertTrue(requestParameters.isEmpty());
        assertTrue(requestParameters2.isEmpty());
    }

    @Test
    public void testNullValues() {
        AccessEvent event = new AccessEvent();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("header1", null);
        event.setRequestHeaders(requestHeaders);
        Map<String, String> requestHeaders2 = event.getRequestHeaders();
        assertTrue(requestHeaders.containsValue(null));
        assertTrue(requestHeaders2.containsValue(null));

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", null});
        event.setRequestParameters(requestParameters);
        Map<String, String[]> requestParameters2 = event.getRequestParameters();
        assertArrayEquals(new String[]{"value1", null}, requestParameters.get("param1"));
        assertArrayEquals(new String[]{"value1", null}, requestParameters2.get("param1"));
    }

    @Test
    public void testSettersWithNullValues() {
        AccessEvent event = new AccessEvent();

        event.setLoggerContext(null);
        assertNull(event.getLoggerContext());

        event.setTimeStamp(null);
        assertNull(event.getTimeStamp());

        event.setRequestURI(null);
        assertNull(event.getRequestURI());

        event.setRequestURL(null);
        assertNull(event.getRequestURL());

        event.setRemoteHost(null);
        assertNull(event.getRemoteHost());

        event.setRemoteUser(null);
        assertNull(event.getRemoteUser());

        event.setProtocol(null);
        assertNull(event.getProtocol());

        event.setMethod(null);
        assertNull(event.getMethod());

        event.setServerName(null);
        assertNull(event.getServerName());

        event.setRemoteAddress(null);
        assertNull(event.getRemoteAddress());

        event.setRequestHeaders(null);
        assertNull(event.getRequestHeaders());

        event.setRequestParameters(null);
        assertNull(event.getRequestParameters());

        event.setLocalPort(-1);
        assertThrows(IllegalArgumentException.class, () -> event.setLocalPort(-2));

        event.setStatusCode(-1);
        assertThrows(IllegalArgumentException.class, () -> event.setStatusCode(-2));
    }
}