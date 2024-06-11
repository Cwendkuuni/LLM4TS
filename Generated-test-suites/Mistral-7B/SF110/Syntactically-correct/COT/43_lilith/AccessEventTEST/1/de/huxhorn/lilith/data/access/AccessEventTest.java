package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expected = 1L;
        event.setTimeStamp(expected);
        Long actual = event.getTimeStamp();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long input = 1L;
        event.setTimeStamp(input);
        Long actual = event.getTimeStamp();
        assertEquals(input, actual);
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext expected = new LoggerContext();
        event.setLoggerContext(expected);
        LoggerContext actual = event.getLoggerContext();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext input = new LoggerContext();
        event.setLoggerContext(input);
        LoggerContext actual = event.getLoggerContext();
        assertEquals(input, actual);
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent event = new AccessEvent();
        String expected = "testURI";
        event.setRequestURI(expected);
        String actual = event.getRequestURI();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetRequestURI() {
        AccessEvent event = new AccessEvent();
        String input = "testURI";
        event.setRequestURI(input);
        String actual = event.getRequestURI();
        assertEquals(input, actual);
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        String expected = "testURL";
        event.setRequestURL(expected);
        String actual = event.getRequestURL();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetRequestURL() {
        AccessEvent event = new AccessEvent();
        String input = "testURL";
        event.setRequestURL(input);
        String actual = event.getRequestURL();
        assertEquals(input, actual);
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String expected = "testHost";
        event.setRemoteHost(expected);
        String actual = event.getRemoteHost();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String input = "testHost";
        event.setRemoteHost(input);
        String actual = event.getRemoteHost();
        assertEquals(input, actual);
    }

    @Test
    public void testGetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String expected = "testUser";
        event.setRemoteUser(expected);
        String actual = event.getRemoteUser();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String input = "testUser";
        event.setRemoteUser(input);
        String actual = event.getRemoteUser();
        assertEquals(input, actual);
    }

    @Test
    public void testGetProtocol() {
        AccessEvent event = new AccessEvent();
        String expected = "testProtocol";
        event.setProtocol(expected);
        String actual = event.getProtocol();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetProtocol() {
        AccessEvent event = new AccessEvent();
        String input = "testProtocol";
        event.setProtocol(input);
        String actual = event.getProtocol();
        assertEquals(input, actual);
    }

    @Test
    public void testGetMethod() {
        AccessEvent event = new AccessEvent();
        String expected = "testMethod";
        event.setMethod(expected);
        String actual = event.getMethod();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetMethod() {
        AccessEvent event = new AccessEvent();
        String input = "testMethod";
        event.setMethod(input);
        String actual = event.getMethod();
        assertEquals(input, actual);
    }

    @Test
    public void testGetServerName() {
        AccessEvent event = new AccessEvent();
        String expected = "testServer";
        event.setServerName(expected);
        String actual = event.getServerName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetServerName() {
        AccessEvent event = new AccessEvent();
        String input = "testServer";
        event.setServerName(input);
        String actual = event.getServerName();
        assertEquals(input, actual);
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String expected = "testAddress";
        event.setRemoteAddress(expected);
        String actual = event.getRemoteAddress();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String input = "testAddress";
        event.setRemoteAddress(input);
        String actual = event.getRemoteAddress();
        assertEquals(input, actual);
    }

    @Test
    public void testGetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> headers = new HashMap<>();
        event.setRequestHeaders(headers);
        Map<String, String> actual = event.getRequestHeaders();
        assertEquals(headers, actual);
    }

    @Test
    public void testSetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> input = new HashMap<>();
        event.setRequestHeaders(input);
        Map<String, String> actual = event.getRequestHeaders();
        assertEquals(input, actual);
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> headers = new HashMap<>();
        event.setResponseHeaders(headers);
        Map<String, String> actual = event.getResponseHeaders();
        assertEquals(headers, actual);
    }

    @Test
    public void testSetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> input = new HashMap<>();
        event.setResponseHeaders(input);
        Map<String, String> actual = event.getResponseHeaders();
        assertEquals(input, actual);
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> parameters = new HashMap<>();
        event.setRequestParameters(parameters);
        Map<String, String[]> actual = event.getRequestParameters();
        assertEquals(parameters, actual);
    }

    @Test
    public void testSetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> input = new HashMap<>();
        event.setRequestParameters(input);
        Map<String, String[]> actual = event.getRequestParameters();
        assertEquals(input, actual);
    }

    @Test
    public void testGetLocalPort() {
        AccessEvent event = new AccessEvent();
        int expected = 8080;
        event.setLocalPort(expected);
        int actual = event.getLocalPort();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetLocalPort() {
        AccessEvent event = new AccessEvent();
        int input = 8080;
        event.setLocalPort(input);
        int actual = event.getLocalPort();
        assertEquals(input, actual);
    }

    @Test
    public void testGetStatusCode() {
        AccessEvent event = new AccessEvent();
        int expected = 200;
        event.setStatusCode(expected);
        int actual = event.getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetStatusCode() {
        AccessEvent event = new AccessEvent();
        int input = 200;
        event.setStatusCode(input);
        int actual = event.getStatusCode();
        assertEquals(input, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNullObject() {
        AccessEvent event = new AccessEvent();
        assertEquals(event, null);
    }

    @Test
    public void testEqualsSameObject() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = event1;
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        assertFalse(event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentObjectsWithSameLocalPortAndStatusCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setLocalPort(1);
        event1.setStatusCode(1);
        event2.setLocalPort(1);
        event2.setStatusCode(1);
        AccessEvent loggerContextEvent1 = new AccessEvent();
        AccessEvent loggerContextEvent2 = new AccessEvent();
        loggerContextEvent1.setLoggerContext(new LoggerContext());
        loggerContextEvent2.setLoggerContext(new LoggerContext());
        event1.setLoggerContext(loggerContextEvent1);
        event2.setLoggerContext(loggerContextEvent2);
        String method1 = "GET";
        String method2 = "POST";
        event1.setMethod(method1);
        event2.setMethod(method2);
        String protocol1 = "HTTP/1.1";
        String protocol2 = "HTTP/1.1";
        event1.setProtocol(protocol1);
        event2.setProtocol(protocol2);
        String remoteAddress1 = "127.0.0.1";
        String remoteAddress2 = "127.0.0.1";
        event1.setRemoteAddress(remoteAddress1);
        event2.setRemoteAddress(remoteAddress2);
        String remoteHost1 = "localhost";
        String remoteHost2 = "localhost";
        event1.setRemoteHost(remoteHost1);
        event2.setRemoteHost(remoteHost2);
        String remoteUser1 = "testUser";
        String remoteUser2 = "testUser";
        event1.setRemoteUser(remoteUser1);
        event2.setRemoteUser(remoteUser2);
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        event1.setRequestHeaders(requestHeaders1);
        event2.setRequestHeaders(requestHeaders2);
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        event1.setResponseHeaders(responseHeaders1);
        event2.setResponseHeaders(responseHeaders2);
        Map<String, String[]> requestParameters1 = new HashMap<>();
        Map<String, String[]> requestParameters2 = new HashMap<>();
        event1.setRequestParameters(requestParameters1);
        event2.setRequestParameters(requestParameters2);
        String requestURI1 = "/test";
        String requestURI2 = "/test";
        event1.setRequestURI(requestURI1);
        event2.setRequestURI(requestURI2);
        String requestURL1 = "http://localhost/test";
        String requestURL2 = "http://localhost/test";
        event1.setRequestURL(requestURL1);
        event2.setRequestURL(requestURL2);
        String serverName1 = "localhost";
        String serverName2 = "localhost";
        event1.setServerName(serverName1);
        event2.setServerName(serverName2);
        Long timeStamp1 = 1L;
        Long timeStamp2 = 1L;
        event1.setTimeStamp(timeStamp1);
        event2.setTimeStamp(timeStamp2);
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        Long expectedTimeStamp = 1L;
        event.setTimeStamp(expectedTimeStamp);
        int expectedHashcode = 31 * 31 + 1;
        int actualHashcode = event.hashCode();
        assertEquals(expectedHashcode, actualHashcode);
    }
}