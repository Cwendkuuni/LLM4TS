package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testAccessEventWithValidValues() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testuser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testserver.com";
        String remoteAddress = "127.0.0.2";
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
    public void testAccessEventEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        Long timeStamp1 = 1L;
        Long timeStamp2 = 1L;
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        String requestURI1 = "/test";
        String requestURI2 = "/test";
        String requestURL1 = "http://test.com/test";
        String requestURL2 = "http://test.com/test";
        String remoteHost1 = "127.0.0.1";
        String remoteHost2 = "127.0.0.1";
        String remoteUser1 = "testuser";
        String remoteUser2 = "testuser";
        String protocol1 = "HTTP/1.1";
        String protocol2 = "HTTP/1.1";
        String method1 = "GET";
        String method2 = "GET";
        String serverName1 = "testserver.com";
        String serverName2 = "testserver.com";
        String remoteAddress1 = "127.0.0.2";
        String remoteAddress2 = "127.0.0.2";
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        Map<String, String[]> requestParameters1 = new HashMap<>();
        Map<String, String[]> requestParameters2 = new HashMap<>();
        int localPort1 = 8080;
        int localPort2 = 8080;
        int statusCode1 = 200;
        int statusCode2 = 200;

        accessEvent1.setTimeStamp(timeStamp1);
        accessEvent1.setLoggerContext(loggerContext1);
        accessEvent1.setRequestURI(requestURI1);
        accessEvent1.setRequestURL(requestURL1);
        accessEvent1.setRemoteHost(remoteHost1);
        accessEvent1.setRemoteUser(remoteUser1);
        accessEvent1.setProtocol(protocol1);
        accessEvent1.setMethod(method1);
        accessEvent1.setServerName(serverName1);
        accessEvent1.setRemoteAddress(remoteAddress1);
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent1.setRequestParameters(requestParameters1);
        accessEvent1.setLocalPort(localPort1);
        accessEvent1.setStatusCode(statusCode1);

        accessEvent2.setTimeStamp(timeStamp2);
        accessEvent2.setLoggerContext(loggerContext2);
        accessEvent2.setRequestURI(requestURI2);
        accessEvent2.setRequestURL(requestURL2);
        accessEvent2.setRemoteHost(remoteHost2);
        accessEvent2.setRemoteUser(remoteUser2);
        accessEvent2.setProtocol(protocol2);
        accessEvent2.setMethod(method2);
        accessEvent2.setServerName(serverName2);
        accessEvent2.setRemoteAddress(remoteAddress2);
        accessEvent2.setRequestHeaders(requestHeaders2);
        accessEvent2.setResponseHeaders(responseHeaders2);
        accessEvent2.setRequestParameters(requestParameters2);
        accessEvent2.setLocalPort(localPort2);
        accessEvent2.setStatusCode(statusCode2);

        // Test equals with same values
        assertTrue(accessEvent1.equals(accessEvent1));

        // Test equals with different objects but same values
        assertTrue(accessEvent1.equals(accessEvent2));

        // Test equals with different values
        AccessEvent accessEvent3 = new AccessEvent();
        accessEvent3.setTimeStamp(2L);
        accessEvent3.setLoggerContext(loggerContext1);
        accessEvent3.setRequestURI(requestURI1);
        accessEvent3.setRequestURL(requestURL1);
        accessEvent3.setRemoteHost(remoteHost1);
        accessEvent3.setRemoteUser(remoteUser1);
        accessEvent3.setProtocol(protocol1);
        accessEvent3.setMethod(method1);
        accessEvent3.setServerName(serverName1);
        accessEvent3.setRemoteAddress(remoteAddress1);
        accessEvent3.setRequestHeaders(requestHeaders1);
        accessEvent3.setResponseHeaders(responseHeaders1);
        accessEvent3.setRequestParameters(requestParameters1);
        accessEvent3.setLocalPort(localPort1);
        accessEvent3.setStatusCode(statusCode1);

        // Test equals with different values
        assertFalse(accessEvent1.equals(accessEvent3));

        // Test equals with null
        assertFalse(accessEvent1.equals(null));
    }

    @Test
    public void testAccessEventHashCode() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testuser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testserver.com";
        String remoteAddress = "127.0.0.2";
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

        int expectedHashCode = 31 * 31 * 31 * 31 * 31 + timeStamp.hashCode() + loggerContext.hashCode() + requestURI.hashCode() + requestURL.hashCode()
                + remoteHost.hashCode() + remoteUser.hashCode() + protocol.hashCode() + method.hashCode() + serverName.hashCode() + remoteAddress.hashCode()
                + localPort + statusCode;

        int actualHashCode = accessEvent.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }
}