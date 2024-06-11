package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertLinesMatch;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters_timeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGettersAndSetters_loggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_requestURI() {
        String requestURI = "requestURI";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        String requestURL = "requestURL";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        String remoteHost = "remoteHost";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        String remoteUser = "remoteUser";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_protocol() {
        String protocol = "protocol";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGettersAndSetters_method() {
        String method = "method";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGettersAndSetters_serverName() {
        String serverName = "serverName";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        String remoteAddress = "remoteAddress";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_requestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_localPort() {
        int localPort = 1;
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        int statusCode = 1;
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals_sameObject() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;
        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_null() {
        AccessEvent accessEvent = new AccessEvent();
        assertFalse(accessEvent.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        AccessEvent accessEvent = new AccessEvent();
        Object other = new Object();
        assertFalse(accessEvent.equals(other));
    }

    @Test
    public void testEquals_differentLoggerContext() {
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(loggerContext1);
        assertFalse(accessEvent.equals(new AccessEvent().setLoggerContext(loggerContext2)));
    }

    @Test
    public void testEquals_differentMethod() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setMethod("GET");
        accessEvent2.setMethod("POST");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentProtocol() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent2.setProtocol("HTTP/1.0");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentRemoteAddress() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent2.setRemoteAddress("192.168.0.1");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentRemoteHost() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteHost("localhost");
        accessEvent2.setRemoteHost("localhost.localdomain");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentRemoteUser() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteUser("user1");
        accessEvent2.setRemoteUser("user2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentRequestHeaders() {
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Header1", "Value1");
        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("Header1", "Value2");
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setRequestHeaders(requestHeaders1);
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setRequestHeaders(requestHeaders2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentRequestURI() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURI("/api/resource1");
        accessEvent2.setRequestURI("/api/resource2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentRequestURL() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURL("http://example.com/api/resource1");
        accessEvent2.setRequestURL("http://example.com/api/resource2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentResponseHeaders() {
        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("Header1", "Value1");
        Map<String, String> responseHeaders2 = new HashMap<>();
        responseHeaders2.put("Header1", "Value2");
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setResponseHeaders(responseHeaders1);
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setResponseHeaders(responseHeaders2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentServerName() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setServerName("example.com");
        accessEvent2.setServerName("example.net");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentTimeStamp() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Long timeStamp1 = 1L;
        Long timeStamp2 = 2L;
        accessEvent1.setTimeStamp(timeStamp1);
        accessEvent2.setTimeStamp(timeStamp2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode_sameObjects() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;
        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_differentObjects() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_sameFields() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setTimeStamp(1L);

        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setTimeStamp(1L);

        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_differentFields() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setTimeStamp(1L);

        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setTimeStamp(2L);

        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        StringBuilder expected = new StringBuilder();
        expected.append("AccessEvent[loggerContext=");
        expected.append(new LoggerContext().toString());
        expected.append(", timeStamp=");
        expected.append(Long.toString(1L));
        expected.append("]");

        StringBuilder result = new StringBuilder();
        result.append(accessEvent.toString());

        assertLinesMatch(expected.toString().split(System.lineSeparator()), result.toString().split(System.lineSeparator()));
    }
}