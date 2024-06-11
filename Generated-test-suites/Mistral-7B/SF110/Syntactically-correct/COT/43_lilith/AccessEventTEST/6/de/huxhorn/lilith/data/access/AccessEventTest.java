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
        Long expectedTimeStamp = 1L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        assertEquals(expectedTimeStamp, actualTimeStamp);
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
        LoggerContext expectedLoggerContext = new LoggerContext();
        event.setLoggerContext(expectedLoggerContext);
        LoggerContext actualLoggerContext = event.getLoggerContext();
        assertEquals(expectedLoggerContext, actualLoggerContext);
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
        String expectedRequestURI = "testURI";
        event.setRequestURI(expectedRequestURI);
        String actualRequestURI = event.getRequestURI();
        assertEquals(expectedRequestURI, actualRequestURI);
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
        String expectedRequestURL = "testURL";
        event.setRequestURL(expectedRequestURL);
        String actualRequestURL = event.getRequestURL();
        assertEquals(expectedRequestURL, actualRequestURL);
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
        String expectedRemoteHost = "testHost";
        event.setRemoteHost(expectedRemoteHost);
        String actualRemoteHost = event.getRemoteHost();
        assertEquals(expectedRemoteHost, actualRemoteHost);
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
        String expectedRemoteUser = "testUser";
        event.setRemoteUser(expectedRemoteUser);
        String actualRemoteUser = event.getRemoteUser();
        assertEquals(expectedRemoteUser, actualRemoteUser);
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
        String expectedProtocol = "testProtocol";
        event.setProtocol(expectedProtocol);
        String actualProtocol = event.getProtocol();
        assertEquals(expectedProtocol, actualProtocol);
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
        String expectedMethod = "testMethod";
        event.setMethod(expectedMethod);
        String actualMethod = event.getMethod();
        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetServerName() {
        AccessEvent event = new AccessEvent();
        String expectedServerName = "testServer";
        event.setServerName(expectedServerName);
        String actualServerName = event.getServerName();
        assertEquals(expectedServerName, actualServerName);
    }

    @Test
    public void testSetServerName() {
        AccessEvent event = new AccessEvent();
        String expectedServerName = "testServer";
        event.setServerName(expectedServerName);
        String actualServerName = event.getServerName();
        assertEquals(expectedServerName, actualServerName);
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String expectedRemoteAddress = "testAddress";
        event.setRemoteAddress(expectedRemoteAddress);
        String actualRemoteAddress = event.getRemoteAddress();
        assertEquals(expectedRemoteAddress, actualRemoteAddress);
    }

    @Test
    public void testSetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String expectedRemoteAddress = "testAddress";
        event.setRemoteAddress(expectedRemoteAddress);
        String actualRemoteAddress = event.getRemoteAddress();
        assertEquals(expectedRemoteAddress, actualRemoteAddress);
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
        Map<String, String> expectedRequestHeaders = new HashMap<>();
        event.setRequestHeaders(expectedRequestHeaders);
        Map<String, String> actualRequestHeaders = event.getRequestHeaders();
        assertEquals(expectedRequestHeaders, actualRequestHeaders);
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
        Map<String, String> expectedResponseHeaders = new HashMap<>();
        event.setResponseHeaders(expectedResponseHeaders);
        Map<String, String> actualResponseHeaders = event.getResponseHeaders();
        assertEquals(expectedResponseHeaders, actualResponseHeaders);
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
        Map<String, String[]> expectedRequestParameters = new HashMap<>();
        event.setRequestParameters(expectedRequestParameters);
        Map<String, String[]> actualRequestParameters = event.getRequestParameters();
        assertEquals(expectedRequestParameters, actualRequestParameters);
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
        int expectedLocalPort = 1;
        event.setLocalPort(expectedLocalPort);
        int actualLocalPort = event.getLocalPort();
        assertEquals(expectedLocalPort, actualLocalPort);
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
        int expectedStatusCode = 1;
        event.setStatusCode(expectedStatusCode);
        int actualStatusCode = event.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNullObject() {
        AccessEvent event = new AccessEvent();
        assertFalse(event.equals(null));
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

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        assertFalse(event1.equals(event2));

        event1.setTimeStamp(1L);
        event2.setTimeStamp(2L);
        assertFalse(event1.equals(event2));

        event1.setMethod("GET");
        event2.setMethod("POST");
        assertFalse(event1.equals(event2));

        event1.setRemoteHost("192.168.1.1");
        event2.setRemoteHost("192.168.1.2");
        assertFalse(event1.equals(event2));

        event1.setRemoteUser("user1");
        event2.setRemoteUser("user2");
        assertFalse(event1.equals(event2));

        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.0");
        assertFalse(event1.equals(event2));

        event1.setServerName("server1.com");
        event2.setServerName("server2.com");
        assertFalse(event1.equals(event2));

        event1.setRemoteAddress("192.168.1.1");
        event2.setRemoteAddress("192.168.1.2");
        assertFalse(event1.equals(event2));

        event1.setRequestHeaders(new HashMap<>());
        event2.setRequestHeaders(new HashMap<>());
        assertFalse(event1.equals(event2));

        event1.setRequestURI("/testURI");
        event2.setRequestURI("/testURI2");
        assertFalse(event1.equals(event2));

        event1.setRequestURL("http://example.com/testURL");
        event2.setRequestURL("http://example.com/testURL2");
        assertFalse(event1.equals(event2));

        event1.setResponseHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        assertFalse(event1.equals(event2));

        event1.setServerName("server1.com");
        event2.setServerName("server1.com");
        event1.setLocalPort(1);
        event2.setLocalPort(2);
        assertFalse(event1.equals(event2));

        event1.setStatusCode(1);
        event2.setStatusCode(2);
        assertFalse(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        int expectedHashCode = 1;
        int actualHashCode = event.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        StringBuilder expectedStringBuilder = new StringBuilder();
        expectedStringBuilder.append("AccessEvent[loggerContext=");
        expectedStringBuilder.append(event.getLoggerContext());
        expectedStringBuilder.append(", timeStamp=");
        expectedStringBuilder.append(event.getTimeStamp());
        expectedStringBuilder.append("]");
        String actualToString = event.toString();
        assertEquals(expectedStringBuilder.toString(), actualToString);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullLoggerContext() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullRequestURI() {
        AccessEvent event = new AccessEvent();
        event.setRequestURI(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullRequestURL() {
        AccessEvent event = new AccessEvent();
        event.setRequestURL(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullRemoteHost() {
        AccessEvent event = new AccessEvent();
        event.setRemoteHost(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullRemoteUser() {
        AccessEvent event = new AccessEvent();
        event.setRemoteUser(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullProtocol() {
        AccessEvent event = new AccessEvent();
        event.setProtocol(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullMethod() {
        AccessEvent event = new AccessEvent();
        event.setMethod(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullServerName() {
        AccessEvent event = new AccessEvent();
        event.setServerName(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullRemoteAddress() {
        AccessEvent event = new AccessEvent();
        event.setRemoteAddress(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullRequestHeaders() {
        AccessEvent event = new AccessEvent();
        event.setRequestHeaders(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullResponseHeaders() {
        AccessEvent event = new AccessEvent();
        event.setResponseHeaders(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullRequestParameters() {
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLocalPortNegativeValue() {
        AccessEvent event = new AccessEvent();
        event.setLocalPort(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStatusCodeNegativeValue() {
        AccessEvent event = new AccessEvent();
        event.setStatusCode(-1);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        AccessEvent originalEvent = new AccessEvent();
        AccessEvent clonedEvent = originalEvent.clone();
        assertEquals(originalEvent, clonedEvent);
    }
}