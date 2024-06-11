package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testAccessEventSettersAndGetters() {
        AccessEvent accessEvent = new AccessEvent();

        accessEvent.setTimeStamp(1L);
        assertEquals(1L, accessEvent.getTimeStamp());

        accessEvent.setLoggerContext(new LoggerContext());
        assertEquals(accessEvent.getLoggerContext(), accessEvent.getLoggerContext());

        accessEvent.setRequestURI("/test/requestURI");
        assertEquals("/test/requestURI", accessEvent.getRequestURI());

        accessEvent.setRequestURL("http://test.com/requestURL");
        assertEquals("http://test.com/requestURL", accessEvent.getRequestURL());

        accessEvent.setRemoteHost("127.0.0.1");
        assertEquals("127.0.0.1", accessEvent.getRemoteHost());

        accessEvent.setRemoteUser("testUser");
        assertEquals("testUser", accessEvent.getRemoteUser());

        accessEvent.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", accessEvent.getProtocol());

        accessEvent.setMethod("GET");
        assertEquals("GET", accessEvent.getMethod());

        accessEvent.setServerName("test.com");
        assertEquals("test.com", accessEvent.getServerName());

        accessEvent.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", accessEvent.getRemoteAddress());

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());

        accessEvent.setLocalPort(8080);
        assertEquals(8080, accessEvent.getLocalPort());

        accessEvent.setStatusCode(200);
        assertEquals(200, accessEvent.getStatusCode());
    }

    @Test
    public void testAccessEventEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(1L);

        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent2.setLoggerContext(new LoggerContext());

        accessEvent1.setRequestURI("/test/requestURI");
        accessEvent2.setRequestURI("/test/requestURI");

        accessEvent1.setRequestURL("http://test.com/requestURL");
        accessEvent2.setRequestURL("http://test.com/requestURL");

        accessEvent1.setRemoteHost("127.0.0.1");
        accessEvent2.setRemoteHost("127.0.0.1");

        accessEvent1.setRemoteUser("testUser");
        accessEvent2.setRemoteUser("testUser");

        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent2.setProtocol("HTTP/1.1");

        accessEvent1.setMethod("GET");
        accessEvent2.setMethod("GET");

        accessEvent1.setServerName("test.com");
        accessEvent2.setServerName("test.com");

        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent2.setRemoteAddress("127.0.0.1");

        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Header1", "Value1");
        accessEvent1.setRequestHeaders(requestHeaders1);

        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("Header1", "Value1");
        accessEvent2.setRequestHeaders(requestHeaders2);

        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("Header1", "Value1");
        accessEvent1.setResponseHeaders(responseHeaders1);

        Map<String, String> responseHeaders2 = new HashMap<>();
        responseHeaders2.put("Header1", "Value1");
        accessEvent2.setResponseHeaders(responseHeaders2);

        Map<String, String[]> requestParameters1 = new HashMap<>();
        requestParameters1.put("param1", new String[]{"value1", "value2"});
        accessEvent1.setRequestParameters(requestParameters1);

        Map<String, String[]> requestParameters2 = new HashMap<>();
        requestParameters2.put("param1", new String[]{"value1", "value2"});
        accessEvent2.setRequestParameters(requestParameters2);

        accessEvent1.setLocalPort(8080);
        accessEvent2.setLocalPort(8080);

        accessEvent1.setStatusCode(200);
        accessEvent2.setStatusCode(200);

        assertTrue(accessEvent1.equals(accessEvent2));

        accessEvent2.setRemoteUser("testUser2");
        assertFalse(accessEvent1.equals(accessEvent2));

        accessEvent2.setRemoteUser("testUser");
        accessEvent1.setRequestParameters(null);
        assertFalse(accessEvent1.equals(accessEvent2));

        accessEvent1.setRequestParameters(new HashMap<>());
        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testAccessEventHashCode() {
        AccessEvent accessEvent = new AccessEvent();

        Long timeStamp = 1L;
        Long timeStamp2 = 2L;

        LoggerContext loggerContext = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();

        String requestURI = "/test/requestURI";
        String requestURL = "http://test.com/requestURL";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "test.com";
        String remoteAddress = "127.0.0.1";

        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();

        int localPort = 8080;
        int statusCode = 200;

        accessEvent.setTimeStamp(timeStamp);
        loggerContext.hashCode(); // call to ensure LoggerContext has been initialized
        accessEvent.setLoggerContext(loggerContext);
        accessEvent.setRequestURI(requestURI);
        accessEvent.setRequestURL(requestURL);
        accessEvent.setRemoteHost(remoteHost);
        accessEvent.setRemoteUser(remoteUser);
        accessEvent.setProtocol(protocol);
        accessEvent.setMethod(method);
        accessEvent.setServerName(serverName);
        accessEvent.setRemoteAddress(remoteAddress);
        accessEvent.setRequestHeaders(requestHeaders);
        accessEvent.setResponseHeaders(responseHeaders);
        accessEvent.setRequestParameters(requestParameters);
        accessEvent.setLocalPort(localPort);
        accessEvent.setStatusCode(statusCode);

        int expectedHashCode = 31 * (31 * (timeStamp != null ? timeStamp.hashCode() : 0) + loggerContext.hashCode())
                + requestURI.hashCode() + requestURL.hashCode() + remoteHost.hashCode() + remoteUser.hashCode()
                + protocol.hashCode() + method.hashCode() + serverName.hashCode() + remoteAddress.hashCode()
                + localPort + statusCode;

        int hashCode = accessEvent.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }
}