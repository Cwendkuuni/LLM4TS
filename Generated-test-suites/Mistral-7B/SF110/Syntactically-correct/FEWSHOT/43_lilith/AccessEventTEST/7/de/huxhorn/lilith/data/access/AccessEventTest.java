package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer.com";
        String remoteAddress = "127.0.0.2";
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
        event1.setRequestURI("/test1");
        event1.setRequestURL("http://test1.com/test1");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testUser1");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("testServer1.com");
        event1.setRemoteAddress("127.0.0.2");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test1");
        event2.setRequestURL("http://test1.com/test1");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testUser1");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("testServer1.com");
        event2.setRemoteAddress("127.0.0.2");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertTrue(event1.equals(event2));

        event2.setRemoteAddress("127.0.0.3");
        assertFalse(event1.equals(event2));

        event2.setRemoteAddress("127.0.0.2");
        event2.setLoggerContext(new LoggerContext());
        assertFalse(event1.equals(event2));

        event2.setLoggerContext(new LoggerContext());
        event2.setMethod("POST");
        assertFalse(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test1");
        event1.setRequestURL("http://test1.com/test1");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testUser1");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("testServer1.com");
        event1.setRemoteAddress("127.0.0.2");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test1");
        event2.setRequestURL("http://test1.com/test1");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testUser1");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("testServer1.com");
        event2.setRemoteAddress("127.0.0.2");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertEquals(event1.hashCode(), event2.hashCode());

        event2.setRemoteAddress("127.0.0.3");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setRemoteAddress("127.0.0.2");
        event2.setLoggerContext(new LoggerContext());
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setLoggerContext(new LoggerContext());
        event2.setMethod("POST");
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }
}