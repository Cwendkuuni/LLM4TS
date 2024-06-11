package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters_TimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long expectedTimeStamp = 1L;
        accessEvent.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = accessEvent.getTimeStamp();
        assertEquals(expectedTimeStamp, actualTimeStamp);
    }

    @Test
    public void testGettersAndSetters_LoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        LoggerContext actualLoggerContext = accessEvent.getLoggerContext();
        assertEquals(loggerContext, actualLoggerContext);
    }

    @Test
    public void testGettersAndSetters_RequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedRequestURI = "expectedRequestURI";
        accessEvent.setRequestURI(expectedRequestURI);
        String actualRequestURI = accessEvent.getRequestURI();
        assertEquals(expectedRequestURI, actualRequestURI);
    }

    @Test
    public void testGettersAndSetters_RequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedRequestURL = "expectedRequestURL";
        accessEvent.setRequestURL(expectedRequestURL);
        String actualRequestURL = accessEvent.getRequestURL();
        assertEquals(expectedRequestURL, actualRequestURL);
    }

    @Test
    public void testGettersAndSetters_RemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedRemoteHost = "expectedRemoteHost";
        accessEvent.setRemoteHost(expectedRemoteHost);
        String actualRemoteHost = accessEvent.getRemoteHost();
        assertEquals(expectedRemoteHost, actualRemoteHost);
    }

    @Test
    public void testGettersAndSetters_RemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedRemoteUser = "expectedRemoteUser";
        accessEvent.setRemoteUser(expectedRemoteUser);
        String actualRemoteUser = accessEvent.getRemoteUser();
        assertEquals(expectedRemoteUser, actualRemoteUser);
    }

    @Test
    public void testGettersAndSetters_Protocol() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedProtocol = "expectedProtocol";
        accessEvent.setProtocol(expectedProtocol);
        String actualProtocol = accessEvent.getProtocol();
        assertEquals(expectedProtocol, actualProtocol);
    }

    @Test
    public void testGettersAndSetters_Method() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedMethod = "expectedMethod";
        accessEvent.setMethod(expectedMethod);
        String actualMethod = accessEvent.getMethod();
        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGettersAndSetters_ServerName() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedServerName = "expectedServerName";
        accessEvent.setServerName(expectedServerName);
        String actualServerName = accessEvent.getServerName();
        assertEquals(expectedServerName, actualServerName);
    }

    @Test
    public void testGettersAndSetters_RemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedRemoteAddress = "expectedRemoteAddress";
        accessEvent.setRemoteAddress(expectedRemoteAddress);
        String actualRemoteAddress = accessEvent.getRemoteAddress();
        assertEquals(expectedRemoteAddress, actualRemoteAddress);
    }

    @Test
    public void testGettersAndSetters_RequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> expectedRequestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(expectedRequestHeaders);
        Map<String, String> actualRequestHeaders = accessEvent.getRequestHeaders();
        assertEquals(expectedRequestHeaders, actualRequestHeaders);
    }

    @Test
    public void testGettersAndSetters_ResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> expectedResponseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(expectedResponseHeaders);
        Map<String, String> actualResponseHeaders = accessEvent.getResponseHeaders();
        assertEquals(expectedResponseHeaders, actualResponseHeaders);
    }

    @Test
    public void testGettersAndSetters_RequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String[]> expectedRequestParameters = new HashMap<>();
        accessEvent.setRequestParameters(expectedRequestParameters);
        Map<String, String[]> actualRequestParameters = accessEvent.getRequestParameters();
        assertEquals(expectedRequestParameters, actualRequestParameters);
    }

    @Test
    public void testGettersAndSetters_LocalPort() {
        AccessEvent accessEvent = new AccessEvent();
        int expectedLocalPort = 1;
        accessEvent.setLocalPort(expectedLocalPort);
        int actualLocalPort = accessEvent.getLocalPort();
        assertEquals(expectedLocalPort, actualLocalPort);
    }

    @Test
    public void testGettersAndSetters_StatusCode() {
        AccessEvent accessEvent = new AccessEvent();
        int expectedStatusCode = 1;
        accessEvent.setStatusCode(expectedStatusCode);
        int actualStatusCode = accessEvent.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test(expected = NullPointerException.class)
    public void testEquals_NullObject() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.equals(null);
    }

    @Test
    public void testEquals_DifferentLoggerContext() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        accessEvent1.setLoggerContext(loggerContext1);
        accessEvent2.setLoggerContext(loggerContext2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentMethod() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setMethod("GET");
        accessEvent2.setMethod("POST");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentProtocol() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent2.setProtocol("HTTP/1.0");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentRemoteAddress() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent2.setRemoteAddress("192.168.1.1");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentRemoteUser() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteUser("user1");
        accessEvent2.setRemoteUser("user2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentRequestHeaders() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Header1", "Value1");
        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("Header1", "Value2");
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent2.setRequestHeaders(requestHeaders2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentRequestURI() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURI("/test1");
        accessEvent2.setRequestURI("/test2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentRequestURL() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURL("http://example.com/test1");
        accessEvent2.setRequestURL("http://example.com/test2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentResponseHeaders() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("Header1", "Value1");
        Map<String, String> responseHeaders2 = new HashMap<>();
        responseHeaders2.put("Header1", "Value2");
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent2.setResponseHeaders(responseHeaders2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentServerName() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setServerName("example.com");
        accessEvent2.setServerName("example2.com");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentTimeStamp() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Long timeStamp1 = 1L;
        Long timeStamp2 = 2L;
        accessEvent1.setTimeStamp(timeStamp1);
        accessEvent2.setTimeStamp(timeStamp2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode_SameObjects() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("testURI");
        accessEvent1.setRequestURL("testURL");
        accessEvent1.setRemoteHost("testHost");
        accessEvent1.setRemoteUser("testUser");
        accessEvent1.setProtocol("testProtocol");
        accessEvent1.setMethod("testMethod");
        accessEvent1.setServerName("testServerName");
        accessEvent1.setRemoteAddress("testRemoteAddress");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(1);
        accessEvent1.setStatusCode(1);

        accessEvent2.setTimeStamp(1L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("testURI");
        accessEvent2.setRequestURL("testURL");
        accessEvent2.setRemoteHost("testHost");
        accessEvent2.setRemoteUser("testUser");
        accessEvent2.setProtocol("testProtocol");
        accessEvent2.setMethod("testMethod");
        accessEvent2.setServerName("testServerName");
        accessEvent2.setRemoteAddress("testRemoteAddress");
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent2.setLocalPort(1);
        accessEvent2.setStatusCode(1);

        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Long timeStamp1 = 1L;
        Long timeStamp2 = 2L;
        accessEvent1.setTimeStamp(timeStamp1);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("testURI1");
        accessEvent1.setRequestURL("testURL1");
        accessEvent1.setRemoteHost("testHost1");
        accessEvent1.setRemoteUser("testUser1");
        accessEvent1.setProtocol("testProtocol1");
        accessEvent1.setMethod("testMethod1");
        accessEvent1.setServerName("testServerName1");
        accessEvent1.setRemoteAddress("testRemoteAddress1");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(1);
        accessEvent1.setStatusCode(1);

        accessEvent2.setTimeStamp(timeStamp2);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("testURI2");
        accessEvent2.setRequestURL("testURL2");
        accessEvent2.setRemoteHost("testHost2");
        accessEvent2.setRemoteUser("testUser2");
        accessEvent2.setProtocol("testProtocol2");
        accessEvent2.setMethod("testMethod2");
        accessEvent2.setServerName("testServerName2");
        accessEvent2.setRemoteAddress("testRemoteAddress2");
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent2.setLocalPort(1);
        accessEvent2.setStatusCode(1);

        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);
        accessEvent.toString();

        String expectedOutput = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=" + timeStamp + "]";
        assertNotNull(accessEvent.toString());
        assertEquals(expectedOutput, accessEvent.toString());
    }
}