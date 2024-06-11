package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testAccessEventWithValidData() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(1L);
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setRequestURI("/api/v1/users");
        accessEvent.setRequestURL("http://localhost:8080/api/v1/users");
        accessEvent.setRemoteHost("127.0.0.1");
        accessEvent.setRemoteUser("testUser");
        accessEvent.setProtocol("HTTP/1.1");
        accessEvent.setMethod("GET");
        accessEvent.setServerName("localhost");
        accessEvent.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEvent.setRequestHeaders(requestHeaders);
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEvent.setResponseHeaders(responseHeaders);
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] paramValue1 = {"John", "Doe"};
        requestParameters.put("username", paramValue1);
        accessEvent.setRequestParameters(requestParameters);
        accessEvent.setLocalPort(8080);
        accessEvent.setStatusCode(200);

        //Test getters
        assertEquals(1L, accessEvent.getTimeStamp());
        assertNotNull(accessEvent.getLoggerContext());
        assertEquals("/api/v1/users", accessEvent.getRequestURI());
        assertEquals("http://localhost:8080/api/v1/users", accessEvent.getRequestURL());
        assertEquals("127.0.0.1", accessEvent.getRemoteHost());
        assertEquals("testUser", accessEvent.getRemoteUser());
        assertEquals("HTTP/1.1", accessEvent.getProtocol());
        assertEquals("GET", accessEvent.getMethod());
        assertEquals("localhost", accessEvent.getServerName());
        assertEquals("127.0.0.1", accessEvent.getRemoteAddress());
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
        assertEquals(requestParameters, accessEvent.getRequestParameters());
        assertEquals(8080, accessEvent.getLocalPort());
        assertEquals(200, accessEvent.getStatusCode());
    }

    @Test
    public void testAccessEventWithEmptyData() {
        AccessEvent accessEvent = new AccessEvent();

        //Test getters with null or empty values
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
        assertNull(accessEvent.getLocalPort());
        assertNull(accessEvent.getStatusCode());
    }

    @Test
    public void testAccessEventEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        //Test equals with different objects
        assertFalse(accessEvent1.equals(new Object()));

        //Test equals with same data
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/api/v1/users");
        accessEvent1.setRequestURL("http://localhost:8080/api/v1/users");
        accessEvent1.setRemoteHost("127.0.0.1");
        accessEvent1.setRemoteUser("testUser");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent1.setServerName("localhost");
        accessEvent1.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Content-Type", "application/json");
        accessEvent1.setRequestHeaders(requestHeaders1);
        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("Content-Type", "application/json");
        accessEvent1.setResponseHeaders(responseHeaders1);
        Map<String, String[]> requestParameters1 = new HashMap<>();
        String[] paramValue1 = {"John", "Doe"};
        requestParameters1.put("username", paramValue1);
        accessEvent1.setRequestParameters(requestParameters1);
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        accessEvent2.setTimeStamp(1L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("/api/v1/users");
        accessEvent2.setRequestURL("http://localhost:8080/api/v1/users");
        accessEvent2.setRemoteHost("127.0.0.1");
        accessEvent2.setRemoteUser("testUser");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent2.setMethod("GET");
        accessEvent2.setServerName("localhost");
        accessEvent2.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders2 = new HashMap<>(requestHeaders1);
        accessEvent2.setRequestHeaders(requestHeaders2);
        Map<String, String> responseHeaders2 = new HashMap<>(responseHeaders1);
        accessEvent2.setResponseHeaders(responseHeaders2);
        Map<String, String[]> requestParameters2 = new HashMap<>(requestParameters1);
        accessEvent2.setRequestParameters(requestParameters2);
        accessEvent2.setLocalPort(8080);
        accessEvent2.setStatusCode(200);

        //Test equals with same data
        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testAccessEventHashCode() {
        AccessEvent accessEvent = new AccessEvent();
        long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/api/v1/users";
        String requestURL = "http://localhost:8080/api/v1/users";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "localhost";
        String remoteAddress = "127.0.0.1";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] paramValue = {"John", "Doe"};
        requestParameters.put("username", paramValue);
        int localPort = 8080;
        int statusCode = 200;

        int hashCode = accessEvent.hashCode();
        long newTimeStamp = timeStamp * 2;

        accessEvent.setTimeStamp(newTimeStamp);
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

        //Test hashCode with different objects
        int differentHashCode = accessEvent.hashCode() ^ 1;

        //Test hashCode with same data
        AccessEvent accessEvent2 = new AccessEvent();
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

        //Test hashCode with same data
        assertEquals(hashCode, accessEvent2.hashCode());
    }
}