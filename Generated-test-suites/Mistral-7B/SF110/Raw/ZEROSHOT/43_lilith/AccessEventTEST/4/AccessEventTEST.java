package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import de.huxhorn.lilith.data.access.*;

@RunWith(JUnitRunner.class)
public class AccessEventTest {

    @Test
    public void testAccessEvent_AllFields() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer.com";
        String remoteAddress = "127.0.0.2";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
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

        assertEquals(timeStamp, event.getTimeStamp());
        assertEquals(loggerContext, event.getLoggerContext());
        assertEquals(requestURI, event.getRequestURI());
        assertEquals(requestURL, event.getRequestURL());
        assertEquals(remoteHost, event.getRemoteHost());
        assertEquals(remoteUser, event.getRemoteUser());
        assertEquals(protocol, event.getProtocol());
        assertEquals(method, event.getMethod());
        assertEquals(serverName, event.getServerName());
        assertEquals(remoteAddress, event.getRemoteAddress());
        assertEquals(requestHeaders, event.getRequestHeaders());
        assertEquals(responseHeaders, event.getResponseHeaders());
        assertEquals(requestParameters, event.getRequestParameters());
        assertEquals(localPort, event.getLocalPort());
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testAccessEvent_Equals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        Long timeStamp1 = 1L;
        Long timeStamp2 = 1L;
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        String requestURI1 = "/test";
        String requestURI2 = "/test";
        String requestURL1 = "http://test.com/test";
        String requestURL2 = "http://test.com/test";
        String remoteHost1 = "127.0.0.1";
        String remoteHost2 = "127.0.0.1";
        String remoteUser1 = "testUser";
        String remoteUser2 = "testUser";
        String protocol1 = "HTTP/1.1";
        String protocol2 = "HTTP/1.1";
        String method1 = "GET";
        String method2 = "GET";
        String serverName1 = "testServer.com";
        String serverName2 = "testServer.com";
        String remoteAddress1 = "127.0.0.2";
        String remoteAddress2 = "127.0.0.2";
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        Map<String, String[]> requestParameters1 = new HashMap<>();
        Map<String, String[]> requestParameters2 = new HashMap<>();
        int localPort1 = 8080;
        int localPort2 = 8080;
        int statusCode1 = 200;
        int statusCode2 = 200;

        event1.setTimeStamp(timeStamp1);
        event1.setLoggerContext(loggerContext1);
        event1.setRequestURI(requestURI1);
        event1.setRequestURL(requestURL1);
        event1.setRemoteHost(remoteHost1);
        event1.setRemoteUser(remoteUser1);
        event1.setProtocol(protocol1);
        event1.setMethod(method1);
        event1.setServerName(serverName1);
        event1.setRemoteAddress(remoteAddress1);
        event1.setRequestHeaders(requestHeaders1);
        event1.setResponseHeaders(responseHeaders1);
        event1.setRequestParameters(requestParameters1);
        event1.setLocalPort(localPort1);
        event1.setStatusCode(statusCode1);

        event2.setTimeStamp(timeStamp2);
        event2.setLoggerContext(loggerContext2);
        event2.setRequestURI(requestURI2);
        event2.setRequestURL(requestURL2);
        event2.setRemoteHost(remoteHost2);
        event2.setRemoteUser(remoteUser2);
        event2.setProtocol(protocol2);
        event2.setMethod(method2);
        event2.setServerName(serverName2);
        event2.setRemoteAddress(remoteAddress2);
        event2.setRequestHeaders(requestHeaders2);
        event2.setResponseHeaders(responseHeaders2);
        event2.setRequestParameters(requestParameters2);
        event2.setLocalPort(localPort2);
        event2.setStatusCode(statusCode2);

        assertTrue(event1.equals(event1));
        assertTrue(event1.equals(event2));
        assertFalse(event1.equals(null));
        assertFalse(event1.equals(""));
        assertFalse(event1.equals(new Object()));

        event2.setLocalPort(localPort1 + 1);
        assertFalse(event1.equals(event2));

