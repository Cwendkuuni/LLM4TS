package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters_timeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 123L;
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
        String requestURI = "/test";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "http://test.com/test";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "192.168.1.1";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "user";
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
        String serverName = "localhost";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "192.168.1.2";
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
        assertEquals(event1, event1);
        assertNotEquals(event1, event2);

        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event2.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event2.setRequestURL("http://test.com/test");
        event1.setRemoteHost("192.168.1.1");
        event2.setRemoteHost("192.168.1.1");
        event1.setRemoteUser("user");
        event2.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event2.setMethod("GET");
        event1.setServerName("localhost");
        event2.setServerName("localhost");
        event1.setRemoteAddress("192.168.1.2");
        event2.setRemoteAddress("192.168.1.2");
        event1.setRequestHeaders(new HashMap<>());
        event2.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event2.setLocalPort(8080);
        event1.setStatusCode(200);
        event2.setStatusCode(200);

        event1.setLoggerContext(new LoggerContext("test"));
        assertNotEquals(event1, event2);

        event1.setMethod("POST");
        assertNotEquals(event1, event2);

        event1.setProtocol("HTTP/1.2");
        assertNotEquals(event1, event2);

        event1.setRemoteAddress("192.168.1.3");
        assertNotEquals(event1, event2);

        event1.setRemoteUser("admin");
        assertNotEquals(event1, event2);

        event1.setRequestURI("/test2");
        assertNotEquals(event1, event2);

        event1.setRequestURL("http://test2.com/test2");
        assertNotEquals(event1, event2);

        event1.setServerName("test.com");
        assertNotEquals(event1, event2);

        event1.setTimeStamp(2L);
        assertNotEquals(event1, event2);

        event1.setLocalPort(8081);
        assertNotEquals(event1, event2);

        event1.setStatusCode(201);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertNotEquals(hashCode1, hashCode2);

        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event2.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event2.setRequestURL("http://test.com/test");
        event1.setRemoteHost("192.168.1.1");
        event2.setRemoteHost("192.168.1.1");
        event1.setRemoteUser("user");
        event2.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event2.setMethod("GET");
        event1.setServerName("localhost");
        event2.setServerName("localhost");
        event1.setRemoteAddress("192.168.1.2");
        event2.setRemoteAddress("192.168.1.2");
        event1.setRequestHeaders(new HashMap<>());
        event2.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event2.setLocalPort(8080);
        event1.setStatusCode(200);
        event2.setStatusCode(200);

        event1.setLoggerContext(new LoggerContext("test"));
        int hashCode3 = event1.hashCode();
        event2.setLoggerContext(new LoggerContext("test"));
        int hashCode4 = event2.hashCode();
        assertEquals(hashCode3, hashCode4);

        event1.setMethod("POST");
        int hashCode5 = event1.hashCode();
        event2.setMethod("POST");
        int hashCode6 = event2.hashCode();
        assertEquals(hashCode5, hashCode6);

        event1.setProtocol("HTTP/1.2");
        int hashCode7 = event1.hashCode();
        event2.setProtocol("HTTP/1.2");
        int hashCode8 = event2.hashCode();
        assertEquals(hashCode7, hashCode8);

        event1.setRemoteAddress("192.168.1.3");
        int hashCode9 = event1.hashCode();
        event2.setRemoteAddress("192.168.1.3");
        int hashCode10 = event2.hashCode();
        assertEquals(hashCode9, hashCode10);

        event1.setRemoteUser("admin");
        int hashCode11 = event1.hashCode();
        event2.setRemoteUser("admin");
        int hashCode12 = event2.hashCode();
        assertEquals(hashCode11, hashCode12);

        event1.setRequestURI("/test2");
        int hashCode13 = event1.hashCode();
        event2.setRequestURI("/test2");
        int hashCode14 = event2.hashCode();
        assertEquals(hashCode13, hashCode14);

        event1.setRequestURL("http://test2.com/test2");
        int hashCode15 = event1.hashCode();
        event2.setRequestURL("http://test2.com/test2");
        int hashCode16 = event2.hashCode();
        assertEquals(hashCode15, hashCode16);

        event1.setServerName("test.com");
        int hashCode17 = event1.hashCode();
        event2.setServerName("test.com");
        int hashCode18 = event2.hashCode();
        assertEquals(hashCode17, hashCode18);

        event1.setTimeStamp(2L);
        int hashCode19 = event1.hashCode();
        event2.setTimeStamp(2L);
        int hashCode20 = event2.hashCode();
        assertEquals(hashCode19, hashCode20);

        event1.setLocalPort(8081);
        int hashCode21 = event1.hashCode();
        event2.setLocalPort(8081);
        int hashCode22 = event2.hashCode();
        assertEquals(hashCode21, hashCode22);

        event1.setStatusCode(201);
        int hashCode23 = event1.hashCode();
        event2.setStatusCode(201);
        int hashCode24 = event2.hashCode();
        assertEquals(hashCode23, hashCode24);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        StringBuilder result = new StringBuilder();
        result.append("AccessEvent[loggerContext=");
        result.append(event.getLoggerContext());
        result.append(", timeStamp=");
        result.append(event.getTimeStamp());
        result.append("]");
        assertEquals(result.toString(), event.toString());
    }
}