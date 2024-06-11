package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters_timeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);
        Long retrievedTimeStamp = accessEvent.getTimeStamp();
        assertEquals(timeStamp, retrievedTimeStamp);
    }

    @Test
    public void testGettersAndSetters_loggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        LoggerContext retrievedLoggerContext = accessEvent.getLoggerContext();
        assertEquals(loggerContext, retrievedLoggerContext);
    }

    @Test
    public void testGettersAndSetters_requestURI() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURI = "testURI";
        accessEvent.setRequestURI(requestURI);
        String retrievedRequestURI = accessEvent.getRequestURI();
        assertEquals(requestURI, retrievedRequestURI);
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURL = "testURL";
        accessEvent.setRequestURL(requestURL);
        String retrievedRequestURL = accessEvent.getRequestURL();
        assertEquals(requestURL, retrievedRequestURL);
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteHost = "testHost";
        accessEvent.setRemoteHost(remoteHost);
        String retrievedRemoteHost = accessEvent.getRemoteHost();
        assertEquals(remoteHost, retrievedRemoteHost);
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteUser = "testUser";
        accessEvent.setRemoteUser(remoteUser);
        String retrievedRemoteUser = accessEvent.getRemoteUser();
        assertEquals(remoteUser, retrievedRemoteUser);
    }

    @Test
    public void testGettersAndSetters_protocol() {
        AccessEvent accessEvent = new AccessEvent();
        String protocol = "testProtocol";
        accessEvent.setProtocol(protocol);
        String retrievedProtocol = accessEvent.getProtocol();
        assertEquals(protocol, retrievedProtocol);
    }

    @Test
    public void testGettersAndSetters_method() {
        AccessEvent accessEvent = new AccessEvent();
        String method = "testMethod";
        accessEvent.setMethod(method);
        String retrievedMethod = accessEvent.getMethod();
        assertEquals(method, retrievedMethod);
    }

    @Test
    public void testGettersAndSetters_serverName() {
        AccessEvent accessEvent = new AccessEvent();
        String serverName = "testServerName";
        accessEvent.setServerName(serverName);
        String retrievedServerName = accessEvent.getServerName();
        assertEquals(serverName, retrievedServerName);
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteAddress = "testRemoteAddress";
        accessEvent.setRemoteAddress(remoteAddress);
        String retrievedRemoteAddress = accessEvent.getRemoteAddress();
        assertEquals(remoteAddress, retrievedRemoteAddress);
    }

    @Test
    public void testGettersAndSetters_requestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(requestHeaders);
        Map<String, String> retrievedRequestHeaders = accessEvent.getRequestHeaders();
        assertEquals(requestHeaders, retrievedRequestHeaders);
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(responseHeaders);
        Map<String, String> retrievedResponseHeaders = accessEvent.getResponseHeaders();
        assertEquals(responseHeaders, retrievedResponseHeaders);
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        accessEvent.setRequestParameters(requestParameters);
        Map<String, String[]> retrievedRequestParameters = accessEvent.getRequestParameters();
        assertEquals(requestParameters, retrievedRequestParameters);
    }

    @Test
    public void testGettersAndSetters_localPort() {
        AccessEvent accessEvent = new AccessEvent();
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        int retrievedLocalPort = accessEvent.getLocalPort();
        assertEquals(localPort, retrievedLocalPort);
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        AccessEvent accessEvent = new AccessEvent();
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        int retrievedStatusCode = accessEvent.getStatusCode();
        assertEquals(statusCode, retrievedStatusCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeStampWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLoggerContextWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRequestURIWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURI(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRequestURLWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURL(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRemoteHostWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteHost(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRemoteUserWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProtocolWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setProtocol(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setMethod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetServerNameWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setServerName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRemoteAddressWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteAddress(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRequestHeadersWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestHeaders(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetResponseHeadersWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setResponseHeaders(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRequestParametersWithNull() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestParameters(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLocalPortWithNegativeValue() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLocalPort(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStatusCodeWithNegativeValue() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setStatusCode(-1);
    }
}