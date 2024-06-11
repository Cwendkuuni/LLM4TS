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
        String serverName = "testServer";
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

        AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(1L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("/test");
        event3.setRequestURL("http://test.com/test");
        event3.setRemoteHost("127.0.0.1");
        event3.setRemoteUser("testUser");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("testServer");
        event3.setRemoteAddress("127.0.0.2");
        event3.setRequestHeaders(new HashMap<>());
        event3.setResponseHeaders(new HashMap<>());
        event3.setRequestParameters(new HashMap<>());
        event3.setLocalPort(8080);
        event3.setStatusCode(200);

        AccessEvent event4 = new AccessEvent();
        event4.setTimeStamp(2L);
        event4.setLoggerContext(new LoggerContext());
        event4.setRequestURI("/test2");
        event4.setRequestURL("http://test.com/test2");
        event4.setRemoteHost("127.0.0.1");
        event4.setRemoteUser("testUser2");
        event4.setProtocol("HTTP/1.1");
        event4.setMethod("POST");
        event4.setServerName("testServer2");
        event4.setRemoteAddress("127.0.0.3");
        event4.setRequestHeaders(new HashMap<>());
        event4.setResponseHeaders(new HashMap<>());
        event4.setRequestParameters(new HashMap<>());
        event4.setLocalPort(8081);
        event4.setStatusCode(201);

        assertTrue(event1.equals(event1));
        assertTrue(event1.equals(event1));
        assertFalse(event1.equals(event2));
        assertFalse(event1.equals(null));
        assertFalse(event1.equals(""));
        assertFalse(event1.equals(new Object()));
        assertFalse(event1.equals(new Integer(1)));

        assertTrue(event3.equals(event3));
        assertTrue(event3.equals(event3));
        assertTrue(event3.equals(event1));
        assertFalse(event3.equals(event4));
    }

    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer";
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

        int hashCode = event.hashCode();

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(timeStamp);
        event2.setLoggerContext(loggerContext);
        event2.setRequestURI(requestURI);
        event2.setRequestURL(requestURL);
        event2.setRemoteHost(remoteHost);
        event2.setRemoteUser(remoteUser);
        event2.setProtocol(protocol);
        event2.setMethod(method);
        event2.setServerName(serverName);
        event2.setRemoteAddress(remoteAddress);
        event2.setRequestHeaders(requestHeaders);
        event2.setResponseHeaders(responseHeaders);
        event2.setRequestParameters(requestParameters);
        event2.setLocalPort(localPort);
        event2.setStatusCode(statusCode);

        assertEquals(hashCode, event2.hashCode());

        AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(null);
        event3.setLoggerContext(null);
        event3.setRequestURI(null);
        event3.setRequestURL(null);
        event3.setRemoteHost(null);
        event3.setRemoteUser(null);
        event3.setProtocol(null);
        event3.setMethod(null);
        event3.setServerName(null);
        event3.setRemoteAddress(null);
        event3.setRequestHeaders(null);
        event3.setResponseHeaders(null);
        event3.setRequestParameters(null);
        event3.setLocalPort(0);
        event3.setStatusCode(0);

        assertNotEquals(hashCode, event3.hashCode());

        AccessEvent event4 = new AccessEvent();
        event4.setTimeStamp(null);
        event4.setLoggerContext(new LoggerContext());
        event4.setRequestURI("/test");
        event4.setRequestURL("http://test.com/test");
        event4.setRemoteHost("127.0.0.1");
        event4.setRemoteUser("testUser");
        event4.setProtocol("HTTP/1.1");
        event4.setMethod("GET");
        event4.setServerName("testServer");
        event4.setRemoteAddress("127.0.0.2");
        event4.setRequestHeaders(new HashMap<>());
        event4.setResponseHeaders(new HashMap<>());
        event4.setRequestParameters(new HashMap<>());
        event4.setLocalPort(8080);
        event4.setStatusCode(200);

        assertNotEquals(hashCode, event4.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer";
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

        String toString = event.toString();

        String expectedToString = "AccessEvent[loggerContext=LoggerContext[], timeStamp=1, ]";

        assertEquals(expectedToString, toString);
    }
}