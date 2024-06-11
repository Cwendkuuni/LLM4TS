package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.*;

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
        HashMap<String, String> requestHeaders = new HashMap<>();
        AccessEvent accessEvent = new AccessEvent();

        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        HashMap<String, String> responseHeaders = new HashMap<>();
        AccessEvent accessEvent = new AccessEvent();

        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        HashMap<String, String[]> requestParameters = new HashMap<>();
        AccessEvent accessEvent = new AccessEvent();

        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_localPort() {
        int localPort = 1234;
        AccessEvent accessEvent = new AccessEvent();

        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        int statusCode = 200;
        AccessEvent accessEvent = new AccessEvent();

        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        accessEvent1.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("requestURI1");
        accessEvent1.setRequestURL("requestURL1");
        accessEvent1.setRemoteHost("remoteHost1");
        accessEvent1.setRemoteUser("remoteUser1");
        accessEvent1.setProtocol("protocol1");
        accessEvent1.setMethod("method1");
        accessEvent1.setServerName("serverName1");
        accessEvent1.setRemoteAddress("remoteAddress1");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(1234);
        accessEvent1.setStatusCode(200);

        accessEvent2.setTimeStamp(1L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("requestURI1");
        accessEvent2.setRequestURL("requestURL1");
        accessEvent2.setRemoteHost("remoteHost1");
        accessEvent2.setRemoteUser("remoteUser1");
        accessEvent2.setProtocol("protocol1");
        accessEvent2.setMethod("method1");
        accessEvent2.setServerName("serverName1");
        accessEvent2.setRemoteAddress("remoteAddress1");
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent2.setLocalPort(1234);
        accessEvent2.setStatusCode(200);

        assertTrue(accessEvent1.equals(accessEvent2));

        accessEvent2.setLocalPort(5678);

        assertFalse(accessEvent1.equals(accessEvent2));

        accessEvent1.setLocalPort(5678);

        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEvent = new AccessEvent();

        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);

        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);

        String requestURI = "requestURI";
        accessEvent.setRequestURI(requestURI);

        String requestURL = "requestURL";
        accessEvent.setRequestURL(requestURL);

        String remoteHost = "remoteHost";
        accessEvent.setRemoteHost(remoteHost);

        String remoteUser = "remoteUser";
        accessEvent.setRemoteUser(remoteUser);

        String protocol = "protocol";
        accessEvent.setProtocol(protocol);

        String method = "method";
        accessEvent.setMethod(method);

        String serverName = "serverName";
        accessEvent.setServerName(serverName);

        String remoteAddress = "remoteAddress";
        accessEvent.setRemoteAddress(remoteAddress);

        Map<String, String> requestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(requestHeaders);

        Map<String, String> responseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(responseHeaders);

        Map<String, String[]> requestParameters = new HashMap<>();
        accessEvent.setRequestParameters(requestParameters);

        int localPort = 1234;
        accessEvent.setLocalPort(localPort);

        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);

        long expectedHashCode = 4273257909635617687L; // calculate the hash code manually

        int actualHashCode = accessEvent.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }
}