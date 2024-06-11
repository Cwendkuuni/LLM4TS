package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expectedTimeStamp = 1L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        assertEquals(expectedTimeStamp, actualTimeStamp);
    }

    @Test
    public void testSetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long inputTimeStamp = 1L;
        event.setTimeStamp(inputTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        assertEquals(inputTimeStamp, actualTimeStamp);
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext expectedLoggerContext = new LoggerContext();
        event.setLoggerContext(expectedLoggerContext);
        LoggerContext actualLoggerContext = event.getLoggerContext();
        assertEquals(expectedLoggerContext, actualLoggerContext);
    }

    @Test
    public void testSetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext inputLoggerContext = new LoggerContext();
        event.setLoggerContext(inputLoggerContext);
        LoggerContext actualLoggerContext = event.getLoggerContext();
        assertEquals(inputLoggerContext, actualLoggerContext);
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent event = new AccessEvent();
        String expectedRequestURI = "testURI";
        event.setRequestURI(expectedRequestURI);
        String actualRequestURI = event.getRequestURI();
        assertEquals(expectedRequestURI, actualRequestURI);
    }

    @Test
    public void testSetRequestURI() {
        AccessEvent event = new AccessEvent();
        String inputRequestURI = "testURI";
        event.setRequestURI(inputRequestURI);
        String actualRequestURI = event.getRequestURI();
        assertEquals(inputRequestURI, actualRequestURI);
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        String expectedRequestURL = "testURL";
        event.setRequestURL(expectedRequestURL);
        String actualRequestURL = event.getRequestURL();
        assertEquals(expectedRequestURL, actualRequestURL);
    }

    @Test
    public void testSetRequestURL() {
        AccessEvent event = new AccessEvent();
        String inputRequestURL = "testURL";
        event.setRequestURL(inputRequestURL);
        String actualRequestURL = event.getRequestURL();
        assertEquals(inputRequestURL, actualRequestURL);
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String expectedRemoteHost = "testHost";
        event.setRemoteHost(expectedRemoteHost);
        String actualRemoteHost = event.getRemoteHost();
        assertEquals(expectedRemoteHost, actualRemoteHost);
    }

    @Test
    public void testSetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String inputRemoteHost = "testHost";
        event.setRemoteHost(inputRemoteHost);
        String actualRemoteHost = event.getRemoteHost();
        assertEquals(inputRemoteHost, actualRemoteHost);
    }

    @Test
    public void testGetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String expectedRemoteUser = "testUser";
        event.setRemoteUser(expectedRemoteUser);
        String actualRemoteUser = event.getRemoteUser();
        assertEquals(expectedRemoteUser, actualRemoteUser);
    }

    @Test
    public void testSetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String inputRemoteUser = "testUser";
        event.setRemoteUser(inputRemoteUser);
        String actualRemoteUser = event.getRemoteUser();
        assertEquals(inputRemoteUser, actualRemoteUser);
    }

    @Test
    public void testGetProtocol() {
        AccessEvent event = new AccessEvent();
        String expectedProtocol = "testProtocol";
        event.setProtocol(expectedProtocol);
        String actualProtocol = event.getProtocol();
        assertEquals(expectedProtocol, actualProtocol);
    }

    @Test
    public void testSetProtocol() {
        AccessEvent event = new AccessEvent();
        String inputProtocol = "testProtocol";
        event.setProtocol(inputProtocol);
        String actualProtocol = event.getProtocol();
        assertEquals(inputProtocol, actualProtocol);
    }

    @Test
    public void testGetMethod() {
        AccessEvent event = new AccessEvent();
        String expectedMethod = "testMethod";
        event.setMethod(expectedMethod);
        String actualMethod = event.getMethod();
        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testSetMethod() {
        AccessEvent event = new AccessEvent();
        String inputMethod = "testMethod";
        event.setMethod(inputMethod);
        String actualMethod = event.getMethod();
        assertEquals(inputMethod, actualMethod);
    }

    @Test
    public void testGetServerName() {
        AccessEvent event = new AccessEvent();
        String expectedServerName = "testServerName";
        event.setServerName(expectedServerName);
        String actualServerName = event.getServerName();
        assertEquals(expectedServerName, actualServerName);
    }

    @Test
    public void testSetServerName() {
        AccessEvent event = new AccessEvent();
        String inputServerName = "testServerName";
        event.setServerName(inputServerName);
        String actualServerName = event.getServerName();
        assertEquals(inputServerName, actualServerName);
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String expectedRemoteAddress = "testRemoteAddress";
        event.setRemoteAddress(expectedRemoteAddress);
        String actualRemoteAddress = event.getRemoteAddress();
        assertEquals(expectedRemoteAddress, actualRemoteAddress);
    }

    @Test
    public void testSetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String inputRemoteAddress = "testRemoteAddress";
        event.setRemoteAddress(inputRemoteAddress);
        String actualRemoteAddress = event.getRemoteAddress();
        assertEquals(inputRemoteAddress, actualRemoteAddress);
    }

    @Test
    public void testGetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> expectedRequestHeaders = new HashMap<>();
        event.setRequestHeaders(expectedRequestHeaders);
        Map<String, String> actualRequestHeaders = event.getRequestHeaders();
        assertEquals(expectedRequestHeaders, actualRequestHeaders);
    }

    @Test
    public void testSetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> inputRequestHeaders = new HashMap<>();
        event.setRequestHeaders(inputRequestHeaders);
        Map<String, String> actualRequestHeaders = event.getRequestHeaders();
        assertEquals(inputRequestHeaders, actualRequestHeaders);
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> expectedResponseHeaders = new HashMap<>();
        event.setResponseHeaders(expectedResponseHeaders);
        Map<String, String> actualResponseHeaders = event.getResponseHeaders();
        assertEquals(expectedResponseHeaders, actualResponseHeaders);
    }

    @Test
    public void testSetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> inputResponseHeaders = new HashMap<>();
        event.setResponseHeaders(inputResponseHeaders);
        Map<String, String> actualResponseHeaders = event.getResponseHeaders();
        assertEquals(inputResponseHeaders, actualResponseHeaders);
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> expectedRequestParameters = new HashMap<>();
        event.setRequestParameters(expectedRequestParameters);
        Map<String, String[]> actualRequestParameters = event.getRequestParameters();
        assertEquals(expectedRequestParameters, actualRequestParameters);
    }

    @Test
    public void testSetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> inputRequestParameters = new HashMap<>();
        event.setRequestParameters(inputRequestParameters);
        Map<String, String[]> actualRequestParameters = event.getRequestParameters();
        assertEquals(inputRequestParameters, actualRequestParameters);
    }

    @Test
    public void testGetLocalPort() {
        AccessEvent event = new AccessEvent();
        int expectedLocalPort = 1;
        event.setLocalPort(expectedLocalPort);
        int actualLocalPort = event.getLocalPort();
        assertEquals(expectedLocalPort, actualLocalPort);
    }

    @Test
    public void testSetLocalPort() {
        AccessEvent event = new AccessEvent();
        int inputLocalPort = 1;
        event.setLocalPort(inputLocalPort);
        int actualLocalPort = event.getLocalPort();
        assertEquals(inputLocalPort, actualLocalPort);
    }

    @Test
    public void testGetStatusCode() {
        AccessEvent event = new AccessEvent();
        int expectedStatusCode = 1;
        event.setStatusCode(expectedStatusCode);
        int actualStatusCode = event.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testSetStatusCode() {
        AccessEvent event = new AccessEvent();
        int inputStatusCode = 1;
        event.setStatusCode(inputStatusCode);
        int actualStatusCode = event.getStatusCode();
        assertEquals(inputStatusCode, actualStatusCode);
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
        assertEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentLoggerContext() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        event1.setLoggerContext(loggerContext1);
        event2.setLoggerContext(loggerContext2);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentTimeStamp() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        Long timeStamp1 = 1L;
        Long timeStamp2 = 2L;
        event1.setTimeStamp(timeStamp1);
        event2.setTimeStamp(timeStamp2);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentMethod() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        String method1 = "GET";
        String method2 = "POST";
        event1.setMethod(method1);
        event2.setMethod(method2);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentProtocol() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        String protocol1 = "HTTP/1.1";
        String protocol2 = "HTTP/1.0";
        event1.setProtocol(protocol1);
        event2.setProtocol(protocol2);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentRemoteHost() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        String remoteHost1 = "127.0.0.1";
        String remoteHost2 = "192.168.0.1";
        event1.setRemoteHost(remoteHost1);
        event2.setRemoteHost(remoteHost2);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentRemoteUser() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        String remoteUser1 = "admin";
        String remoteUser2 = "guest";
        event1.setRemoteUser(remoteUser1);
        event2.setRemoteUser(remoteUser2);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentRequestURI() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        String requestURI1 = "/test/uri";
        String requestURI2 = "/other/uri";
        event1.setRequestURI(requestURI1);
        event2.setRequestURI(requestURI2);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentRequestURL() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        String requestURL1 = "http://test.com/test/uri";
        String requestURL2 = "http://other.com/other/uri";
        event1.setRequestURL(requestURL1);
        event2.setRequestURL(requestURL2);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentResponseHeaders() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        event1.setResponseHeaders(responseHeaders1);
        event2.setResponseHeaders(responseHeaders2);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testEqualsDifferentObjectDifferentServerName() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        String serverName1 = "test.com";
        String serverName2 = "other.com";
        event1.setServerName(serverName1);
        event2.setServerName(serverName2);
        assertNotEquals(event1, event2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRequestHeadersNullMap() {
        AccessEvent event = new AccessEvent();
        event.setRequestHeaders(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetResponseHeadersNullMap() {
        AccessEvent event = new AccessEvent();
        event.setResponseHeaders(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRequestParametersNullMap() {
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(null);
    }
}