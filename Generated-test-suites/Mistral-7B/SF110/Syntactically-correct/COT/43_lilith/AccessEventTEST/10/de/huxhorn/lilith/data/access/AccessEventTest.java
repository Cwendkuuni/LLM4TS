package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters_TimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);
        Long retrievedTimeStamp = event.getTimeStamp();
        assertEquals(timeStamp, retrievedTimeStamp);
    }

    @Test
    public void testGettersAndSetters_LoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        LoggerContext retrievedLoggerContext = event.getLoggerContext();
        assertEquals(loggerContext, retrievedLoggerContext);
    }

    @Test
    public void testGettersAndSetters_RequestURI() {
        AccessEvent event = new AccessEvent();
        String requestURI = "testURI";
        event.setRequestURI(requestURI);
        String retrievedRequestURI = event.getRequestURI();
        assertEquals(requestURI, retrievedRequestURI);
    }

    @Test
    public void testGettersAndSetters_RequestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "testURL";
        event.setRequestURL(requestURL);
        String retrievedRequestURL = event.getRequestURL();
        assertEquals(requestURL, retrievedRequestURL);
    }

    @Test
    public void testGettersAndSetters_RemoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "testRemoteHost";
        event.setRemoteHost(remoteHost);
        String retrievedRemoteHost = event.getRemoteHost();
        assertEquals(remoteHost, retrievedRemoteHost);
    }

    @Test
    public void testGettersAndSetters_RemoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "testRemoteUser";
        event.setRemoteUser(remoteUser);
        String retrievedRemoteUser = event.getRemoteUser();
        assertEquals(remoteUser, retrievedRemoteUser);
    }

    @Test
    public void testGettersAndSetters_Protocol() {
        AccessEvent event = new AccessEvent();
        String protocol = "testProtocol";
        event.setProtocol(protocol);
        String retrievedProtocol = event.getProtocol();
        assertEquals(protocol, retrievedProtocol);
    }

    @Test
    public void testGettersAndSetters_Method() {
        AccessEvent event = new AccessEvent();
        String method = "testMethod";
        event.setMethod(method);
        String retrievedMethod = event.getMethod();
        assertEquals(method, retrievedMethod);
    }

    @Test
    public void testGettersAndSetters_ServerName() {
        AccessEvent event = new AccessEvent();
        String serverName = "testServerName";
        event.setServerName(serverName);
        String retrievedServerName = event.getServerName();
        assertEquals(serverName, retrievedServerName);
    }

    @Test
    public void testGettersAndSetters_RemoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "testRemoteAddress";
        event.setRemoteAddress(remoteAddress);
        String retrievedRemoteAddress = event.getRemoteAddress();
        assertEquals(remoteAddress, retrievedRemoteAddress);
    }

    @Test
    public void testGettersAndSetters_RequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        Map<String, String> retrievedRequestHeaders = event.getRequestHeaders();
        assertEquals(requestHeaders, retrievedRequestHeaders);
    }

    @Test
    public void testGettersAndSetters_ResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        Map<String, String> retrievedResponseHeaders = event.getResponseHeaders();
        assertEquals(responseHeaders, retrievedResponseHeaders);
    }

    @Test
    public void testGettersAndSetters_RequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        Map<String, String[]> retrievedRequestParameters = event.getRequestParameters();
        assertEquals(requestParameters, retrievedRequestParameters);
    }

    @Test
    public void testGettersAndSetters_LocalPort() {
        AccessEvent event = new AccessEvent();
        int localPort = 1;
        event.setLocalPort(localPort);
        int retrievedLocalPort = event.getLocalPort();
        assertEquals(localPort, retrievedLocalPort);
    }

    @Test
    public void testGettersAndSetters_StatusCode() {
        AccessEvent event = new AccessEvent();
        int statusCode = 1;
        event.setStatusCode(statusCode);
        int retrievedStatusCode = event.getStatusCode();
        assertEquals(statusCode, retrievedStatusCode);
    }

    @Test(expected = NullPointerException.class)
    public void testEquals_NullObject() {
        AccessEvent event = new AccessEvent();
        event.equals(null);
    }

    @Test
    public void testEquals_DifferentObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event2.setTimeStamp(2L);
        assertFalse(event1.equals(event2));
    }

    @Test
    public void testEquals_SameObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode_SameObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_DifferentObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event2.setTimeStamp(2L);
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        event.setTimeStamp(1L);
        String expectedToString = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=1]";
        String actualToString = event.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testSettersWithNullValues() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(null);
        event.setTimeStamp(null);
        event.setRequestURI(null);
        event.setRequestURL(null);
        event.setRemoteHost(null);
        event.setRemoteUser(null);
        event.setProtocol(null);
        event.setMethod(null);
        event.setServerName(null);
        event.setRemoteAddress(null);
        event.setRequestHeaders(null);
        event.setResponseHeaders(null);
        event.setRequestParameters(null);
        event.setLocalPort(-1);
        event.setStatusCode(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithInvalidValues() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp("invalidTimeStamp");
    }

    @Test
    public void testEquals_SameObjectsWithNullFields() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setLoggerContext(null);
        event1.setTimeStamp(1L);
        event2.setLoggerContext(null);
        event2.setTimeStamp(1L);
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode_SameObjectsWithNullFields() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setLoggerContext(null);
        event1.setTimeStamp(1L);
        event2.setLoggerContext(null);
        event2.setTimeStamp(1L);
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

}