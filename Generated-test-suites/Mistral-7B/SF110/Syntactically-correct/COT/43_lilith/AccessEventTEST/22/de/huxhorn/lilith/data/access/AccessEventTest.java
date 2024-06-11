package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;
import java.util.*;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters_TimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGettersAndSetters_LoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_RequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURI = "testURI";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_RequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURL = "testURL";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_RemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteHost = "testRemoteHost";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_RemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteUser = "testRemoteUser";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_Protocol() {
        AccessEvent accessEvent = new AccessEvent();
        String protocol = "testProtocol";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGettersAndSetters_Method() {
        AccessEvent accessEvent = new AccessEvent();
        String method = "testMethod";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGettersAndSetters_ServerName() {
        AccessEvent accessEvent = new AccessEvent();
        String serverName = "testServerName";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGettersAndSetters_RemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteAddress = "testRemoteAddress";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_RequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> testHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(testHeaders);
        assertEquals(testHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_ResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> testHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(testHeaders);
        assertEquals(testHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_RequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String[]> testParameters = new HashMap<>();
        accessEvent.setRequestParameters(testParameters);
        assertEquals(testParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_LocalPort() {
        AccessEvent accessEvent = new AccessEvent();
        int localPort = 1;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_StatusCode() {
        AccessEvent accessEvent = new AccessEvent();
        int statusCode = 1;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullParameters() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullRequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURI(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullRequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURL(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullRemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteHost(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullRemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteUser(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullProtocol() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setProtocol(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullMethod() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setMethod(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullServerName() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setServerName(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullRemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteAddress(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullRequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestHeaders(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setResponseHeaders(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullRequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestParameters(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetters_InvalidTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetters_InvalidLocalPort() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLocalPort(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetters_InvalidStatusCode() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setStatusCode(-1);
    }

    @Test
    public void testEquals_SameObject() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;
        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_SameFields() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setLocalPort(1);
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setLocalPort(1);
        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentFields() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setLocalPort(1);
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setLocalPort(2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode_SameObject() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;
        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_SameFields() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setLocalPort(1);
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setLocalPort(1);
        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentFields() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setLocalPort(1);
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setLocalPort(2);
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }
}