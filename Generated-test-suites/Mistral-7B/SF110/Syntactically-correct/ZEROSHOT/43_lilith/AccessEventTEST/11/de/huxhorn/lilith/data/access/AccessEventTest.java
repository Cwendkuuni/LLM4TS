package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testSetAndGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);
        Long retrievedTimeStamp = event.getTimeStamp();
        assertEquals(timeStamp, retrievedTimeStamp);
    }

    @Test
    public void testSetAndGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        LoggerContext retrievedLoggerContext = event.getLoggerContext();
        assertEquals(loggerContext, retrievedLoggerContext);
    }

    @Test
    public void testSetAndGetRequestURI() {
        AccessEvent event = new AccessEvent();
        String requestURI = "/test";
        event.setRequestURI(requestURI);
        String retrievedRequestURI = event.getRequestURI();
        assertEquals(requestURI, retrievedRequestURI);
    }

    @Test
    public void testSetAndGetRequestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "http://example.com/test";
        event.setRequestURL(requestURL);
        String retrievedRequestURL = event.getRequestURL();
        assertEquals(requestURL, retrievedRequestURL);
    }

    @Test
    public void testSetAndGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "127.0.0.1";
        event.setRemoteHost(remoteHost);
        String retrievedRemoteHost = event.getRemoteHost();
        assertEquals(remoteHost, retrievedRemoteHost);
    }

    @Test
    public void testSetAndGetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "testuser";
        event.setRemoteUser(remoteUser);
        String retrievedRemoteUser = event.getRemoteUser();
        assertEquals(remoteUser, retrievedRemoteUser);
    }

    @Test
    public void testSetAndGetProtocol() {
        AccessEvent event = new AccessEvent();
        String protocol = "HTTP/1.1";
        event.setProtocol(protocol);
        String retrievedProtocol = event.getProtocol();
        assertEquals(protocol, retrievedProtocol);
    }

    @Test
    public void testSetAndGetMethod() {
        AccessEvent event = new AccessEvent();
        String method = "GET";
        event.setMethod(method);
        String retrievedMethod = event.getMethod();
        assertEquals(method, retrievedMethod);
    }

    @Test
    public void testSetAndGetServerName() {
        AccessEvent event = new AccessEvent();
        String serverName = "example.com";
        event.setServerName(serverName);
        String retrievedServerName = event.getServerName();
        assertEquals(serverName, retrievedServerName);
    }

    @Test
    public void testSetAndGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "127.0.0.1";
        event.setRemoteAddress(remoteAddress);
        String retrievedRemoteAddress = event.getRemoteAddress();
        assertEquals(remoteAddress, retrievedRemoteAddress);
    }

    @Test
    public void testSetAndGetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("header1", "value1");
        event.setRequestHeaders(requestHeaders);
        Map<String, String> retrievedRequestHeaders = event.getRequestHeaders();
        assertEquals(requestHeaders, retrievedRequestHeaders);
    }

    @Test
    public void testSetAndGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("header1", "value1");
        event.setResponseHeaders(responseHeaders);
        Map<String, String> retrievedResponseHeaders = event.getResponseHeaders();
        assertEquals(responseHeaders, retrievedResponseHeaders);
    }

    @Test
    public void testSetAndGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        event.setRequestParameters(requestParameters);
        Map<String, String[]> retrievedRequestParameters = event.getRequestParameters();
        assertEquals(requestParameters, retrievedRequestParameters);
    }

    @Test
    public void testSetAndGetLocalPort() {
        AccessEvent event = new AccessEvent();
        int localPort = 8080;
        event.setLocalPort(localPort);
        int retrievedLocalPort = event.getLocalPort();
        assertEquals(localPort, retrievedLocalPort);
    }

    @Test
    public void testSetAndGetStatusCode() {
        AccessEvent event = new AccessEvent();
        int statusCode = 200;
        event.setStatusCode(statusCode);
        int retrievedStatusCode = event.getStatusCode();
        assertEquals(statusCode, retrievedStatusCode);
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://example.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testuser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("example.com");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://example.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testuser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("example.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://example.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testuser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("example.com");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://example.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testuser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("example.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        int event1HashCode = event1.hashCode();
        int event2HashCode = event2.hashCode();

        assertEquals(event1HashCode, event2HashCode);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        String expectedToString = "AccessEvent[loggerContext=LoggerContext, timeStamp=1]";
        String actualToString = event.toString();
        assertEquals(expectedToString, actualToString);
    }
}