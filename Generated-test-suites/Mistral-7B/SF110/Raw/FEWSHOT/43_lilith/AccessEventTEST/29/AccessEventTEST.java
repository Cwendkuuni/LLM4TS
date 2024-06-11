package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

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
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_requestURI() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURI = "requestURI";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURL = "requestURL";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteHost = "remoteHost";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteUser = "remoteUser";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_protocol() {
        AccessEvent accessEvent = new AccessEvent();
        String protocol = "protocol";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGettersAndSetters_method() {
        AccessEvent accessEvent = new AccessEvent();
        String method = "method";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGettersAndSetters_serverName() {
        AccessEvent accessEvent = new AccessEvent();
        String serverName = "serverName";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteAddress = "remoteAddress";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_requestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_localPort() {
        AccessEvent accessEvent = new AccessEvent();
        int localPort = 1;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        AccessEvent accessEvent = new AccessEvent();
        int statusCode = 1;
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
    public void testEquals_sameValues() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setRequestURI("requestURI");
        accessEvent1.setRequestURL("requestURL");
        accessEvent1.setRemoteHost("remoteHost");
        accessEvent1.setRemoteUser("remoteUser");
        accessEvent1.setProtocol("protocol");
        accessEvent1.setMethod("method");
        accessEvent1.setServerName("serverName");
        accessEvent1.setRemoteAddress("remoteAddress");
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String[]> requestParameters1 = new HashMap<>();
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent1.setRequestParameters(requestParameters1);
        accessEvent1.setLocalPort(1);
        accessEvent1.setStatusCode(1);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setTimeStamp(1L);
        accessEvent2.setRequestURI("requestURI");
        accessEvent2.setRequestURL("requestURL");
        accessEvent2.setRemoteHost("remoteHost");
        accessEvent2.setRemoteUser("remoteUser");
        accessEvent2.setProtocol("protocol");
        accessEvent2.setMethod("method");
        accessEvent2.setServerName("serverName");
        accessEvent2.setRemoteAddress("remoteAddress");
        Map<String, String> requestHeaders2 = requestHeaders1;
        Map<String, String> responseHeaders2 = responseHeaders1;
        Map<String, String[]> requestParameters2 = requestParameters1;
        accessEvent2.setRequestHeaders(requestHeaders2);
        accessEvent2.setResponseHeaders(responseHeaders2);
        accessEvent2.setRequestParameters(requestParameters2);
        accessEvent2.setLocalPort(1);
        accessEvent2.setStatusCode(1);
        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_differentObjects() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setRequestURI("requestURI1");
        accessEvent1.setRequestURL("requestURL1");
        accessEvent1.setRemoteHost("remoteHost1");
        accessEvent1.setRemoteUser("remoteUser1");
        accessEvent1.setProtocol("protocol1");
        accessEvent1.setMethod("method1");
        accessEvent1.setServerName("serverName1");
        accessEvent1.setRemoteAddress("remoteAddress1");
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String[]> requestParameters1 = new HashMap<>();
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent1.setRequestParameters(requestParameters1);
        accessEvent1.setLocalPort(1);
        accessEvent1.setStatusCode(1);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setTimeStamp(1L);
        accessEvent2.setRequestURI("requestURI2");
        accessEvent2.setRequestURL("requestURL2");
        accessEvent2.setRemoteHost("remoteHost2");
        accessEvent2.setRemoteUser("remoteUser2");
        accessEvent2.setProtocol("protocol2");
        accessEvent2.setMethod("method2");
        accessEvent2.setServerName("serverName2");
        accessEvent2.setRemoteAddress("remoteAddress2");
        Map<String, String> requestHeaders2 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        Map<String, String[]> requestParameters2 = new HashMap<>();
        accessEvent2.setRequestHeaders(requestHeaders2);
        accessEvent2.setResponseHeaders(responseHeaders2);
        accessEvent2.setRequestParameters(requestParameters2);
        accessEvent2.setLocalPort(2);
        accessEvent2.setStatusCode(2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode_sameObject() {
        AccessEvent accessEvent1 = new AccessEvent();
        int hashCode1 = accessEvent1.hashCode();
        AccessEvent accessEvent2 = accessEvent1;
        int hashCode2 = accessEvent2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_sameValues() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setRequestURI("requestURI");
        accessEvent1.setRequestURL("requestURL");
        accessEvent1.setRemoteHost("remoteHost");
        accessEvent1.setRemoteUser("remoteUser");
        accessEvent1.setProtocol("protocol");
        accessEvent1.setMethod("method");
        accessEvent1.setServerName("serverName");
        accessEvent1.setRemoteAddress("remoteAddress");
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String[]> requestParameters1 = new HashMap<>();
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent1.setRequestParameters(requestParameters1);
        accessEvent1.setLocalPort(1);
        accessEvent1.setStatusCode(1);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setTimeStamp(1L);
        accessEvent2.setRequestURI("requestURI");
        accessEvent2.setRequestURL("requestURL");
        accessEvent2.setRemoteHost("remoteHost");
        accessEvent2.setRemoteUser("remoteUser");
        accessEvent2.setProtocol("protocol");
        accessEvent2.setMethod("method");
        accessEvent2.setServerName("serverName");
        accessEvent2.setRemoteAddress("remoteAddress");
        Map<String, String> requestHeaders2 = requestHeaders1;
        Map<String, String> responseHeaders2 = responseHeaders1;
        Map<String, String[]> requestParameters2 = requestParameters1;
        accessEvent2.setRequestHeaders(requestHeaders2);
        accessEvent2.setResponseHeaders(responseHeaders2);
        accessEvent2.setRequestParameters(requestParameters2);
        accessEvent2.setLocalPort(1);
        accessEvent2.setStatusCode(1);
        int hashCode1 = accessEvent1.hashCode();
        int hashCode2 = accessEvent2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        StringBuilder toString = new StringBuilder();
        accessEvent.toString().toString().equals(toString.append("AccessEvent[").append("loggerContext=").append(accessEvent.getLoggerContext()).append(", ")
                .append("timeStamp=").append(accessEvent.getTimeStamp()).append("]").toString());
    }
}