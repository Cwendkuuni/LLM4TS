package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testAccessEvent() {
        // Create an instance of AccessEvent with default values
        AccessEvent accessEvent = new AccessEvent();

        // Test default values
        assertNull(accessEvent.getTimeStamp());
        assertNull(accessEvent.getLoggerContext());
        assertNull(accessEvent.getRequestURI());
        assertNull(accessEvent.getRequestURL());
        assertNull(accessEvent.getRemoteHost());
        assertNull(accessEvent.getRemoteUser());
        assertNull(accessEvent.getProtocol());
        assertNull(accessEvent.getMethod());
        assertNull(accessEvent.getServerName());
        assertNull(accessEvent.getRemoteAddress());
        assertNull(accessEvent.getRequestHeaders());
        assertNull(accessEvent.getResponseHeaders());
        assertNull(accessEvent.getRequestParameters());
        assertEquals(0, accessEvent.getLocalPort());
        assertEquals(0, accessEvent.getStatusCode());

        // Test setters and getters

        // TimeStamp
        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());

        // LoggerContext
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());

        // RequestURI
        String requestURI = "testURI";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());

        // RequestURL
        String requestURL = "testURL";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());

        // RemoteHost
        String remoteHost = "testHost";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());

        // RemoteUser
        String remoteUser = "testUser";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());

        // Protocol
        String protocol = "testProtocol";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());

        // Method
        String method = "testMethod";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());

        // ServerName
        String serverName = "testServerName";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());

        // RemoteAddress
        String remoteAddress = "testRemoteAddress";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());

        // RequestHeaders
        Map<String, String> requestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());

        // ResponseHeaders
        Map<String, String> responseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());

        // RequestParameters
        Map<String, String[]> requestParameters = new HashMap<>();
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());

        // LocalPort
        int localPort = 1;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());

        // StatusCode
        int statusCode = 1;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());

        // Test equals method
        AccessEvent accessEvent2 = new AccessEvent();
        assertFalse(accessEvent.equals(accessEvent2));

        accessEvent2.setTimeStamp(timeStamp);
        accessEvent2.setLoggerContext(loggerContext);
        accessEvent2.setRequestURI(requestURI);
        accessEvent2.setRequestURL(requestURL);
        accessEvent2.setRemoteHost(remoteHost);
        accessEvent2.setRemoteUser(remoteUser);
        accessEvent2.setProtocol(protocol);
        accessEvent2.setMethod(method);
        accessEvent2.setServerName(serverName);
        accessEvent2.setRemoteAddress(remoteAddress);
        accessEvent2.setRequestHeaders(requestHeaders);
        accessEvent2.setResponseHeaders(responseHeaders);
        accessEvent2.setRequestParameters(requestParameters);
        accessEvent2.setLocalPort(localPort);
        accessEvent2.setStatusCode(statusCode);
        assertTrue(accessEvent.equals(accessEvent2));

        // Test hashCode method
        AccessEvent accessEvent3 = new AccessEvent();
        int hashCode = accessEvent.hashCode();
        AccessEvent accessEvent4 = new AccessEvent();
        int hashCode2 = accessEvent4.hashCode();
        assertNotEquals(hashCode, hashCode2);

        accessEvent3.setTimeStamp(timeStamp);
        accessEvent3.setLoggerContext(loggerContext);
        accessEvent3.setRequestURI(requestURI);
        accessEvent3.setRequestURL(requestURL);
        accessEvent3.setRemoteHost(remoteHost);
        accessEvent3.setRemoteUser(remoteUser);
        accessEvent3.setProtocol(protocol);
        accessEvent3.setMethod(method);
        accessEvent3.setServerName(serverName);
        accessEvent3.setRemoteAddress(remoteAddress);
        accessEvent3.setRequestHeaders(requestHeaders);
        accessEvent3.setResponseHeaders(responseHeaders);
        accessEvent3.setRequestParameters(requestParameters);
        accessEvent3.setLocalPort(localPort);
        accessEvent3.setStatusCode(statusCode);

        assertEquals(hashCode, accessEvent3.hashCode());
    }

    @Test
    public void testToString() {
        // Create an instance of AccessEvent
        AccessEvent accessEvent = new AccessEvent();

        // Test default values
        String toString = accessEvent.toString();
        assertTrue(toString.contains("AccessEvent[loggerContext="));
        assertTrue(toString.contains("timeStamp="));
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNullObject() {
        AccessEvent accessEvent = new AccessEvent();
        assertNotEquals(accessEvent, null);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsDifferentClass() {
        AccessEvent accessEvent = new AccessEvent();
        String string = "testString";
        assertNotEquals(accessEvent, string);
    }
}