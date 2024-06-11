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
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_requestURI() {
        AccessEvent event = new AccessEvent();
        String requestURI = "testRequestURI";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "testRequestURL";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "testRemoteHost";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "testRemoteUser";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_protocol() {
        AccessEvent event = new AccessEvent();
        String protocol = "testProtocol";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    @Test
    public void testGettersAndSetters_method() {
        AccessEvent event = new AccessEvent();
        String method = "testMethod";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    @Test
    public void testGettersAndSetters_serverName() {
        AccessEvent event = new AccessEvent();
        String serverName = "testServerName";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "testRemoteAddress";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_requestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_localPort() {
        AccessEvent event = new AccessEvent();
        int localPort = 1234;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        AccessEvent event = new AccessEvent();
        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertFalse(event1.equals(null));
        assertFalse(event1.equals(new Object()));
        assertFalse(event1.equals(event2));

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("testRequestURI");
        event1.setRequestURL("testRequestURL");
        event1.setRemoteHost("testRemoteHost");
        event1.setRemoteUser("testRemoteUser");
        event1.setProtocol("testProtocol");
        event1.setMethod("testMethod");
        event1.setServerName("testServerName");
        event1.setRemoteAddress("testRemoteAddress");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(1234);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("testRequestURI");
        event2.setRequestURL("testRequestURL");
        event2.setRemoteHost("testRemoteHost");
        event2.setRemoteUser("testRemoteUser");
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
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "testRequestURI";
        String requestURL = "testRequestURL";
        String remoteHost = "testRemoteHost";
        String remoteUser = "testRemoteUser";
        String protocol = "testProtocol";
        String method = "testMethod";
        String serverName = "testServerName";
        String remoteAddress = "testRemoteAddress";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 1234;
        int statusCode = 200;

        int hashCode = event.hashCode();
        event.setTimeStamp(timeStamp);
        hashCode = 31 * hashCode + (timeStamp != null ? timeStamp.hashCode() : 0);
        event.setLoggerContext(loggerContext);
        hashCode = 31 * hashCode + (loggerContext != null ? loggerContext.hashCode() : 0);
        event.setRequestURI(requestURI);
        hashCode = 31 * hashCode + (requestURI != null ? requestURI.hashCode() : 0);
        event.setRequestURL(requestURL);
        hashCode = 31 * hashCode + (requestURL != null ? requestURL.hashCode() : 0);
        event.setRemoteHost(remoteHost);
        hashCode = 31 * hashCode + (remoteHost != null ? remoteHost.hashCode() : 0);
        event.setRemoteUser(remoteUser);
        hashCode = 31 * hashCode + (remoteUser != null ? remoteUser.hashCode() : 0);
        event.setProtocol(protocol);
        hashCode = 31 * hashCode + (protocol != null ? protocol.hashCode() : 0);
        event.setMethod(method);
        hashCode = 31 * hashCode + (method != null ? method.hashCode() : 0);
        event.setServerName(serverName);
        hashCode = 31 * hashCode + (serverName != null ? serverName.hashCode() : 0);
        event.setRemoteAddress(remoteAddress);
        hashCode = 31 * hashCode + (remoteAddress != null ? remoteAddress.hashCode() : 0);
        event.setLocalPort(localPort);
        hashCode = 31 * hashCode + localPort;
        event.setStatusCode(statusCode);
        hashCode = 31 * hashCode + statusCode;

        assertEquals(hashCode, event.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "testRequestURI";
        String requestURL = "testRequestURL";
        String remoteHost = "testRemoteHost";
        String remoteUser = "testRemoteUser";
        String protocol = "testProtocol";
        String method = "testMethod";
        String serverName = "testServerName";
        String remoteAddress = "testRemoteAddress";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 1234;
        int statusCode = 200;

        event.setTimeStamp(timeStamp);
        event.setLoggerContext(loggerContext);
        event.setRequestURI(requestURI);
        event.setRequestURL(requestURL);
        event.setRemoteHost(remoteHost);
        event.setRemoteUser(remoteUser);
        event.setProtocol(protocol);
        event.setMethod(method);
        event.setServerName(serverName);
        event.setRemoteAddress(remoteAddress);
        event.setRequestHeaders(requestHeaders);
        event.setResponseHeaders(responseHeaders);
        event.setRequestParameters(requestParameters);
        event.setLocalPort(localPort);
        event.setStatusCode(statusCode);

        String expected = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=" + timeStamp + "]";
        String actual = event.toString();

        assertEquals(expected, actual);
    }
}