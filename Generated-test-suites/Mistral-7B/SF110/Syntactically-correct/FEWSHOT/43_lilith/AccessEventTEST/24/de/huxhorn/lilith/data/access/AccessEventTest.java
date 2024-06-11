package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters_timeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    @Test
    public void testGettersAndSetters_loggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_requestURI() {
        String requestURI = "testURI";
        AccessEvent event = new AccessEvent();
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        String requestURL = "testURL";
        AccessEvent event = new AccessEvent();
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        String remoteHost = "testHost";
        AccessEvent event = new AccessEvent();
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        String remoteUser = "testUser";
        AccessEvent event = new AccessEvent();
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_protocol() {
        String protocol = "testProtocol";
        AccessEvent event = new AccessEvent();
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    @Test
    public void testGettersAndSetters_method() {
        String method = "testMethod";
        AccessEvent event = new AccessEvent();
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    @Test
    public void testGettersAndSetters_serverName() {
        String serverName = "testServerName";
        AccessEvent event = new AccessEvent();
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        String remoteAddress = "testRemoteAddress";
        AccessEvent event = new AccessEvent();
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_requestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        AccessEvent event = new AccessEvent();
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        AccessEvent event = new AccessEvent();
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_localPort() {
        int localPort = 1234;
        AccessEvent event = new AccessEvent();
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        int statusCode = 200;
        AccessEvent event = new AccessEvent();
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testEquals_sameObject() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = event1;
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testEquals_differentObjects_sameValues() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("testURI");
        event1.setRequestURL("testURL");
        event1.setRemoteHost("testHost");
        event1.setRemoteUser("testUser");
        event1.setProtocol("testProtocol");
        event1.setMethod("testMethod");
        event1.setServerName("testServerName");
        event1.setRemoteAddress("testRemoteAddress");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(1234);
        event1.setStatusCode(200);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("testURI");
        event2.setRequestURL("testURL");
        event2.setRemoteHost("testHost");
        event2.setRemoteUser("testUser");
        event2.setProtocol("testProtocol");
        event2.setMethod("testMethod");
        event2.setServerName("testServerName");
        event2.setRemoteAddress("testRemoteAddress");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(1234);
        event2.setStatusCode(200);

        assertTrue(event1.equals(event2));
    }

    @Test
    public void testEquals_differentObjects_differentValues() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("testURI");
        event1.setRequestURL("testURL");
        event1.setRemoteHost("testHost");
        event1.setRemoteUser("testUser");
        event1.setProtocol("testProtocol");
        event1.setMethod("testMethod");
        event1.setServerName("testServerName");
        event1.setRemoteAddress("testRemoteAddress");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(1234);
        event1.setStatusCode(200);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(2L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("testURI2");
        event2.setRequestURL("testURL2");
        event2.setRemoteHost("testHost2");
        event2.setRemoteUser("testUser2");
        event2.setProtocol("testProtocol2");
        event2.setMethod("testMethod2");
        event2.setServerName("testServerName2");
        event2.setRemoteAddress("testRemoteAddress2");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(1235);
        event2.setStatusCode(201);

        assertFalse(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        String requestURI = "testURI";
        event.setRequestURI(requestURI);
        String requestURL = "testURL";
        event.setRequestURL(requestURL);
        String remoteHost = "testHost";
        event.setRemoteHost(remoteHost);
        String remoteUser = "testUser";
        event.setRemoteUser(remoteUser);
        String protocol = "testProtocol";
        event.setProtocol(protocol);
        String method = "testMethod";
        event.setMethod(method);
        String serverName = "testServerName";
        event.setServerName(serverName);
        String remoteAddress = "testRemoteAddress";
        event.setRemoteAddress(remoteAddress);
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        int localPort = 1234;
        event.setLocalPort(localPort);
        int statusCode = 200;
        event.setStatusCode(statusCode);

        int expectedHashCode = 61 * (31 * (31 * (timeStamp.hashCode() + loggerContext.hashCode())) + requestURI.hashCode())
                + requestURL.hashCode() + remoteHost.hashCode() + remoteUser.hashCode() + protocol.hashCode()
                + method.hashCode() + serverName.hashCode() + remoteAddress.hashCode() + localPort + statusCode;

        int actualHashCode = event.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        String requestURI = "testURI";
        event.setRequestURI(requestURI);
        String requestURL = "testURL";
        event.setRequestURL(requestURL);
        String remoteHost = "testHost";
        event.setRemoteHost(remoteHost);
        String remoteUser = "testUser";
        event.setRemoteUser(remoteUser);
        String protocol = "testProtocol";
        event.setProtocol(protocol);
        String method = "testMethod";
        event.setMethod(method);
        String serverName = "testServerName";
        event.setServerName(serverName);
        String remoteAddress = "testRemoteAddress";
        event.setRemoteAddress(remoteAddress);
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        int localPort = 1234;
        event.setLocalPort(localPort);
        int statusCode = 200;
        event.setStatusCode(statusCode);

        String expectedToString = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=" + timeStamp + "]";

        String actualToString = event.toString();

        assertEquals(expectedToString, actualToString);
    }
}