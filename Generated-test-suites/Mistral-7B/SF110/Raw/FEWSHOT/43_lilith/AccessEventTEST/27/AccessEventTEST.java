package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/path/to/resource";
        String requestURL = "http://example.com/path/to/resource";
        String remoteHost = "192.168.0.1";
        String remoteUser = "user";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "example.com";
        String remoteAddress = "192.168.0.2";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
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
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/path/to/resource");
        event1.setRequestURL("http://example.com/path/to/resource");
        event1.setRemoteHost("192.168.0.1");
        event1.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("example.com");
        event1.setRemoteAddress("192.168.0.2");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/path/to/resource");
        event2.setRequestURL("http://example.com/path/to/resource");
        event2.setRemoteHost("192.168.0.1");
        event2.setRemoteUser("user");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("example.com");
        event2.setRemoteAddress("192.168.0.2");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertTrue(event1.equals(event2));
        assertTrue(event2.equals(event1));

        event2.setLocalPort(8081);
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));

        event2.setLocalPort(8080);
        event1.setRequestURI("/path/to/another/resource");
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/path/to/resource");
        event1.setRequestURL("http://example.com/path/to/resource");
        event1.setRemoteHost("192.168.0.1");
        event1.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("example.com");
        event1.setRemoteAddress("192.168.0.2");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/path/to/resource");
        event2.setRequestURL("http://example.com/path/to/resource");
        event2.setRemoteHost("192.168.0.1");
        event2.setRemoteUser("user");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("example.com");
        event2.setRemoteAddress("192.168.0.2");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertEquals(event1.hashCode(), event2.hashCode());

        event2.setLocalPort(8081);
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setLocalPort(8080);
        event1.setRequestURI("/path/to/another/resource");
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();

        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/path/to/resource");
        event.setRequestURL("http://example.com/path/to/resource");
        event.setRemoteHost("192.168.0.1");
        event.setRemoteUser("user");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("example.com");
        event.setRemoteAddress("192.168.0.2");
        event.setRequestHeaders(new HashMap<>());
        event.setResponseHeaders(new HashMap<>());
        event.setRequestParameters(new HashMap<>());
        event.setLocalPort(8080);
        event.setStatusCode(200);

        String expected = "AccessEvent[loggerContext=LoggerContext@1b6d351, timeStamp=1]";
        String actual = event.toString();
        assertEquals(expected, actual);
    }
}