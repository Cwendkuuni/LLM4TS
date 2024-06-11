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
        assertEquals("TimeStamp should be set and get correctly", expectedTimeStamp, actualTimeStamp);
    }

    @Test
    public void testSetGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        LoggerContext actualLoggerContext = event.getLoggerContext();
        assertEquals("Logger context should be set and get correctly", loggerContext, actualLoggerContext);
    }

    @Test
    public void testGetSetRequestURI() {
        AccessEvent event = new AccessEvent();
        String expectedRequestURI = "testRequestURI";
        event.setRequestURI(expectedRequestURI);
        String actualRequestURI = event.getRequestURI();
        assertEquals("RequestURI should be set and get correctly", expectedRequestURI, actualRequestURI);
    }

    @Test
    public void testGetSetRequestURL() {
        AccessEvent event = new AccessEvent();
        String expectedRequestURL = "testRequestURL";
        event.setRequestURL(expectedRequestURL);
        String actualRequestURL = event.getRequestURL();
        assertEquals("RequestURL should be set and get correctly", expectedRequestURL, actualRequestURL);
    }

    @Test
    public void testGetSetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String expectedRemoteHost = "testRemoteHost";
        event.setRemoteHost(expectedRemoteHost);
        String actualRemoteHost = event.getRemoteHost();
        assertEquals("RemoteHost should be set and get correctly", expectedRemoteHost, actualRemoteHost);
    }

    @Test
    public void testGetSetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String expectedRemoteUser = "testRemoteUser";
        event.setRemoteUser(expectedRemoteUser);
        String actualRemoteUser = event.getRemoteUser();
        assertEquals("RemoteUser should be set and get correctly", expectedRemoteUser, actualRemoteUser);
    }

    @Test
    public void testGetSetProtocol() {
        AccessEvent event = new AccessEvent();
        String expectedProtocol = "testProtocol";
        event.setProtocol(expectedProtocol);
        String actualProtocol = event.getProtocol();
        assertEquals("Protocol should be set and get correctly", expectedProtocol, actualProtocol);
    }

    @Test
    public void testGetSetMethod() {
        AccessEvent event = new AccessEvent();
        String expectedMethod = "testMethod";
        event.setMethod(expectedMethod);
        String actualMethod = event.getMethod();
        assertEquals("Method should be set and get correctly", expectedMethod, actualMethod);
    }

    @Test
    public void testGetSetServerName() {
        AccessEvent event = new AccessEvent();
        String expectedServerName = "testServerName";
        event.setServerName(expectedServerName);
        String actualServerName = event.getServerName();
        assertEquals("ServerName should be set and get correctly", expectedServerName, actualServerName);
    }

    @Test
    public void testGetSetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String expectedRemoteAddress = "testRemoteAddress";
        event.setRemoteAddress(expectedRemoteAddress);
        String actualRemoteAddress = event.getRemoteAddress();
        assertEquals("RemoteAddress should be set and get correctly", expectedRemoteAddress, actualRemoteAddress);
    }

    @Test
    public void testGetSetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> expectedRequestHeaders = new HashMap<>();
        event.setRequestHeaders(expectedRequestHeaders);
        Map<String, String> actualRequestHeaders = event.getRequestHeaders();
        assertEquals("RequestHeaders should be set and get correctly", expectedRequestHeaders, actualRequestHeaders);
    }

    @Test
    public void testGetSetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> expectedResponseHeaders = new HashMap<>();
        event.setResponseHeaders(expectedResponseHeaders);
        Map<String, String> actualResponseHeaders = event.getResponseHeaders();
        assertEquals("ResponseHeaders should be set and get correctly", expectedResponseHeaders, actualResponseHeaders);
    }

    @Test
    public void testGetSetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> expectedRequestParameters = new HashMap<>();
        event.setRequestParameters(expectedRequestParameters);
        Map<String, String[]> actualRequestParameters = event.getRequestParameters();
        assertEquals("RequestParameters should be set and get correctly", expectedRequestParameters, actualRequestParameters);
    }

    @Test
    public void testGetSetLocalPort() {
        AccessEvent event = new AccessEvent();
        int expectedLocalPort = 1234;
        event.setLocalPort(expectedLocalPort);
        int actualLocalPort = event.getLocalPort();
        assertEquals("LocalPort should be set and get correctly", expectedLocalPort, actualLocalPort);
    }

    @Test
    public void testGetSetStatusCode() {
        AccessEvent event = new AccessEvent();
        int expectedStatusCode = 200;
        event.setStatusCode(expectedStatusCode);
        int actualStatusCode = event.getStatusCode();
        assertEquals("Status code should be set and get correctly", expectedStatusCode, actualStatusCode);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNullObject() {
        AccessEvent event = new AccessEvent();
        assertEquals("Should throw NullPointerException when comparing with null", null, event);
    }

    @Test
    public void testEqualsSameObject() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = event1;
        assertTrue("Should return true when comparing same object", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        assertFalse("Should return false when comparing different objects", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentTimeStamp() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(1L);
        event2.setTimeStamp(2L);
        assertFalse("Should return false when comparing different time stamps", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentLoggerContext() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        event1.setLoggerContext(loggerContext1);
        event2.setLoggerContext(loggerContext2);
        assertFalse("Should return false when comparing different logger contexts", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentMethod() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setMethod("GET");
        event2.setMethod("POST");
        assertFalse("Should return false when comparing different methods", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentProtocol() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setProtocol("HTTP");
        event2.setProtocol("HTTPS");
        assertFalse("Should return false when comparing different protocols", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentRemoteHost() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setRemoteHost("127.0.0.1");
        event2.setRemoteHost("192.168.0.1");
        assertFalse("Should return false when comparing different remote hosts", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentRemoteUser() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setRemoteUser("user1");
        event2.setRemoteUser("user2");
        assertFalse("Should return false when comparing different remote users", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentRequestHeaders() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        Map<String, String> headers1 = new HashMap<>();
        headers1.put("header1", "value1");
        Map<String, String> headers2 = new HashMap<>();
        headers2.put("header1", "value2");
        event1.setRequestHeaders(headers1);
        event2.setRequestHeaders(headers2);
        assertFalse("Should return false when comparing different request headers", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentRequestURI() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setRequestURI("/resource1");
        event2.setRequestURI("/resource2");
        assertFalse("Should return false when comparing different request URIs", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentRequestURL() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setRequestURL("http://example.com/resource1");
        event2.setRequestURL("http://example.com/resource2");
        assertFalse("Should return false when comparing different request URLs", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentResponseHeaders() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        Map<String, String> headers1 = new HashMap<>();
        headers1.put("header1", "value1");
        Map<String, String> headers2 = new HashMap<>();
        headers2.put("header1", "value3");
        event1.setResponseHeaders(headers1);
        event2.setResponseHeaders(headers2);
        assertFalse("Should return false when comparing different response headers", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentServerName() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setServerName("example.com");
        event2.setServerName("example2.com");
        assertFalse("Should return false when comparing different server names", event1.equals(event2));
    }

    @Test
    public void testEqualsDifferentRemoteAddress() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setRemoteAddress("127.0.0.1");
        event2.setRemoteAddress("192.168.0.1");
        assertFalse("Should return false when comparing different remote addresses", event1.equals(event2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeNullLoggerContext() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(null);
        int hashCode = event.hashCode();
    }

    @Test
    public void testHashCodeSameObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = event1;
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertEquals("Hashcodes should be equal for same objects", hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeDifferentObjects() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();
        assertNotEquals("Hashcodes should be different for different objects", hashCode1, hashCode2);
    }
}