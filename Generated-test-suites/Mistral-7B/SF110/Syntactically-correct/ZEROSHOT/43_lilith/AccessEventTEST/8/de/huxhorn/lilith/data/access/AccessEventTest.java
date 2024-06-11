package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testAccessEvent() {
        AccessEvent accessEvent = new AccessEvent();

        // Testing the default values
        assertNull(accessEvent.getTimeStamp());
        assertNull(accessEvent.getLoggerContext());
        assertNull(accessEvent.getRequestURI());
        assertNull(accessEvent.getRequestURL());
        assertNull(accessEvent.getRemoteHost());
        assertNull(accessEvent.getRemoteUser());
        assertNull(accessEvent.getProtocol());
        assertNull(accessEvent.getMethod());
        assertNull(accessEvent.getServerName());
        assertNull(accessEvent.getRemoteAddress());
        assertNull(accessEvent.getRequestHeaders());
        assertNull(accessEvent.getResponseHeaders());
        assertNull(accessEvent.getRequestParameters());
        assertEquals(0, accessEvent.getLocalPort());
        assertEquals(0, accessEvent.getStatusCode());

        // Testing the setter and getter methods for all fields

        AccessEvent accessEventWithValues = new AccessEvent();
        accessEventWithValues.setTimeStamp(1L);
        accessEventWithValues.setLoggerContext(new LoggerContext());
        accessEventWithValues.setRequestURI("/test/uri");
        accessEventWithValues.setRequestURL("http://test.com/test/uri");
        accessEventWithValues.setRemoteHost("192.168.1.1");
        accessEventWithValues.setRemoteUser("testUser");
        accessEventWithValues.setProtocol("HTTP/1.1");
        accessEventWithValues.setMethod("GET");
        accessEventWithValues.setServerName("testServer.com");
        accessEventWithValues.setRemoteAddress("192.168.1.2");

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEventWithValues.setRequestHeaders(requestHeaders);

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEventWithValues.setResponseHeaders(responseHeaders);

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("id", new String[]{"1", "2"});
        accessEventWithValues.setRequestParameters(requestParameters);

        accessEventWithValues.setLocalPort(8080);
        accessEventWithValues.setStatusCode(200);

        assertEquals(1L, accessEventWithValues.getTimeStamp());
        assertEquals(new LoggerContext(), accessEventWithValues.getLoggerContext());
        assertEquals("/test/uri", accessEventWithValues.getRequestURI());
        assertEquals("http://test.com/test/uri", accessEventWithValues.getRequestURL());
        assertEquals("192.168.1.1", accessEventWithValues.getRemoteHost());
        assertEquals("testUser", accessEventWithValues.getRemoteUser());
        assertEquals("HTTP/1.1", accessEventWithValues.getProtocol());
        assertEquals("GET", accessEventWithValues.getMethod());
        assertEquals("testServer.com", accessEventWithValues.getServerName());
        assertEquals("192.168.1.2", accessEventWithValues.getRemoteAddress());
        assertEquals(requestHeaders, accessEventWithValues.getRequestHeaders());
        assertEquals(responseHeaders, accessEventWithValues.getResponseHeaders());
        assertEquals(requestParameters, accessEventWithValues.getRequestParameters());
        assertEquals(8080, accessEventWithValues.getLocalPort());
        assertEquals(200, accessEventWithValues.getStatusCode());

        // Testing the equals method
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEventWithValues;
        AccessEvent accessEvent3 = new AccessEvent();

        assertTrue(accessEvent2.equals(accessEventWithValues));
        assertFalse(accessEvent1.equals(accessEventWithValues));
        assertFalse(accessEvent1.equals(accessEvent3));
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEventWithValues = new AccessEvent();
        accessEventWithValues.setTimeStamp(1L);
        accessEventWithValues.setLoggerContext(new LoggerContext());
        accessEventWithValues.setRequestURI("/test/uri");
        accessEventWithValues.setRequestURL("http://test.com/test/uri");
        accessEventWithValues.setRemoteHost("192.168.1.1");
        accessEventWithValues.setRemoteUser("testUser");
        accessEventWithValues.setProtocol("HTTP/1.1");
        accessEventWithValues.setMethod("GET");
        accessEventWithValues.setServerName("testServer.com");
        accessEventWithValues.setRemoteAddress("192.168.1.2");

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEventWithValues.setRequestHeaders(requestHeaders);

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEventWithValues.setResponseHeaders(responseHeaders);

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("id", new String[]{"1", "2"});
        accessEventWithValues.setRequestParameters(requestParameters);

        accessEventWithValues.setLocalPort(8080);
        accessEventWithValues.setStatusCode(200);

        long hashCode = accessEventWithValues.hashCode();

        AccessEvent accessEventWithSameValues = new AccessEvent();
        accessEventWithSameValues.setTimeStamp(1L);
        accessEventWithSameValues.setLoggerContext(accessEventWithValues.getLoggerContext());
        accessEventWithSameValues.setRequestURI(accessEventWithValues.getRequestURI());
        accessEventWithSameValues.setRequestURL(accessEventWithValues.getRequestURL());
        accessEventWithSameValues.setRemoteHost(accessEventWithValues.getRemoteHost());
        accessEventWithSameValues.setRemoteUser(accessEventWithValues.getRemoteUser());
        accessEventWithSameValues.setProtocol(accessEventWithValues.getProtocol());
        accessEventWithSameValues.setMethod(accessEventWithValues.getMethod());
        accessEventWithSameValues.setServerName(accessEventWithValues.getServerName());
        accessEventWithSameValues.setRemoteAddress(accessEventWithValues.getRemoteAddress());
        accessEventWithSameValues.setRequestHeaders(accessEventWithValues.getRequestHeaders());
        accessEventWithSameValues.setResponseHeaders(accessEventWithValues.getResponseHeaders());
        accessEventWithSameValues.setRequestParameters(accessEventWithValues.getRequestParameters());
        accessEventWithSameValues.setLocalPort(accessEventWithValues.getLocalPort());
        accessEventWithSameValues.setStatusCode(accessEventWithValues.getStatusCode());

        long hashCodeSameValues = accessEventWithSameValues.hashCode();

        assertEquals(hashCode, hashCodeSameValues);

        AccessEvent accessEventWithDifferentValues = new AccessEvent();
        accessEventWithDifferentValues.setTimeStamp(2L);
        accessEventWithDifferentValues.setLoggerContext(new LoggerContext());
        accessEventWithDifferentValues.setRequestURI("/test/uri2");
        accessEventWithDifferentValues.setRequestURL("http://test2.com/test/uri2");
        accessEventWithDifferentValues.setRemoteHost("192.168.2.1");
        accessEventWithDifferentValues.setRemoteUser("testUser2");
        accessEventWithDifferentValues.setProtocol("HTTP/1.1");
        accessEventWithDifferentValues.setMethod("POST");
        accessEventWithDifferentValues.setServerName("testServer2.com");
        accessEventWithDifferentValues.setRemoteAddress("192.168.2.2");

        Map<String, String> requestHeadersDifferent = new HashMap<>();
        requestHeadersDifferent.put("Content-Type", "text/plain");
        accessEventWithDifferentValues.setRequestHeaders(requestHeadersDifferent);

        Map<String, String> responseHeadersDifferent = new HashMap<>();
        responseHeadersDifferent.put("Content-Type", "text/plain");
        accessEventWithDifferentValues.setResponseHeaders(responseHeadersDifferent);

        Map<String, String[]> requestParametersDifferent = new HashMap<>();
        requestParametersDifferent.put("id", new String[]{"3", "4"});
        accessEventWithDifferentValues.setRequestParameters(requestParametersDifferent);

        accessEventWithDifferentValues.setLocalPort(8081);
        accessEventWithDifferentValues.setStatusCode(201);

        long hashCodeDifferent = accessEventWithDifferentValues.hashCode();

        assertNotEquals(hashCode, hashCodeDifferent);
    }
}