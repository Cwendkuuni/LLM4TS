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
        String requestURI = "testURI";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "testURL";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "testHost";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "testUser";
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
}