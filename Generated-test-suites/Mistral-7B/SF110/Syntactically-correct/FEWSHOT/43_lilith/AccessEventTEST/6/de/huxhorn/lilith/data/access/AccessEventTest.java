package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import org.junit.Test;

public class AccessEventTest {

    // Test for getters and setters of timeStamp
    @Test
    public void testTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    // Test for getters and setters of loggerContext
    @Test
    public void testLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    // Test for getters and setters of requestURI
    @Test
    public void testRequestURI() {
        AccessEvent event = new AccessEvent();
        String requestURI = "testRequestURI";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    // Test for getters and setters of requestURL
    @Test
    public void testRequestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "testRequestURL";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    // Test for getters and setters of remoteHost
    @Test
    public void testRemoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "testRemoteHost";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    // Test for getters and setters of remoteUser
    @Test
    public void testRemoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "testRemoteUser";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    // Test for getters and setters of protocol
    @Test
    public void testProtocol() {
        AccessEvent event = new AccessEvent();
        String protocol = "testProtocol";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    // Test for getters and setters of method
    @Test
    public void testMethod() {
        AccessEvent event = new AccessEvent();
        String method = "testMethod";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    // Test for getters and setters of serverName
    @Test
    public void testServerName() {
        AccessEvent event = new AccessEvent();
        String serverName = "testServerName";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    // Test for getters and setters of remoteAddress
    @Test
    public void testRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "testRemoteAddress";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }

    // Test for getters and setters of requestHeaders
    @Test
    public void testRequestHeaders() {
        AccessEvent event = new AccessEvent();
        HashMap<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    // Test for getters and setters of responseHeaders
    @Test
    public void testResponseHeaders() {
        AccessEvent event = new AccessEvent();
        HashMap<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    // Test for getters and setters of requestParameters
    @Test
    public void testRequestParameters() {
        AccessEvent event = new AccessEvent();
        HashMap<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }

    // Test for getters and setters of localPort
    @Test
    public void testLocalPort() {
        AccessEvent event = new AccessEvent();
        int localPort = 1234;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    // Test for getters and setters of statusCode
    @Test
    public void testStatusCode() {
        AccessEvent event = new AccessEvent();
        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    // Test for equals method
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("testRequestURI1");
        event1.setRequestURL("testRequestURL1");
        event1.setRemoteHost("testRemoteHost1");
        event1.setRemoteUser("testRemoteUser1");
        event1.setProtocol("testProtocol1");
        event1.setMethod("testMethod1");
        event1.setServerName("testServerName1");
        event1.setRemoteAddress("testRemoteAddress1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(1234);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("testRequestURI1");
        event2.setRequestURL("testRequestURL1");
        event2.setRemoteHost("testRemoteHost1");
        event2.setRemoteUser("testRemoteUser1");
        event2.setProtocol("testProtocol1");
        event2.setMethod("testMethod1");
        event2.setServerName("testServerName1");
        event2.setRemoteAddress("testRemoteAddress1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(1234);
        event2.setStatusCode(200);

        assertEquals(event1, event2);

        event2.setTimeStamp(null);
        assertNotEquals(event1, event2);

        event2.setLoggerContext(new LoggerContext());
        assertNotEquals(event1, event2);

        event2.setRemoteHost("testRemoteHost2");
        assertNotEquals(event1, event2);

        event2.setRemoteUser("testRemoteUser2");
        assertNotEquals(event1, event2);

        event2.setProtocol("testProtocol2");
        assertNotEquals(event1, event2);

        event2.setMethod("testMethod2");
        assertNotEquals(event1, event2);

        event2.setServerName("testServerName2");
        assertNotEquals(event1, event2);

        event2.setRemoteAddress("testRemoteAddress2");
        assertNotEquals(event1, event2);

        event2.setRequestHeaders(new HashMap<String, String>());
        assertNotEquals(event1, event2);

        event2.setResponseHeaders(new HashMap<String, String>());
        assertNotEquals(event1, event2);

        event2.setRequestParameters(new HashMap<String, String[]>());
        assertNotEquals(event1, event2);

        event2.setLocalPort(1235);
        assertNotEquals(event1, event2);

        event2.setStatusCode(201);
        assertNotEquals(event1, event2);
    }

    // Test for hashCode method
    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("testRequestURI1");
        event1.setRequestURL("testRequestURL1");
        event1.setRemoteHost("testRemoteHost1");
        event1.setRemoteUser("testRemoteUser1");
        event1.setProtocol("testProtocol1");
        event1.setMethod("testMethod1");
        event1.setServerName("testServerName1");
        event1.setRemoteAddress("testRemoteAddress1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(1234);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("testRequestURI1");
        event2.setRequestURL("testRequestURL1");
        event2.setRemoteHost("testRemoteHost1");
        event2.setRemoteUser("testRemoteUser1");
        event2.setProtocol("testProtocol1");
        event2.setMethod("testMethod1");
        event2.setServerName("testServerName1");
        event2.setRemoteAddress("testRemoteAddress1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(1234);
        event2.setStatusCode(200);

        assertEquals(event1.hashCode(), event2.hashCode());

        event2.setTimeStamp(null);
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setLoggerContext(new LoggerContext());
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setRemoteHost("testRemoteHost2");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setRemoteUser("testRemoteUser2");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setProtocol("testProtocol2");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setMethod("testMethod2");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setServerName("testServerName2");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setRemoteAddress("testRemoteAddress2");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setRequestHeaders(new HashMap<String, String>());
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setResponseHeaders(new HashMap<String, String>());
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setRequestParameters(new HashMap<String, String[]>());
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setLocalPort(1235);
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setStatusCode(201);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    // Test for toString method
    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        String expectedToString = "AccessEvent[loggerContext=null, timeStamp=null]";
        assertEquals(expectedToString, event.toString());

        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        expectedToString = "AccessEvent[loggerContext=LoggerContext[name=null, level=null], timeStamp=1]";
        assertEquals(expectedToString, event.toString());
    }
}