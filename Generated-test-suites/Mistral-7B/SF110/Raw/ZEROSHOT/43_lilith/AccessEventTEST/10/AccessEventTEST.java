package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testAccessEvent() {
        // create an instance of AccessEvent
        AccessEvent accessEvent = new AccessEvent();
        
        // test setting and getting timeStamp
        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());

        // test setting and getting loggerContext
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());

        // test setting and getting requestURI
        String requestURI = "/test";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());

        // test setting and getting requestURL
        String requestURL = "http://test.com";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());

        // test setting and getting remoteHost
        String remoteHost = "127.0.0.1";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());

        // test setting and getting remoteUser
        String remoteUser = "testuser";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());

        // test setting and getting protocol
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());

        // test setting and getting method
        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());

        // test setting and getting serverName
        String serverName = "localhost";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());

        // test setting and getting remoteAddress
        String remoteAddress = "127.0.0.1:8080";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());

        // test setting and getting requestHeaders
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());

        // test setting and getting responseHeaders
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());

        // test setting and getting requestParameters
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());

        // test setting and getting localPort
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());

        // test setting and getting statusCode
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());

        // test equals method
        AccessEvent accessEvent1 = new AccessEvent();
        assertFalse(accessEvent.equals(accessEvent1));

        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(timeStamp);
        accessEvent2.setLoggerContext(loggerContext);
        accessEvent2.setRequestURI(requestURI);
        accessEvent2.setRequestURL(requestURL);
        accessEvent2.setRemoteHost(remoteHost);
        accessEvent2.setRemoteUser(remoteUser);
        accessEvent2.setProtocol(protocol);
        accessEvent2.setMethod(method);
        accessEvent2.setServerName(serverName);
        accessEvent2.setRemoteAddress(remoteAddress);
        accessEvent2.setRequestHeaders(requestHeaders);
        accessEvent2.setResponseHeaders(responseHeaders);
        accessEvent2.setRequestParameters(requestParameters);
        accessEvent2.setLocalPort(localPort);
        accessEvent2.setStatusCode(statusCode);
        assertTrue(accessEvent.equals(accessEvent2));

        // test hashCode method
        int hashCode1 = accessEvent.hashCode();
        int hashCode2 = accessEvent2.hashCode();
        assertNotEquals(hashCode1, hashCode2);

        AccessEvent accessEvent3 = new AccessEvent();
        accessEvent3.setTimeStamp(timeStamp);
        accessEvent3.setLoggerContext(loggerContext);
        accessEvent3.setRequestURI(requestURI);
        accessEvent3.setRequestURL(requestURL);
        accessEvent3.setRemoteHost(remoteHost);
        accessEvent3.setRemoteUser(remoteUser);
        accessEvent3.setProtocol(protocol);
        accessEvent3.setMethod(method);
        accessEvent3.setServerName(serverName);
        accessEvent3.setRemoteAddress(remoteAddress);
        accessEvent3.setRequestHeaders(requestHeaders);
        accessEvent3.setResponseHeaders(responseHeaders);
        accessEvent3.setRequestParameters(requestParameters);
        accessEvent3.setLocalPort(localPort);
        accessEvent3.setStatusCode(statusCode);
        assertEquals(hashCode1, accessEvent3.hashCode());

        // test toString method
        String toString = accessEvent.toString();
        assertTrue(toString.contains("AccessEvent["));
        assertTrue(toString.contains("loggerContext="));
        assertTrue(toString.contains("timeStamp="));
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
}