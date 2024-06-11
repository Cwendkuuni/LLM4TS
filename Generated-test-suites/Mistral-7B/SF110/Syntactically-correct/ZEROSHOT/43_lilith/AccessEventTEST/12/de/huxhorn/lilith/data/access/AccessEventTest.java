package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccessEventTest {

    private AccessEvent event;
    private LoggerContext loggerContext;

    @Before
    public void setUp() {
        loggerContext = mock(LoggerContext.class);
        event = new AccessEvent();
        event.setLoggerContext(loggerContext);
    }

    @After
    public void tearDown() {
        event = null;
        loggerContext = null;
    }

    @Test
    public void testEqualsNull() {
        assertNull(event.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        assertSame(event, event);
    }

    @Test
    public void testEqualsDifferentInstances() {
        final AccessEvent otherEvent = new AccessEvent();
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentTimeStamp() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setTimeStamp(1L);
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentLoggerContext() {
        final LoggerContext otherLoggerContext = mock(LoggerContext.class);
        event.setLoggerContext(loggerContext);
        when(otherLoggerContext.hashCode()).thenReturn(1);
        when(otherLoggerContext.equals(loggerContext)).thenReturn(false);
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setLoggerContext(otherLoggerContext);
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentMethod() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setMethod("GET");
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentProtocol() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setProtocol("HTTPS");
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentRemoteHost() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRemoteHost("127.0.0.1");
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentRemoteUser() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRemoteUser("user1");
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentServerName() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setServerName("localhost");
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentRemoteAddress() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRemoteAddress("127.0.0.1");
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentRequestHeaders() {
        final Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        event.setRequestHeaders(requestHeaders);
        final Map<String, String> otherRequestHeaders = new HashMap<>();
        otherRequestHeaders.put("Header1", "Value2");
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRequestHeaders(otherRequestHeaders);
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentRequestURI() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRequestURI("/test");
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentRequestURL() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRequestURL("http://localhost/test");
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentResponseHeaders() {
        final Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        event.setResponseHeaders(responseHeaders);
        final Map<String, String> otherResponseHeaders = new HashMap<>();
        otherResponseHeaders.put("Header1", "Value2");
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setResponseHeaders(otherResponseHeaders);
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentLocalPort() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setLocalPort(8080);
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testEqualsDifferentStatusCode() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setStatusCode(404);
        assertFalse(event.equals(otherEvent));
    }

    @Test
    public void testHashCodeSameInstance() {
        assertEquals(event.hashCode(), event.hashCode());
    }

    @Test
    public void testHashCodeDifferentInstance() {
        final AccessEvent otherEvent = new AccessEvent();
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentTimeStamp() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setTimeStamp(1L);
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentLoggerContext() {
        final LoggerContext otherLoggerContext = mock(LoggerContext.class);
        final AccessEvent otherEvent = new AccessEvent();
        when(otherLoggerContext.hashCode()).thenReturn(1);
        otherEvent.setLoggerContext(otherLoggerContext);
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentMethod() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setMethod("GET");
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentProtocol() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setProtocol("HTTPS");
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentRemoteHost() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRemoteHost("127.0.0.1");
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentRemoteUser() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRemoteUser("user1");
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentServerName() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setServerName("localhost");
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentRemoteAddress() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRemoteAddress("127.0.0.1");
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentRequestHeaders() {
        final Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        event.setRequestHeaders(requestHeaders);
        final Map<String, String> otherRequestHeaders = new HashMap<>();
        otherRequestHeaders.put("Header1", "Value2");
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRequestHeaders(otherRequestHeaders);
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentRequestURI() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRequestURI("/test");
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentRequestURL() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setRequestURL("http://localhost/test");
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentResponseHeaders() {
        final Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        event.setResponseHeaders(responseHeaders);
        final Map<String, String> otherResponseHeaders = new HashMap<>();
        otherResponseHeaders.put("Header1", "Value2");
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setResponseHeaders(otherResponseHeaders);
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentLocalPort() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setLocalPort(8080);
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferentStatusCode() {
        final AccessEvent otherEvent = new AccessEvent();
        otherEvent.setStatusCode(404);
        assertNotSame(event.hashCode(), otherEvent.hashCode());
    }

    @Test
    public void testToString() {
        final String expected = "AccessEvent[loggerContext=mock(de.huxhorn.lilith.data.LoggerContext), timeStamp=null]";
        assertEquals(expected, event.toString());
    }

    @Test
    public void testSetAndGetTimeStamp() {
        final Long timeStamp = 12345L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    @Test
    public void testSetAndGetLoggerContext() {
        event.setLoggerContext(loggerContext);
        assertSame(loggerContext, event.getLoggerContext());
    }

    @Test
    public void testSetAndGetRequestURI() {
        final String requestURI = "/test";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testSetAndGetRequestURL() {
        final String requestURL = "http://localhost/test";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testSetAndGetRemoteHost() {
        final String remoteHost = "127.0.0.1";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testSetAndGetRemoteUser() {
        final String remoteUser = "user1";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    @Test
    public void testSetAndGetProtocol() {
        final String protocol = "HTTPS";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    @Test
    public void testSetAndGetMethod() {
        final String method = "GET";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    @Test
    public void testSetAndGetServerName() {
        final String serverName = "localhost";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testSetAndGetRemoteAddress() {
        final String remoteAddress = "127.0.0.1";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }

    @Test
    public void testSetAndGetRequestHeaders() {
        final Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testSetAndGetResponseHeaders() {
        final Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testSetAndGetRequestParameters() {
        final Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Param1", new String[] {"Value1", "Value2"});
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testSetAndGetLocalPort() {
        final int localPort = 8080;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    @Test
    public void testSetAndGetStatusCode() {
        final int statusCode = 404;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }
}