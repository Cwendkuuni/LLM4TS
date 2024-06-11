package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer";
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
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        AccessEvent equalEvent = new AccessEvent();
        equalEvent.setLocalPort(1);
        equalEvent.setStatusCode(1);
        equalEvent.setLoggerContext(new LoggerContext());
        equalEvent.setMethod("GET");
        equalEvent.setProtocol("HTTP/1.1");
        equalEvent.setRemoteAddress("127.0.0.1");
        equalEvent.setRemoteHost("127.0.0.1");
        equalEvent.setRemoteUser("testUser");
        equalEvent.setRequestURI("/test");
        equalEvent.setRequestURL("http://test.com/test");
        equalEvent.setTimeStamp(1L);
        equalEvent.setServerName("testServer");

        AccessEvent notEqualEvent1 = new AccessEvent();
        notEqualEvent1.setLocalPort(2);
        notEqualEvent1.setStatusCode(1);
        notEqualEvent1.setLoggerContext(new LoggerContext());
        notEqualEvent1.setMethod("GET");
        notEqualEvent1.setProtocol("HTTP/1.1");
        notEqualEvent1.setRemoteAddress("127.0.0.1");
        notEqualEvent1.setRemoteHost("127.0.0.2");
        notEqualEvent1.setRemoteUser("testUser");
        notEqualEvent1.setRequestURI("/test");
        notEqualEvent1.setRequestURL("http://test.com/test");
        notEqualEvent1.setTimeStamp(1L);
        notEqualEvent1.setServerName("testServer");

        AccessEvent notEqualEvent2 = new AccessEvent();
        notEqualEvent2.setLocalPort(1);
        notEqualEvent2.setStatusCode(2);
        notEqualEvent2.setLoggerContext(new LoggerContext());
        notEqualEvent2.setMethod("GET");
        notEqualEvent2.setProtocol("HTTP/1.1");
        notEqualEvent2.setRemoteAddress("127.0.0.1");
        notEqualEvent2.setRemoteHost("127.0.0.1");
        notEqualEvent2.setRemoteUser("testUser");
        notEqualEvent2.setRequestURI("/test");
        notEqualEvent2.setRequestURL("http://test.com/test");
        notEqualEvent2.setTimeStamp(1L);
        notEqualEvent2.setServerName("testServer");

        AccessEvent notEqualEvent3 = new AccessEvent();
        notEqualEvent3.setLocalPort(1);
        notEqualEvent3.setStatusCode(1);
        notEqualEvent3.setLoggerContext(new LoggerContext());
        notEqualEvent3.setMethod("POST");
        notEqualEvent3.setProtocol("HTTP/1.1");
        notEqualEvent3.setRemoteAddress("127.0.0.1");
        notEqualEvent3.setRemoteHost("127.0.0.1");
        notEqualEvent3.setRemoteUser("testUser");
        notEqualEvent3.setRequestURI("/test");
        notEqualEvent3.setRequestURL("http://test.com/test");
        notEqualEvent3.setTimeStamp(1L);
        notEqualEvent3.setServerName("testServer");

        assertTrue(event1.equals(event1));
        assertTrue(event1.equals(equalEvent));
        assertFalse(event1.equals(event2));
        assertFalse(event1.equals(notEqualEvent1));
        assertFalse(event1.equals(notEqualEvent2));
        assertFalse(event1.equals(notEqualEvent3));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        AccessEvent equalEvent = new AccessEvent();
        equalEvent.setLocalPort(1);
        equalEvent.setStatusCode(1);
        equalEvent.setLoggerContext(new LoggerContext());
        equalEvent.setMethod("GET");
        equalEvent.setProtocol("HTTP/1.1");
        equalEvent.setRemoteAddress("127.0.0.1");
        equalEvent.setRemoteHost("127.0.0.1");
        equalEvent.setRemoteUser("testUser");
        equalEvent.setRequestURI("/test");
        equalEvent.setRequestURL("http://test.com/test");
        equalEvent.setTimeStamp(1L);
        equalEvent.setServerName("testServer");

        AccessEvent notEqualEvent1 = new AccessEvent();
        notEqualEvent1.setLocalPort(2);
        notEqualEvent1.setStatusCode(1);
        notEqualEvent1.setLoggerContext(new LoggerContext());
        notEqualEvent1.setMethod("GET");
        notEqualEvent1.setProtocol("HTTP/1.1");
        notEqualEvent1.setRemoteAddress("127.0.0.1");
        notEqualEvent1.setRemoteHost("127.0.0.2");
        notEqualEvent1.setRemoteUser("testUser");
        notEqualEvent1.setRequestURI("/test");
        notEqualEvent1.setRequestURL("http://test.com/test");
        notEqualEvent1.setTimeStamp(1L);
        notEqualEvent1.setServerName("testServer");

        AccessEvent notEqualEvent2 = new AccessEvent();
        notEqualEvent2.setLocalPort(1);
        notEqualEvent2.setStatusCode(2);
        notEqualEvent2.setLoggerContext(new LoggerContext());
        notEqualEvent2.setMethod("GET");
        notEqualEvent2.setProtocol("HTTP/1.1");
        notEqualEvent2.setRemoteAddress("127.0.0.1");
        notEqualEvent2.setRemoteHost("127.0.0.1");
        notEqualEvent2.setRemoteUser("testUser");
        notEqualEvent2.setRequestURI("/test");
        notEqualEvent2.setRequestURL("http://test.com/test");
        notEqualEvent2.setTimeStamp(1L);
        notEqualEvent2.setServerName("testServer");

        AccessEvent notEqualEvent3 = new AccessEvent();
        notEqualEvent3.setLocalPort(1);
        notEqualEvent3.setStatusCode(1);
        notEqualEvent3.setLoggerContext(new LoggerContext());
        notEqualEvent3.setMethod("POST");
        notEqualEvent3.setProtocol("HTTP/1.1");
        notEqualEvent3.setRemoteAddress("127.0.0.1");
        notEqualEvent3.setRemoteHost("127.0.0.1");
        notEqualEvent3.setRemoteUser("testUser");
        notEqualEvent3.setRequestURI("/test");
        notEqualEvent3.setRequestURL("http://test.com/test");
        notEqualEvent3.setTimeStamp(1L);
        notEqualEvent3.setServerName("testServer");

        int hashcode1 = event1.hashCode();
        int hashcode2 = event2.hashCode();
        int hashcodeEqual = equalEvent.hashCode();

        assertEquals(hashcode1, hashcodeEqual);
        assertNotSame(hashcode1, hashcode2);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer";
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

        StringBuilder expected = new StringBuilder();
        expected.append("AccessEvent[");
        expected.append("loggerContext=").append(loggerContext).append(", ");
        expected.append("timeStamp=").append(timeStamp);
        expected.append("]");

        assertEquals(expected.toString(), event.toString());
    }

    @Test
    public void testSetRequestHeadersAndGetRequestHeaders() {
        AccessEvent event = new AccessEvent();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Accept", "application/json");
        requestHeaders.put("Accept-Language", "en-US,en;q=0.5");
        requestHeaders.put("Accept-Encoding", "gzip, deflate");
        requestHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0");
        requestHeaders.put("X-Requested-With", "XMLHttpRequest");

        event.setRequestHeaders(requestHeaders);

        Map<String, String> actualRequestHeaders = event.getRequestHeaders();

        assertEquals(requestHeaders, actualRequestHeaders);
        assertEquals("application/json", actualRequestHeaders.get("Accept"));
        assertEquals("en-US,en;q=0.5", actualRequestHeaders.get("Accept-Language"));
        assertEquals("gzip, deflate", actualRequestHeaders.get("Accept-Encoding"));
        assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0", actualRequestHeaders.get("User-Agent"));
        assertEquals("XMLHttpRequest", actualRequestHeaders.get("X-Requested-With"));
    }

    @Test
    public void testSetResponseHeadersAndGetResponseHeaders() {
        AccessEvent event = new AccessEvent();

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        responseHeaders.put("Content-Length", "1234");
        responseHeaders.put("Server", "Apache/2.4.46 (Win64) OpenSSL/1.1.1c PHP/7.4.13");

        event.setResponseHeaders(responseHeaders);

        Map<String, String> actualResponseHeaders = event.getResponseHeaders();

        assertEquals(responseHeaders, actualResponseHeaders);
        assertEquals("application/json", actualResponseHeaders.get("Content-Type"));
        assertEquals("1234", actualResponseHeaders.get("Content-Length"));
        assertEquals("Apache/2.4.46 (Win64) OpenSSL/1.1.1c PHP/7.4.13", actualResponseHeaders.get("Server"));
    }

    @Test
    public void testSetRequestParametersAndGetRequestParameters() {
        AccessEvent event = new AccessEvent();

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("name", new String[] { "value1", "value2" });
        requestParameters.put("age", new String[] { "18" });

        event.setRequestParameters(requestParameters);

        Map<String, String[]> actualRequestParameters = event.getRequestParameters();

        assertEquals(requestParameters, actualRequestParameters);
        assertArrayEquals(new String[] { "value1", "value2" }, actualRequestParameters.get("name"));
        assertEquals("18", actualRequestParameters.get("age")[0]);
    }
}