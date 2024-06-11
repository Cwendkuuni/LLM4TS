package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();

        event.setTimeStamp(1L);
        assertEquals(1L, event.getTimeStamp());

        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());

        event.setRequestURI("/test/uri");
        assertEquals("/test/uri", event.getRequestURI());

        event.setRequestURL("http://test.com/test/uri");
        assertEquals("http://test.com/test/uri", event.getRequestURL());

        event.setRemoteHost("127.0.0.1");
        assertEquals("127.0.0.1", event.getRemoteHost());

        event.setRemoteUser("testUser");
        assertEquals("testUser", event.getRemoteUser());

        event.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", event.getProtocol());

        event.setMethod("GET");
        assertEquals("GET", event.getMethod());

        event.setServerName("testServer.com");
        assertEquals("testServer.com", event.getServerName());

        event.setRemoteAddress("127.0.0.2");
        assertEquals("127.0.0.2", event.getRemoteAddress());

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Parameter1", new String[]{"Value1", "Value2"});
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

        assertNotEquals(event1, event2);

        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        assertEquals(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        assertEquals(event1, event2);

        event1.setRequestURI("/test/uri1");
        event2.setRequestURI("/test/uri1");
        assertEquals(event1, event2);

        event1.setRequestURL("http://test1.com/test/uri1");
        event2.setRequestURL("http://test2.com/test/uri1");
        assertNotEquals(event1, event2);

        event1.setRemoteHost("127.0.0.1");
        event2.setRemoteHost("127.0.0.1");
        assertEquals(event1, event2);

        event1.setRemoteUser("testUser1");
        event2.setRemoteUser("testUser1");
        assertEquals(event1, event2);

        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        assertEquals(event1, event2);

        event1.setMethod("GET");
        event2.setMethod("GET");
        assertEquals(event1, event2);

        event1.setServerName("testServer1.com");
        event2.setServerName("testServer1.com");
        assertEquals(event1, event2);

        event1.setRemoteAddress("127.0.0.2");
        event2.setRemoteAddress("127.0.0.2");
        assertEquals(event1, event2);

        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Header1", "Value1");
        event1.setRequestHeaders(requestHeaders1);

        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("Header1", "Value1");
        event2.setRequestHeaders(requestHeaders2);

        assertEquals(event1, event2);

        event1.setRequestHeaders(null);
        assertNotEquals(event1, event2);

        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("Header1", "Value1");
        event1.setResponseHeaders(responseHeaders1);

        Map<String, String> responseHeaders2 = new HashMap<>();
        responseHeaders2.put("Header1", "Value1");
        event2.setResponseHeaders(responseHeaders2);

        assertEquals(event1, event2);

        event1.setResponseHeaders(null);
        assertNotEquals(event1, event2);

        Map<String, String[]> requestParameters1 = new HashMap<>();
        requestParameters1.put("Parameter1", new String[]{"Value1", "Value2"});
        event1.setRequestParameters(requestParameters1);

        Map<String, String[]> requestParameters2 = new HashMap<>();
        requestParameters2.put("Parameter1", new String[]{"Value1", "Value2"});
        event2.setRequestParameters(requestParameters2);

        assertEquals(event1, event2);

        event1.setRequestParameters(null);
        assertNotEquals(event1, event2);

        event1.setLocalPort(8080);
        event2.setLocalPort(8080);
        assertEquals(event1, event2);

        event1.setStatusCode(200);
        event2.setStatusCode(200);
        assertEquals(event1, event2);

        event1.setStatusCode(201);
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

        event1.setRequestURI("/test/uri1");
        event2.setRequestURI("/test/uri1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestURL("http://test1.com/test/uri1");
        event2.setRequestURL("http://test2.com/test/uri1");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteHost("127.0.0.1");
        event2.setRemoteHost("127.0.0.1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteUser("testUser1");
        event2.setRemoteUser("testUser1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setMethod("GET");
        event2.setMethod("GET");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setServerName("testServer1.com");
        event2.setServerName("testServer1.com");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteAddress("127.0.0.2");
        event2.setRemoteAddress("127.0.0.2");
        assertEquals(event1.hashCode(), event2.hashCode());

        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Header1", "Value1");
        event1.setRequestHeaders(requestHeaders1);

        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("Header1", "Value1");
        event2.setRequestHeaders(requestHeaders2);

        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestHeaders(null);
        int hashcode3 = event1.hashCode();
        assertNotEquals(hashcode1, hashcode3);

        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("Header1", "Value1");
        event1.setResponseHeaders(responseHeaders1);

        Map<String, String> responseHeaders2 = new HashMap<>();
        responseHeaders2.put("Header1", "Value1");
        event2.setResponseHeaders(responseHeaders2);

        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setResponseHeaders(null);
        int hashcode4 = event1.hashCode();
        assertNotEquals(hashcode1, hashcode4);

        Map<String, String[]> requestParameters1 = new HashMap<>();
        requestParameters1.put("Parameter1", new String[]{"Value1", "Value2"});
        event1.setRequestParameters(requestParameters1);

        Map<String, String[]> requestParameters2 = new HashMap<>();
        requestParameters2.put("Parameter1", new String[]{"Value1", "Value2"});
        event2.setRequestParameters(requestParameters2);

        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestParameters(null);
        int hashcode5 = event1.hashCode();
        assertNotEquals(hashcode1, hashcode5);

        event1.setLocalPort(8080);
        event2.setLocalPort(8080);
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setStatusCode(200);
        event2.setStatusCode(200);
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setStatusCode(201);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();

        assertEquals("AccessEvent[]", event.toString());

        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test/uri");
        event.setRequestURL("http://test.com/test/uri");
        event.setRemoteHost("127.0.0.1");
        event.setRemoteUser("testUser");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("testServer.com");
        event.setRemoteAddress("127.0.0.2");
        event.setRequestHeaders(new HashMap<>());
        event.setResponseHeaders(new HashMap<>());
        event.setRequestParameters(new HashMap<>());
        event.setLocalPort(8080);
        event.setStatusCode(200);

        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("AccessEvent[loggerContext=");
        expectedResult.append(event.getLoggerContext()).append(", ");
        expectedResult.append("timeStamp=").append(event.getTimeStamp());
        expectedResult.append("]");

        assertEquals(expectedResult.toString(), event.toString());
    }
}