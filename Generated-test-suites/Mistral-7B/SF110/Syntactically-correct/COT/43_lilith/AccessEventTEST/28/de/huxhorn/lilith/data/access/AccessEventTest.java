package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    // Basic test cases for getters and setters

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    @Test
    public void testSetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    @Test
    public void testSetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent event = new AccessEvent();
        String requestURI = "testURI";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testSetRequestURI() {
        AccessEvent event = new AccessEvent();
        String requestURI = "testURI";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "testURL";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testSetRequestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "testURL";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "testHost";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testSetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "testHost";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testGetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "testUser";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    @Test
    public void testSetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "testUser";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    @Test
    public void testGetProtocol() {
        AccessEvent event = new AccessEvent();
        String protocol = "testProtocol";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    @Test
    public void testSetProtocol() {
        AccessEvent event = new AccessEvent();
        String protocol = "testProtocol";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    @Test
    public void testGetMethod() {
        AccessEvent event = new AccessEvent();
        String method = "testMethod";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    @Test
    public void testSetMethod() {
        AccessEvent event = new AccessEvent();
        String method = "testMethod";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    @Test
    public void testGetServerName() {
        AccessEvent event = new AccessEvent();
        String serverName = "testServerName";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testSetServerName() {
        AccessEvent event = new AccessEvent();
        String serverName = "testServerName";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "testRemoteAddress";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }

    @Test
    public void testSetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "testRemoteAddress";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testSetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testSetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testSetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testGetLocalPort() {
        AccessEvent event = new AccessEvent();
        int localPort = 1234;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    @Test
    public void testSetLocalPort() {
        AccessEvent event = new AccessEvent();
        int localPort = 1234;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    @Test
    public void testGetStatusCode() {
        AccessEvent event = new AccessEvent();
        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testSetStatusCode() {
        AccessEvent event = new AccessEvent();
        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    // Test edge cases and exceptions

    @Test(expected = NullPointerException.class)
    public void testNullLoggerContext() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullRequestURI() {
        AccessEvent event = new AccessEvent();
        event.setRequestURI(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullRequestURL() {
        AccessEvent event = new AccessEvent();
        event.setRequestURL(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullRemoteHost() {
        AccessEvent event = new AccessEvent();
        event.setRemoteHost(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullRemoteUser() {
        AccessEvent event = new AccessEvent();
        event.setRemoteUser(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullProtocol() {
        AccessEvent event = new AccessEvent();
        event.setProtocol(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullMethod() {
        AccessEvent event = new AccessEvent();
        event.setMethod(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullServerName() {
        AccessEvent event = new AccessEvent();
        event.setServerName(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullRemoteAddress() {
        AccessEvent event = new AccessEvent();
        event.setRemoteAddress(null);
    }

    @Test
    public void testEmptyMapRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        assertTrue(event.getRequestHeaders().isEmpty());
    }

    @Test
    public void testEmptyMapResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        assertTrue(event.getResponseHeaders().isEmpty());
    }

    @Test
    public void testEmptyMapRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        assertTrue(event.getRequestParameters().isEmpty());
    }
}