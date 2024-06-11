package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters() {
        AccessEvent accessEvent = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "localhost";
        String remoteAddress = "127.0.0.1";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
        int statusCode = 200;

        accessEvent.setTimeStamp(timeStamp);
        accessEvent.setLoggerContext(loggerContext);
        accessEvent.setRequestURI(requestURI);
        accessEvent.setRequestURL(requestURL);
        accessEvent.setRemoteHost(remoteHost);
        accessEvent.setRemoteUser(remoteUser);
        accessEvent.setProtocol(protocol);
        accessEvent.setMethod(method);
        accessEvent.setServerName(serverName);
        accessEvent.setRemoteAddress(remoteAddress);
        accessEvent.setRequestHeaders(requestHeaders);
        accessEvent.setResponseHeaders(responseHeaders);
        accessEvent.setRequestParameters(requestParameters);
        accessEvent.setLocalPort(localPort);
        accessEvent.setStatusCode(statusCode);

        assertEquals(timeStamp, accessEvent.getTimeStamp());
        assertEquals(loggerContext, accessEvent.getLoggerContext());
        assertEquals(requestURI, accessEvent.getRequestURI());
        assertEquals(requestURL, accessEvent.getRequestURL());
        assertEquals(remoteHost, accessEvent.getRemoteHost());
        assertEquals(remoteUser, accessEvent.getRemoteUser());
        assertEquals(protocol, accessEvent.getProtocol());
        assertEquals(method, accessEvent.getMethod());
        assertEquals(serverName, accessEvent.getServerName());
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
        assertEquals(requestParameters, accessEvent.getRequestParameters());
        assertEquals(localPort, accessEvent.getLocalPort());
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEqualsMethod() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        AccessEvent accessEvent3 = new AccessEvent();

        accessEvent1.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/test");
        accessEvent1.setRequestURL("http://test.com/test");
        accessEvent1.setRemoteHost("127.0.0.1");
        accessEvent1.setRemoteUser("testUser");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent1.setServerName("localhost");
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        accessEvent2.setTimeStamp(1L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("/test");
        accessEvent2.setRequestURL("http://test.com/test");
        accessEvent2.setRemoteHost("127.0.0.1");
        accessEvent2.setRemoteUser("testUser");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent2.setMethod("GET");
        accessEvent2.setServerName("localhost");
        accessEvent2.setRemoteAddress("127.0.0.1");
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent2.setLocalPort(8080);
        accessEvent2.setStatusCode(200);

        accessEvent3.setTimeStamp(2L);
        accessEvent3.setLoggerContext(new LoggerContext());
        accessEvent3.setRequestURI("/test2");
        accessEvent3.setRequestURL("http://test.com/test2");
        accessEvent3.setRemoteHost("127.0.0.1");
        accessEvent3.setRemoteUser("testUser2");
        accessEvent3.setProtocol("HTTP/1.1");
        accessEvent3.setMethod("POST");
        accessEvent3.setServerName("localhost");
        accessEvent3.setRemoteAddress("127.0.0.1");
        accessEvent3.setRequestHeaders(new HashMap<>());
        accessEvent3.setResponseHeaders(new HashMap<>());
        accessEvent3.setRequestParameters(new HashMap<>());
        accessEvent3.setLocalPort(8081);
        accessEvent3.setStatusCode(201);

        assertTrue(accessEvent1.equals(accessEvent2));
        assertFalse(accessEvent1.equals(accessEvent3));
        assertFalse(accessEvent2.equals(accessEvent3));
        assertFalse(accessEvent1.equals(null));
        assertFalse(accessEvent1.equals(""));
    }

    @Test
    public void testHashCodeMethod() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        accessEvent1.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/test");
        accessEvent1.setRequestURL("http://test.com/test");
        accessEvent1.setRemoteHost("127.0.0.1");
        accessEvent1.setRemoteUser("testUser");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent1.setServerName("localhost");
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        accessEvent2.setTimeStamp(1L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("/test");
        accessEvent2.setRequestURL("http://test.com/test");
        accessEvent2.setRemoteHost("127.0.0.1");
        accessEvent2.setRemoteUser("testUser");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent2.setMethod("GET");
        accessEvent2.setServerName("localhost");
        accessEvent2.setRemoteAddress("127.0.0.1");
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent2.setLocalPort(8080);
        accessEvent2.setStatusCode(200);

        int accessEvent1HashCode = accessEvent1.hashCode();
        int accessEvent2HashCode = accessEvent2.hashCode();

        assertEquals(accessEvent1HashCode, accessEvent2HashCode);
    }

    @Test
    public void testToStringMethod() {
        AccessEvent accessEvent = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "localhost";
        String remoteAddress = "127.0.0.1";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
        int statusCode = 200;

        accessEvent.setTimeStamp(timeStamp);
        accessEvent.setLoggerContext(loggerContext);
        accessEvent.setRequestURI(requestURI);
        accessEvent.setRequestURL(requestURL);
        accessEvent.setRemoteHost(remoteHost);
        accessEvent.setRemoteUser(remoteUser);
        accessEvent.setProtocol(protocol);
        accessEvent.setMethod(method);
        accessEvent.setServerName(serverName);
        accessEvent.setRemoteAddress(remoteAddress);
        accessEvent.setRequestHeaders(requestHeaders);
        accessEvent.setResponseHeaders(responseHeaders);
        accessEvent.setRequestParameters(requestParameters);
        accessEvent.setLocalPort(localPort);
        accessEvent.setStatusCode(statusCode);

        String expectedResult = "AccessEvent[loggerContext=LoggerContext[], timeStamp=1]";

        String actualResult = accessEvent.toString();

        assertEquals(expectedResult, actualResult);
    }
}