        event2.setStatusCode(statusCode1 - 1);
        assertFalse(event1.equals(event2));

        event2.setLoggerContext(new LoggerContext());
        assertFalse(event1.equals(event2));

        event2.setMethod("POST");
        assertFalse(event1.equals(event2));

        event2.setProtocol("HTTP/1.2");
        assertFalse(event1.equals(event2));

        event2.setRemoteAddress("127.0.0.3");
        assertFalse(event1.equals(event2));

        event2.setRemoteUser("testUser2");
        assertFalse(event1.equals(event2));

        event2.setRequestHeaders(new HashMap<>());
        assertFalse(event1.equals(event2));

        event2.setRequestURI("/test2");
        assertFalse(event1.equals(event2));

        event2.setRequestURL("http://test.com/test2");
        assertFalse(event1.equals(event2));

        event2.setServerName("testServer2.com");
        assertFalse(event1.equals(event2));

        event2.setTimeStamp(timeStamp1 + 1);
        assertFalse(event1.equals(event2));
    }

    @Test
    public void testAccessEvent_HashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        Long timeStamp1 = 1L;
        Long timeStamp2 = 1L;
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        String requestURI1 = "/test";
        String requestURI2 = "/test";
        String requestURL1 = "http://test.com/test";
        String requestURL2 = "http://test.com/test";
        String remoteHost1 = "127.0.0.1";
        String remoteHost2 = "127.0.0.1";
        String remoteUser1 = "testUser";
        String remoteUser2 = "testUser";
        String protocol1 = "HTTP/1.1";
        String protocol2 = "HTTP/1.1";
        String method1 = "GET";
        String method2 = "GET";
        String serverName1 = "testServer.com";
        String serverName2 = "testServer.com";
        String remoteAddress1 = "127.0.0.2";
        String remoteAddress2 = "127.0.0.2";
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        Map<String, String[]> requestParameters1 = new HashMap<>();
        Map<String, String[]> requestParameters2 = new HashMap<>();
        int localPort1 = 8080;
        int localPort2 = 8080;
        int statusCode1 = 200;
        int statusCode2 = 200;

        event1.setTimeStamp(timeStamp1);
        event1.setLoggerContext(loggerContext1);
        event1.setRequestURI(requestURI1);
        event1.setRequestURL(requestURL1);
        event1.setRemoteHost(remoteHost1);
        event1.setRemoteUser(remoteUser1);
        event1.setProtocol(protocol1);
        event1.setMethod(method1);
        event1.setServerName(serverName1);
        event1.setRemoteAddress(remoteAddress1);
        event1.setRequestHeaders(requestHeaders1);
        event1.setResponseHeaders(responseHeaders1);
        event1.setRequestParameters(requestParameters1);
        event1.setLocalPort(localPort1);
        event1.setStatusCode(statusCode1);

        event2.setTimeStamp(timeStamp2);
        event2.setLoggerContext(loggerContext2);
        event2.setRequestURI(requestURI2);
        event2.setRequestURL(requestURL2);
        event2.setRemoteHost(remoteHost2);
        event2.setRemoteUser(remoteUser2);
        event2.setProtocol(protocol2);
        event2.setMethod(method2);
        event2.setServerName(serverName2);
        event2.setRemoteAddress(remoteAddress2);
        event2.setRequestHeaders(requestHeaders2);
        event2.setResponseHeaders(responseHeaders2);
        event2.setRequestParameters(requestParameters2);
        event2.setLocalPort(localPort2);
        event2.setStatusCode(statusCode2);

        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, event1.hashCode());
        assertEquals(hashCode2, hashCode2);
        assertEquals(hashCode2, event2.hashCode());
        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testAccessEvent_ToString() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer.com";
        String remoteAddress = "127.0.0.2";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
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

        String expected = "AccessEvent[loggerContext=LoggerContext, timeStamp=1]";
        String actual = event.toString();

        assertEquals(expected, actual);
    }
}