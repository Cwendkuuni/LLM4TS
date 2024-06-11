package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class AccessEventTest {
    @Test
    public void testGettersAndSetters_timeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    @Test
    public void testGettersAndSetters_loggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_requestURI() {
        AccessEvent event = new AccessEvent();
        String requestURI = "/test/requestURI";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "http://test.com/test/requestURL";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "192.168.0.1";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "testUser";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_protocol() {
        AccessEvent event = new AccessEvent();
        String protocol = "HTTP/1.1";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    @Test
    public void testGettersAndSetters_method() {
        AccessEvent event = new AccessEvent();
        String method = "GET";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    @Test
    public void testGettersAndSetters_serverName() {
        AccessEvent event = new AccessEvent();
        String serverName = "test.com";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "192.168.0.2";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_requestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_localPort() {
        AccessEvent event = new AccessEvent();
        int localPort = 8080;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        AccessEvent event = new AccessEvent();
        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test/requestURI");
        event1.setRequestURL("http://test.com/test/requestURL");
        event1.setRemoteHost("192.168.0.1");
        event1.setRemoteUser("testUser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("test.com");
        event1.setRemoteAddress("192.168.0.2");
        event1.setRequestHeaders(new HashMap<String, String>());
        event1.setResponseHeaders(new HashMap<String, String>());
        event1.setRequestParameters(new HashMap<String, String[]>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        assertEquals(event1, event1);
        assertNotEquals(event1, event2);
        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test/requestURI");
        event2.setRequestURL("http://test.com/test/requestURL");
        event2.setRemoteHost("192.168.0.1");
        event2.setRemoteUser("testUser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("test.com");
        event2.setRemoteAddress("192.168.0.2");
        event2.setRequestHeaders(new HashMap<String, String>());
        event2.setResponseHeaders(new HashMap<String, String>());
        event2.setRequestParameters(new HashMap<String, String[]>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);
        assertEquals(event1, event2);

        event3.setTimeStamp(2L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("/test/requestURI");
        event3.setRequestURL("http://test.com/test/requestURL");
        event3.setRemoteHost("192.168.0.1");
        event3.setRemoteUser("testUser");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("test.com");
        event3.setRemoteAddress("192.168.0.2");
        event3.setRequestHeaders(new HashMap<String, String>());
        event3.setResponseHeaders(new HashMap<String, String>());
        event3.setRequestParameters(new HashMap<String, String[]>());
        event3.setLocalPort(8080);
        event3.setStatusCode(200);
        assertNotEquals(event1, event3);
    }

    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);

        int hashCode1 = event.hashCode();
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test/requestURI");
        event.setRequestURL("http://test.com/test/requestURL");
        event.setRemoteHost("192.168.0.1");
        event.setRemoteUser("testUser");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("test.com");
        event.setRemoteAddress("192.168.0.2");
        event.setRequestHeaders(new HashMap<String, String>());
        event.setResponseHeaders(new HashMap<String, String>());
        event.setRequestParameters(new HashMap<String, String[]>());
        event.setLocalPort(8080);
        event.setStatusCode(200);

        int hashCode2 = event.hashCode();
        assertEquals(hashCode1, hashCode2);

        event.setTimeStamp(null);
        int hashCode3 = event.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test/requestURI");
        event.setRequestURL("http://test.com/test/requestURL");
        event.setRemoteHost("192.168.0.1");
        event.setRemoteUser("testUser");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("test.com");
        event.setRemoteAddress("192.168.0.2");
        event.setRequestHeaders(new HashMap<String, String>());
        event.setResponseHeaders(new HashMap<String, String>());
        event.setRequestParameters(new HashMap<String, String[]>());
        event.setLocalPort(8080);
        event.setStatusCode(200);

        String toString = event.toString();
        assertTrue(toString.contains("AccessEvent["));
        assertTrue(toString.contains("loggerContext="));
        assertTrue(toString.contains("timeStamp="));
    }
}