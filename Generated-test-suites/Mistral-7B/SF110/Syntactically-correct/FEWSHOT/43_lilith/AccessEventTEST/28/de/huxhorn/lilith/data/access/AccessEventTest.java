package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters_timeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 1234567890123L;

        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGettersAndSetters_loggerContext() {
        LoggerContext loggerContext = new LoggerContext();

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_requestURI() {
        String requestURI = "/test/uri";

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        String requestURL = "http://example.com/test/uri";

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        String remoteHost = "192.168.1.1";

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        String remoteUser = "testuser";

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_protocol() {
        String protocol = "HTTP/1.1";

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGettersAndSetters_method() {
        String method = "GET";

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGettersAndSetters_serverName() {
        String serverName = "example.com";

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        String remoteAddress = "192.168.1.2";

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_requestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Param1", new String[]{"Value1", "Value2"});

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_localPort() {
        int localPort = 8080;

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        int statusCode = 200;

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals_sameObject() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;

        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentObjects_sameValues() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        accessEvent1.setTimeStamp(1234567890123L);
        accessEvent2.setTimeStamp(1234567890123L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/test/uri");
        accessEvent2.setRequestURI("/test/uri");
        accessEvent1.setRequestURL("http://example.com/test/uri");
        accessEvent2.setRequestURL("http://example.com/test/uri");
        accessEvent1.setRemoteHost("192.168.1.1");
        accessEvent2.setRemoteHost("192.168.1.1");
        accessEvent1.setRemoteUser("testuser");
        accessEvent2.setRemoteUser("testuser");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent2.setMethod("GET");
        accessEvent1.setServerName("example.com");
        accessEvent2.setServerName("example.com");
        accessEvent1.setRemoteAddress("192.168.1.2");
        accessEvent2.setRemoteAddress("192.168.1.2");
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Header1", "Value1");
        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("Header1", "Value1");
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent2.setRequestHeaders(requestHeaders2);
        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("Header1", "Value1");
        Map<String, String> responseHeaders2 = new HashMap<>();
        responseHeaders2.put("Header1", "Value1");
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent2.setResponseHeaders(responseHeaders2);
        Map<String, String[]> requestParameters1 = new HashMap<>();
        requestParameters1.put("Param1", new String[]{"Value1", "Value2"});
        Map<String, String[]> requestParameters2 = new HashMap<>();
        requestParameters2.put("Param1", new String[]{"Value1", "Value2"});
        accessEvent1.setRequestParameters(requestParameters1);
        accessEvent2.setRequestParameters(requestParameters2);
        accessEvent1.setLocalPort(8080);
        accessEvent2.setLocalPort(8080);
        accessEvent1.setStatusCode(200);
        accessEvent2.setStatusCode(200);

        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentObjects_differentValues() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        accessEvent1.setTimeStamp(1234567890123L);
        accessEvent2.setTimeStamp(1234567890124L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/test/uri");
        accessEvent2.setRequestURI("/test/otheruri");
        accessEvent1.setRequestURL("http://example.com/test/uri");
        accessEvent2.setRequestURL("http://example.com/test/otheruri");
        accessEvent1.setRemoteHost("192.168.1.1");
        accessEvent2.setRemoteHost("192.168.1.3");
        accessEvent1.setRemoteUser("testuser");
        accessEvent2.setRemoteUser("testuser2");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent2.setProtocol("HTTP/1.2");
        accessEvent1.setMethod("GET");
        accessEvent2.setMethod("POST");
        accessEvent1.setServerName("example.com");
        accessEvent2.setServerName("example2.com");
        accessEvent1.setRemoteAddress("192.168.1.2");
        accessEvent2.setRemoteAddress("192.168.1.4");
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Header1", "Value1");
        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("Header1", "Value2");
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent2.setRequestHeaders(requestHeaders2);
        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("Header1", "Value1");
        Map<String, String> responseHeaders2 = new HashMap<>();
        responseHeaders2.put("Header1", "Value2");
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent2.setResponseHeaders(responseHeaders2);
        Map<String, String[]> requestParameters1 = new HashMap<>();
        requestParameters1.put("Param1", new String[]{"Value1", "Value2"});
        Map<String, String[]> requestParameters2 = new HashMap<>();
        requestParameters2.put("Param1", new String[]{"Value3", "Value4"});
        accessEvent1.setRequestParameters(requestParameters1);
        accessEvent2.setRequestParameters(requestParameters2);
        accessEvent1.setLocalPort(8080);
        accessEvent2.setLocalPort(8081);
        accessEvent1.setStatusCode(200);
        accessEvent2.setStatusCode(404);

        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEvent = new AccessEvent();

        Long timeStamp = 1234567890123L;
        accessEvent.setTimeStamp(timeStamp);
        long expectedHashCode = timeStamp.hashCode();
        accessEvent.setLoggerContext(new LoggerContext());
        expectedHashCode = 31 * expectedHashCode + accessEvent.loggerContext.hashCode();
        accessEvent.setRequestURI("/test/uri");
        expectedHashCode = 31 * expectedHashCode + accessEvent.requestURI.hashCode();
        accessEvent.setRequestURL("http://example.com/test/uri");
        expectedHashCode = 31 * expectedHashCode + accessEvent.requestURL.hashCode();
        accessEvent.setRemoteHost("192.168.1.1");
        expectedHashCode = 31 * expectedHashCode + accessEvent.remoteHost.hashCode();
        accessEvent.setRemoteUser("testuser");
        expectedHashCode = 31 * expectedHashCode + accessEvent.remoteUser.hashCode();
        accessEvent.setProtocol("HTTP/1.1");
        expectedHashCode = 31 * expectedHashCode + accessEvent.protocol.hashCode();
        accessEvent.setMethod("GET");
        expectedHashCode = 31 * expectedHashCode + accessEvent.method.hashCode();
        accessEvent.setServerName("example.com");
        expectedHashCode = 31 * expectedHashCode + accessEvent.serverName.hashCode();
        accessEvent.setRemoteAddress("192.168.1.2");
        expectedHashCode = 31 * expectedHashCode + accessEvent.remoteAddress.hashCode();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        accessEvent.setRequestHeaders(requestHeaders);
        expectedHashCode = 31 * expectedHashCode + requestHeaders.hashCode();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        accessEvent.setResponseHeaders(responseHeaders);
        expectedHashCode = 31 * expectedHashCode + responseHeaders.hashCode();
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Param1", new String[]{"Value1", "Value2"});
        accessEvent.setRequestParameters(requestParameters);
        expectedHashCode = 31 * expectedHashCode + requestParameters.hashCode();
        accessEvent.setLocalPort(8080);
        expectedHashCode = 31 * expectedHashCode + accessEvent.localPort;
        accessEvent.setStatusCode(200);
        expectedHashCode = 31 * expectedHashCode + accessEvent.statusCode;

        int calculatedHashCode = accessEvent.hashCode();

        assertEquals(expectedHashCode, calculatedHashCode);
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(1234567890123L);
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setRequestURI("/test/uri");
        accessEvent.setRequestURL("http://example.com/test/uri");
        accessEvent.setRemoteHost("192.168.1.1");
        accessEvent.setRemoteUser("testuser");
        accessEvent.setProtocol("HTTP/1.1");
        accessEvent.setMethod("GET");
        accessEvent.setServerName("example.com");
        accessEvent.setRemoteAddress("192.168.1.2");
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        accessEvent.setRequestHeaders(requestHeaders);
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        accessEvent.setResponseHeaders(responseHeaders);
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Param1", new String[]{"Value1", "Value2"});
        accessEvent.setRequestParameters(requestParameters);
        accessEvent.setLocalPort(8080);
        accessEvent.setStatusCode(200);

        String expectedString = "AccessEvent[loggerContext=" + accessEvent.loggerContext + ", timeStamp=" + accessEvent.timeStamp + "]";

        String actualString = accessEvent.toString();

        assertEquals(expectedString, actualString);
    }
}