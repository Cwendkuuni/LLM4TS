package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

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
        String requestURI = "requestURI";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "requestURL";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "remoteHost";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "remoteUser";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_protocol() {
        AccessEvent event = new AccessEvent();
        String protocol = "protocol";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    @Test
    public void testGettersAndSetters_method() {
        AccessEvent event = new AccessEvent();
        String method = "method";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    @Test
    public void testGettersAndSetters_serverName() {
        AccessEvent event = new AccessEvent();
        String serverName = "serverName";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "remoteAddress";
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
        int localPort = 1;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        AccessEvent event = new AccessEvent();
        int statusCode = 1;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("requestURI1");
        event1.setRequestURL("requestURL1");
        event1.setRemoteHost("remoteHost1");
        event1.setRemoteUser("remoteUser1");
        event1.setProtocol("protocol1");
        event1.setMethod("method1");
        event1.setServerName("serverName1");
        event1.setRemoteAddress("remoteAddress1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(1);
        event1.setStatusCode(1);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("requestURI1");
        event2.setRequestURL("requestURL1");
        event2.setRemoteHost("remoteHost1");
        event2.setRemoteUser("remoteUser1");
        event2.setProtocol("protocol1");
        event2.setMethod("method1");
        event2.setServerName("serverName1");
        event2.setRemoteAddress("remoteAddress1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(1);
        event2.setStatusCode(1);

        assertTrue(event1.equals(event2));

        event3.setTimeStamp(2L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("requestURI1");
        event3.setRequestURL("requestURL1");
        event3.setRemoteHost("remoteHost1");
        event3.setRemoteUser("remoteUser1");
        event3.setProtocol("protocol1");
        event3.setMethod("method1");
        event3.setServerName("serverName1");
        event3.setRemoteAddress("remoteAddress1");
        event3.setRequestHeaders(new HashMap<>());
        event3.setResponseHeaders(new HashMap<>());
        event3.setRequestParameters(new HashMap<>());
        event3.setLocalPort(1);
        event3.setStatusCode(1);

        assertFalse(event1.equals(event3));

        event3.setTimeStamp(1L);

        assertFalse(event1.equals(event3));

        event3.setLocalPort(2);

        assertFalse(event1.equals(event3));

        event3.setStatusCode(2);

        assertFalse(event1.equals(event3));

        event3.setLoggerContext(new LoggerContext());

        assertFalse(event1.equals(event3));

        event3.setMethod("method2");

        assertFalse(event1.equals(event3));

        event3.setProtocol("protocol2");

        assertFalse(event1.equals(event3));

        event3.setRemoteAddress("remoteAddress2");

        assertFalse(event1.equals(event3));

        event3.setRemoteHost("remoteHost2");

        assertFalse(event1.equals(event3));

        event3.setRemoteUser("remoteUser2");

        assertFalse(event1.equals(event3));

        event3.setRequestURI("requestURI2");

        assertFalse(event1.equals(event3));

        event3.setRequestURL("requestURL2");

        assertFalse(event1.equals(event3));

        event3.setRequestHeaders(new HashMap<>());

        assertFalse(event1.equals(event3));

        event3.setResponseHeaders(new HashMap<>());

        assertFalse(event1.equals(event3));

        event3.setRequestParameters(new HashMap<>());

        assertFalse(event1.equals(event3));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("requestURI1");
        event1.setRequestURL("requestURL1");
        event1.setRemoteHost("remoteHost1");
        event1.setRemoteUser("remoteUser1");
        event1.setProtocol("protocol1");
        event1.setMethod("method1");
        event1.setServerName("serverName1");
        event1.setRemoteAddress("remoteAddress1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(1);
        event1.setStatusCode(1);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("requestURI1");
        event2.setRequestURL("requestURL1");
        event2.setRemoteHost("remoteHost1");
        event2.setRemoteUser("remoteUser1");
        event2.setProtocol("protocol1");
        event2.setMethod("method1");
        event2.setServerName("serverName1");
        event2.setRemoteAddress("remoteAddress1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(1);
        event2.setStatusCode(1);

        assertEquals(event1.hashCode(), event2.hashCode());

        event3.setTimeStamp(2L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("requestURI1");
        event3.setRequestURL("requestURL1");
        event3.setRemoteHost("remoteHost1");
        event3.setRemoteUser("remoteUser1");
        event3.setProtocol("protocol1");
        event3.setMethod("method1");
        event3.setServerName("serverName1");
        event3.setRemoteAddress("remoteAddress1");
        event3.setRequestHeaders(new HashMap<>());
        event3.setResponseHeaders(new HashMap<>());
        event3.setRequestParameters(new HashMap<>());
        event3.setLocalPort(1);
        event3.setStatusCode(1);

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setTimeStamp(1L);

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setLocalPort(2);

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setStatusCode(2);

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setLoggerContext(new LoggerContext());

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setMethod("method2");

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setProtocol("protocol2");

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setRemoteAddress("remoteAddress2");

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setRemoteHost("remoteHost2");

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setRemoteUser("remoteUser2");

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setRequestURI("requestURI2");

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setRequestURL("requestURL2");

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setRequestHeaders(new HashMap<>());

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setResponseHeaders(new HashMap<>());

        assertNotEquals(event1.hashCode(), event3.hashCode());

        event3.setRequestParameters(new HashMap<>());

        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        assertEquals("AccessEvent[loggerContext=null, timeStamp=null]", event.toString());

        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        assertEquals("AccessEvent[loggerContext=" + event.getLoggerContext() + ", timeStamp=" + event.getTimeStamp() + "]", event.toString());
    }
}