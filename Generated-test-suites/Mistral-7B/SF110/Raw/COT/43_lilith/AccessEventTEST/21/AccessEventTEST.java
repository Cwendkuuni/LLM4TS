package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters_TimeStamp() {
        Long timeStamp = 1L;
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    @Test
    public void testGettersAndSetters_LoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_RequestURI() {
        String requestURI = "testURI";
        AccessEvent event = new AccessEvent();
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_RequestURL() {
        String requestURL = "testURL";
        AccessEvent event = new AccessEvent();
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_RemoteHost() {
        String remoteHost = "testHost";
        AccessEvent event = new AccessEvent();
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_RemoteUser() {
        String remoteUser = "testUser";
        AccessEvent event = new AccessEvent();
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_Protocol() {
        String protocol = "testProtocol";
        AccessEvent event = new AccessEvent();
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    @Test
    public void testGettersAndSetters_Method() {
        String method = "testMethod";
        AccessEvent event = new AccessEvent();
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    @Test
    public void testGettersAndSetters_ServerName() {
        String serverName = "testServer";
        AccessEvent event = new AccessEvent();
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testGettersAndSetters_RemoteAddress() {
        String remoteAddress = "testAddress";
        AccessEvent event = new AccessEvent();
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_RequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        AccessEvent event = new AccessEvent();
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_ResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        AccessEvent event = new AccessEvent();
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_RequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_LocalPort() {
        int localPort = 1;
        AccessEvent event = new AccessEvent();
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_StatusCode() {
        int statusCode = 1;
        AccessEvent event = new AccessEvent();
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullLoggerContext() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullRequestHeaders() {
        AccessEvent event = new AccessEvent();
        event.setRequestHeaders(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullResponseHeaders() {
        AccessEvent event = new AccessEvent();
        event.setResponseHeaders(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullRequestParameters() {
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(null);
    }

    @Test
    public void testEquals_SameObject() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = event1;
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testEquals_SameValues() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("testURI");
        event1.setRequestURL("testURL");
        event1.setRemoteHost("testHost");
        event1.setRemoteUser("testUser");
        event1.setProtocol("testProtocol");
        event1.setMethod("testMethod");
        event1.setServerName("testServer");
        event1.setRemoteAddress("testAddress");
        event1.setRequestHeaders(new HashMap<String, String>());
        event1.setResponseHeaders(new HashMap<String, String>());
        event1.setRequestParameters(new HashMap<String, String[]>());
        event1.setLocalPort(1);
        event1.setStatusCode(1);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("testURI");
        event2.setRequestURL("testURL");
        event2.setRemoteHost("testHost");
        event2.setRemoteUser("testUser");
        event2.setProtocol("testProtocol");
        event2.setMethod("testMethod");
        event2.setServerName("testServer");
        event2.setRemoteAddress("testAddress");
        event2.setRequestHeaders(new HashMap<String, String>());
        event2.setResponseHeaders(new HashMap<String, String>());
        event2.setRequestParameters(new HashMap<String, String[]>());
        event2.setLocalPort(1);
        event2.setStatusCode(1);

        assertTrue(event1.equals(event2));
    }

    @Test
    public void testEquals_DifferentObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertFalse(event1.equals(event2));
    }

    @Test
    public void testEquals_DifferentLoggerContext() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        event1.setRequestURI("testURI");
        event2.setRequestURI("testURI");
        event1.setRequestURL("testURL");
        event2.setRequestURL("testURL");
        event1.setRemoteHost("testHost");
        event2.setRemoteHost("testHost");
        event1.setRemoteUser("testUser");
        event2.setRemoteUser("testUser");
        event1.setProtocol("testProtocol");
        event2.setProtocol("testProtocol");
        event1.setMethod("testMethod");
        event2.setMethod("testMethod");
        event1.setServerName("testServer");
        event2.setServerName("testServer");
        event1.setRemoteAddress("testAddress");
        event2.setRemoteAddress("testAddress");
        event1.setRequestHeaders(new HashMap<String, String>());
        event2.setRequestHeaders(new HashMap<String, String>());
        event1.setResponseHeaders(new HashMap<String, String>());
        event2.setResponseHeaders(new HashMap<String, String>());
        event1.setRequestParameters(new HashMap<String, String[]>());
        event2.setRequestParameters(new HashMap<String, String[]>());
        event1.setLocalPort(1);
        event2.setLocalPort(2);
        event1.setStatusCode(1);
        event2.setStatusCode(1);

        AccessEvent event3 = new AccessEvent();
        event3.setLoggerContext(new LoggerContext());
        assertFalse(event1.equals(event3));
    }

    @Test
    public void testHashCode_SameObject() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = event1;
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_SameValues() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("testURI");
        event1.setRequestURL("testURL");
        event1.setRemoteHost("testHost");
        event1.setRemoteUser("testUser");
        event1.setProtocol("testProtocol");
        event1.setMethod("testMethod");
        event1.setServerName("testServer");
        event1.setRemoteAddress("testAddress");
        event1.setRequestHeaders(new HashMap<String, String>());
        event1.setResponseHeaders(new HashMap<String, String>());
        event1.setRequestParameters(new HashMap<String, String[]>());
        event1.setLocalPort(1);
        event1.setStatusCode(1);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("testURI");
        event2.setRequestURL("testURL");
        event2.setRemoteHost("testHost");
        event2.setRemoteUser("testUser");
        event2.setProtocol("testProtocol");
        event2.setMethod("testMethod");
        event2.setServerName("testServer");
        event2.setRemoteAddress("testAddress");
        event2.setRequestHeaders(new HashMap<String, String>());
        event2.setResponseHeaders(new HashMap<String, String>());
        event2.setRequestParameters(new HashMap<String, String[]>());
        event2.setLocalPort(1);
        event2.setStatusCode(1);

        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_DifferentObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(new LoggerContext());
        event.setTimeStamp(1L);
        String expectedResult = "AccessEvent[loggerContext=LoggerContext(), timeStamp=1]";
        assertEquals(expectedResult, event.toString());
    }

    @Test
    public void testEmptyRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        assertNotNull(event.getRequestHeaders());
        assertEquals(0, event.getRequestHeaders().size());
    }

    @Test
    public void testEmptyResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        assertNotNull(event.getResponseHeaders());
        assertEquals(0, event.getResponseHeaders().size());
    }

    @Test
    public void testEmptyRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        assertNotNull(event.getRequestParameters());
        assertEquals(0, event.getRequestParameters().size());
    }

    @Test
    public void testNullRequestHeaders_GetRequestHeader() {
        AccessEvent event = new AccessEvent();
        event.setRequestHeaders(null);
        assertNull(event.getRequestHeader("headerName"));
    }

    @Test
    public void testNullRequestHeaders_GetAllRequestHeaders() {
        AccessEvent event = new AccessEvent();
        event.setRequestHeaders(null);
        assertEquals(0, event.getRequestHeaders().size());
    }

    @Test
    public void testNullResponseHeaders_GetResponseHeader() {
        AccessEvent event = new AccessEvent();
        event.setResponseHeaders(null);
        assertNull(event.getResponseHeader("headerName"));
    }

    @Test
    public void testNullResponseHeaders_GetAllResponseHeaders() {
        AccessEvent event = new AccessEvent();
        event.setResponseHeaders(null);
        assertEquals(0, event.getResponseHeaders().size());
    }

    @Test
    public void testNullRequestParameters_GetRequestParameter() {
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(null);
        assertNull(event.getRequestParameter("parameterName"));
    }

    @Test
    public void testNullRequestParameters_GetAllRequestParameters() {
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(null);
        assertEquals(0, event.getRequestParameters().size());
    }

    @Test
    public void testGettersAndSetters_WithLargeValues() {
        byte[] largeValue = new byte[2048];
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(new HashMap<String, String[]>());
        event.getRequestParameters().put("largeValue", new String(largeValue).split(" "));
        String largeValueString = new String(largeValue);
        assertEquals(largeValueString, event.getRequestParameter("largeValue")[0]);
    }
}