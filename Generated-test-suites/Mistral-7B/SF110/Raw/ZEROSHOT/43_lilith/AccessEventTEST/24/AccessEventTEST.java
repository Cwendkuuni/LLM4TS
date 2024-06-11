package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters() {
        AccessEvent accessEvent = new AccessEvent();

        Long expectedTimeStamp = 1L;
        LoggerContext expectedLoggerContext = new LoggerContext();
        String expectedRequestURI = "/test";
        String expectedRequestURL = "http://test.com/test";
        String expectedRemoteHost = "localhost";
        String expectedRemoteUser = "testUser";
        String expectedProtocol = "HTTP/1.1";
        String expectedMethod = "GET";
        String expectedServerName = "testServer.com";
        String expectedRemoteAddress = "127.0.0.1";
        Map<String, String> expectedRequestHeaders = new HashMap<>();
        Map<String, String> expectedResponseHeaders = new HashMap<>();
        Map<String, String[]> expectedRequestParameters = new HashMap<>();
        int expectedLocalPort = 8080;
        int expectedStatusCode = 200;

        accessEvent.setTimeStamp(expectedTimeStamp);
        assertEquals(expectedTimeStamp, accessEvent.getTimeStamp());

        accessEvent.setLoggerContext(expectedLoggerContext);
        assertEquals(expectedLoggerContext, accessEvent.getLoggerContext());

        accessEvent.setRequestURI(expectedRequestURI);
        assertEquals(expectedRequestURI, accessEvent.getRequestURI());

        accessEvent.setRequestURL(expectedRequestURL);
        assertEquals(expectedRequestURL, accessEvent.getRequestURL());

        accessEvent.setRemoteHost(expectedRemoteHost);
        assertEquals(expectedRemoteHost, accessEvent.getRemoteHost());

        accessEvent.setRemoteUser(expectedRemoteUser);
        assertEquals(expectedRemoteUser, accessEvent.getRemoteUser());

        accessEvent.setProtocol(expectedProtocol);
        assertEquals(expectedProtocol, accessEvent.getProtocol());

        accessEvent.setMethod(expectedMethod);
        assertEquals(expectedMethod, accessEvent.getMethod());

        accessEvent.setServerName(expectedServerName);
        assertEquals(expectedServerName, accessEvent.getServerName());

        accessEvent.setRemoteAddress(expectedRemoteAddress);
        assertEquals(expectedRemoteAddress, accessEvent.getRemoteAddress());

        accessEvent.setRequestHeaders(expectedRequestHeaders);
        assertEquals(expectedRequestHeaders, accessEvent.getRequestHeaders());

        accessEvent.setResponseHeaders(expectedResponseHeaders);
        assertEquals(expectedResponseHeaders, accessEvent.getResponseHeaders());

        accessEvent.setRequestParameters(expectedRequestParameters);
        assertEquals(expectedRequestParameters, accessEvent.getRequestParameters());

        accessEvent.setLocalPort(expectedLocalPort);
        assertEquals(expectedLocalPort, accessEvent.getLocalPort());

        accessEvent.setStatusCode(expectedStatusCode);
        assertEquals(expectedStatusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEqualsAndHashCode() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        Long timeStamp1 = 1L;
        Long timeStamp2 = 2L;
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        String requestURI1 = "/test1";
        String requestURI2 = "/test2";
        String requestURL1 = "http://test1.com/test1";
        String requestURL2 = "http://test2.com/test2";
        String remoteHost1 = "localhost1";
        String remoteHost2 = "localhost2";
        String remoteUser1 = "testUser1";
        String remoteUser2 = "testUser2";
        String protocol1 = "HTTP/1.1";
        String protocol2 = "HTTP/1.2";
        String method1 = "GET";
        String method2 = "POST";
        String serverName1 = "testServer1.com";
        String serverName2 = "testServer2.com";
        String remoteAddress1 = "127.0.0.1";
        String remoteAddress2 = "127.0.0.2";
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        Map<String, String[]> requestParameters1 = new HashMap<>();
        Map<String, String[]> requestParameters2 = new HashMap<>();
        int localPort1 = 8080;
        int localPort2 = 8081;
        int statusCode1 = 200;
        int statusCode2 = 404;

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

        // Test equals with same object
        assertTrue(accessEvent1.equals(accessEvent1));

        // Test equals with different objects but equal values
        accessEvent1.setTimeStamp(timeStamp2);
        accessEvent1.setLoggerContext(loggerContext2);
        accessEvent1.setRequestURI(requestURI2);
        accessEvent1.setRequestURL(requestURL2);
        accessEvent1.setRemoteHost(remoteHost2);
        accessEvent1.setRemoteUser(remoteUser2);
        accessEvent1.setProtocol(protocol2);
        accessEvent1.setMethod(method2);
        accessEvent1.setServerName(serverName2);
        accessEvent1.setRemoteAddress(remoteAddress2);
        accessEvent1.setRequestHeaders(requestHeaders2);
        accessEvent1.setResponseHeaders(responseHeaders2);
        accessEvent1.setRequestParameters(requestParameters2);
        accessEvent1.setLocalPort(localPort2);
        accessEvent1.setStatusCode(statusCode2);
        assertTrue(accessEvent1.equals(accessEvent2));
        assertTrue(accessEvent2.equals(accessEvent1));

        // Test equals with different objects and different values
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
        assertFalse(accessEvent1.equals(accessEvent2));
        assertFalse(accessEvent2.equals(accessEvent1));

        // Test hashCode with same objects
        int expectedHashCode1 = 41 * hashCode("accessEvent1") + 31 * hashCode(timeStamp1) + 31 * hashCode(loggerContext1)
                + 31 * hashCode(requestURI1) + 31 * hashCode(requestURL1) + 31 * hashCode(remoteHost1)
                + 31 * hashCode(remoteUser1) + 31 * hashCode(protocol1) + 31 * hashCode(method1)
                + 31 * hashCode(serverName1) + 31 * hashCode(remoteAddress1) + 31 * localPort1 + 31 * statusCode1;
        int expectedHashCode2 = 41 * hashCode("accessEvent2") + 31 * hashCode(timeStamp2) + 31 * hashCode(loggerContext2)
                + 31 * hashCode(requestURI2) + 31 * hashCode(requestURL2) + 31 * hashCode(remoteHost2)
                + 31 * hashCode(remoteUser2) + 31 * hashCode(protocol2) + 31 * hashCode(method2)
                + 31 * hashCode(serverName2) + 31 * hashCode(remoteAddress2) + 31 * localPort2 + 31 * statusCode2;
        assertEquals(expectedHashCode1, accessEvent1.hashCode());
        assertEquals(expectedHashCode2, accessEvent2.hashCode());

        // Test hashCode with different objects but equal values
        accessEvent1.setTimeStamp(timeStamp2);
        accessEvent1.setLoggerContext(loggerContext2);
        accessEvent1.setRequestURI(requestURI2);
        accessEvent1.setRequestURL(requestURL2);
        accessEvent1.setRemoteHost(remoteHost2);
        accessEvent1.setRemoteUser(remoteUser2);
        accessEvent1.setProtocol(protocol2);
        accessEvent1.setMethod(method2);
        accessEvent1.setServerName(serverName2);
        accessEvent1.setRemoteAddress(remoteAddress2);
        accessEvent1.setRequestHeaders(requestHeaders2);
        accessEvent1.setResponseHeaders(responseHeaders2);
        accessEvent1.setRequestParameters(requestParameters2);
        accessEvent1.setLocalPort(localPort2);
        accessEvent1.setStatusCode(statusCode2);
        int actualHashCode1 = accessEvent1.hashCode();
        int actualHashCode2 = accessEvent2.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);
        assertEquals(expectedHashCode2, actualHashCode2);

        // Test hashCode with different objects and different values
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
        int actualHashCode3 = accessEvent1.hashCode();
        int actualHashCode4 = accessEvent2.hashCode();
        assertNotEquals(expectedHashCode1, actualHashCode3);
        assertNotEquals(expectedHashCode1, actualHashCode4);
    }

    private int hashCode(Object obj) {
        if (obj == null) return 0;
        return obj.hashCode();
    }

    private int hashCode(String str) {
        if (str == null) return 0;
        int hash = 7;
        for (int i = 0; i < str.length(); i++) {
            hash = 31 * hash + str.charAt(i);
        }
        return hash;
    }

    private static int hashCode(Object obj) {
        if (obj == null) return 0;
        return obj.hashCode();
    }
}