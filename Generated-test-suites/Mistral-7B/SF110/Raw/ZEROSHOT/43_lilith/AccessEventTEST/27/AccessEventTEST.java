package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testAccessEvent_AllFields() {
        AccessEvent accessEvent = new AccessEvent();

        // Test setting and getting fields
        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());

        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());

        String requestURI = "/test";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());

        String requestURL = "http://test.com/test";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());

        String remoteHost = "127.0.0.1";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());

        String remoteUser = "testUser";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());

        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());

        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());

        String serverName = "localhost";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());

        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());

        Map<String, String> requestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());

        Map<String, String[]> requestParameters = new HashMap<>();
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());

        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());

        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testAccessEvent_Equals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        // Test equality with same object
        assertEquals(accessEvent1, accessEvent1);

        // Test equality with null object
        assertNotEquals(accessEvent1, null);

        // Test equality with different objects having the same fields
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(1L);
        assertEquals(accessEvent1, accessEvent2);

        // Test inequality with different objects having different fields
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(2L);
        assertNotEquals(accessEvent1, accessEvent2);

        // Test inequality with objects of different classes
        assertNotEquals(accessEvent1, new Object());
    }

    @Test
    public void testAccessEvent_HashCode() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        // Test same object having the same hashcode
        accessEvent1 = accessEvent1;
        int hashcode1 = accessEvent1.hashCode();
        assertEquals(hashcode1, accessEvent1.hashCode());

        // Test different objects having different hashcodes
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(2L);
        int hashcode1 = accessEvent1.hashCode();
        int hashcode2 = accessEvent2.hashCode();
        assertNotEquals(hashcode1, hashcode2);
    }
}