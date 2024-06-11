package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testAccessEventWithDefaultValues() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getTimeStamp());
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
    }

    @Test
    public void testSettersAndGetters() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 123L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer.com";
        String remoteAddress = "192.168.1.1";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
        int statusCode = 200;

        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());

        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());

        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());

        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());

        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());

        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());

        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());

        event.setMethod(method);
        assertEquals(method, event.getMethod());

        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());

        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());

        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());

        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());

        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());

        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());

        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());

        event.toString();
    }

    @Test
    public void testEqualsAndHashcode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testUser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("testServer.com");
        event1.setRemoteAddress("192.168.1.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://test.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testUser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("testServer.com");
        event2.setRemoteAddress("192.168.1.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        event3.setTimeStamp(2L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("/test");
        event3.setRequestURL("http://test.com/test");
        event3.setRemoteHost("127.0.0.1");
        event3.setRemoteUser("testUser");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("testServer.com");
        event3.setRemoteAddress("192.168.1.1");
        event3.setRequestHeaders(new HashMap<>());
        event3.setResponseHeaders(new HashMap<>());
        event3.setRequestParameters(new HashMap<>());
        event3.setLocalPort(8080);
        event3.setStatusCode(200);

        assertTrue(event1.equals(event2));
        assertTrue(event1.hashCode() == event2.hashCode());

        assertFalse(event1.equals(event3));
        assertFalse(event1.hashCode() == event3.hashCode());

        assertFalse(event1.equals(null));
        assertFalse(event1.hashCode() == 0);

        assertFalse(event1.equals(new Object()));
        assertNotEquals(event1.hashCode(), new Object().hashCode());
    }
}