package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    // Test for getter and setter methods of timeStamp
    @Test
    public void testGetSetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long expectedTimeStamp = 1L;
        accessEvent.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = accessEvent.getTimeStamp();
        assertEquals("TimeStamp should match", expectedTimeStamp, actualTimeStamp);
    }

    // Test for getter and setter methods of loggerContext
    @Test
    public void testGetSetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(loggerContext);
        LoggerContext actualLoggerContext = accessEvent.getLoggerContext();
        assertEquals("LoggerContext should match", loggerContext, actualLoggerContext);
    }

    // Test for getter and setter methods of requestURI
    @Test
    public void testGetSetRequestURI() {
        String expectedRequestURI = "testRequestURI";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURI(expectedRequestURI);
        String actualRequestURI = accessEvent.getRequestURI();
        assertEquals("RequestURI should match", expectedRequestURI, actualRequestURI);
    }

    // Test for getter and setter methods of requestURL
    @Test
    public void testGetSetRequestURL() {
        String expectedRequestURL = "testRequestURL";
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURL(expectedRequestURL);
        String actualRequestURL = accessEvent.getRequestURL();
        assertEquals("RequestURL should match", expectedRequestURL, actualRequestURL);
    }

    // Test for getter methods of remoteHost, remoteUser, protocol, method, serverName, remoteAddress, requestHeaders, responseHeaders, requestParameters, localPort, and statusCode
    @Test
    public void testGetters() {
        AccessEvent accessEvent = new AccessEvent();

        // Test remoteHost getter
        String expectedRemoteHost = "testRemoteHost";
        accessEvent.setRemoteHost(expectedRemoteHost);
        String actualRemoteHost = accessEvent.getRemoteHost();
        assertEquals("RemoteHost should match", expectedRemoteHost, actualRemoteHost);

        // Test remoteUser getter
        String expectedRemoteUser = "testRemoteUser";
        accessEvent.setRemoteUser(expectedRemoteUser);
        String actualRemoteUser = accessEvent.getRemoteUser();
        assertEquals("RemoteUser should match", expectedRemoteUser, actualRemoteUser);

        // Test protocol getter
        String expectedProtocol = "testProtocol";
        accessEvent.setProtocol(expectedProtocol);
        String actualProtocol = accessEvent.getProtocol();
        assertEquals("Protocol should match", expectedProtocol, actualProtocol);

        // Test method getter
        String expectedMethod = "testMethod";
        accessEvent.setMethod(expectedMethod);
        String actualMethod = accessEvent.getMethod();
        assertEquals("Method should match", expectedMethod, actualMethod);

        // Test serverName getter
        String expectedServerName = "testServerName";
        accessEvent.setServerName(expectedServerName);
        String actualServerName = accessEvent.getServerName();
        assertEquals("ServerName should match", expectedServerName, actualServerName);

        // Test remoteAddress getter
        String expectedRemoteAddress = "testRemoteAddress";
        accessEvent.setRemoteAddress(expectedRemoteAddress);
        String actualRemoteAddress = accessEvent.getRemoteAddress();
        assertEquals("RemoteAddress should match", expectedRemoteAddress, actualRemoteAddress);

        // Test requestHeaders getter
        Map<String, String> expectedRequestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(expectedRequestHeaders);
        Map<String, String> actualRequestHeaders = accessEvent.getRequestHeaders();
        assertEquals("RequestHeaders should match", expectedRequestHeaders, actualRequestHeaders);

        // Test responseHeaders getter
        Map<String, String> expectedResponseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(expectedResponseHeaders);
        Map<String, String> actualResponseHeaders = accessEvent.getResponseHeaders();
        assertEquals("ResponseHeaders should match", expectedResponseHeaders, actualResponseHeaders);

        // Test requestParameters getter
        Map<String, String[]> expectedRequestParameters = new HashMap<>();
        accessEvent.setRequestParameters(expectedRequestParameters);
        Map<String, String[]> actualRequestParameters = accessEvent.getRequestParameters();
        assertEquals("RequestParameters should match", expectedRequestParameters, actualRequestParameters);

        // Test localPort getter
        int expectedLocalPort = 1234;
        accessEvent.setLocalPort(expectedLocalPort);
        int actualLocalPort = accessEvent.getLocalPort();
        assertEquals("LocalPort should match", expectedLocalPort, actualLocalPort);

        // Test statusCode getter
        int expectedStatusCode = 200;
        accessEvent.setStatusCode(expectedStatusCode);
        int actualStatusCode = accessEvent.getStatusCode();
        assertEquals("StatusCode should match", expectedStatusCode, actualStatusCode);
    }

    // Test equals method with same object
    @Test
    public void testEqualsSameObject() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;
        assertTrue("Should be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with null object
    @Test
    public void testEqualsNullObject() {
        AccessEvent accessEvent = new AccessEvent();
        assertFalse("Should not be equal", accessEvent.equals(null));
    }

    // Test equals method with different classes
    @Test
    public void testEqualsDifferentClasses() {
        AccessEvent accessEvent = new AccessEvent();
        Object otherObject = new Object();
        assertFalse("Should not be equal", accessEvent.equals(otherObject));
    }

    // Test equals method with different localPort
    @Test
    public void testEqualsDifferentLocalPort() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setLocalPort(1235);
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different statusCode
    @Test
    public void testEqualsDifferentStatusCode() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setStatusCode(201);
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different loggerContext
    @Test
    public void testEqualsDifferentLoggerContext() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        LoggerContext loggerContext2 = new LoggerContext();
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent2.setLoggerContext(loggerContext2);
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different method
    @Test
    public void testEqualsDifferentMethod() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setMethod("GET");
        accessEvent2.setMethod("POST");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different protocol
    @Test
    public void testEqualsDifferentProtocol() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent2.setProtocol("HTTP/2.0");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different remoteAddress
    @Test
    public void testEqualsDifferentRemoteAddress() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent2.setRemoteAddress("192.168.1.1");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different remoteHost
    @Test
    public void testEqualsDifferentRemoteHost() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteHost("localhost");
        accessEvent2.setRemoteHost("192.168.1.1");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different remoteUser
    @Test
    public void testEqualsDifferentRemoteUser() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteUser("testUser1");
        accessEvent2.setRemoteUser("testUser2");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different requestHeaders
    @Test
    public void testEqualsDifferentRequestHeaders() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent2.setRequestHeaders(requestHeaders2);
        requestHeaders1.put("Header1", "Value1");
        requestHeaders2.put("Header1", "Value2");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different requestURI
    @Test
    public void testEqualsDifferentRequestURI() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURI("/testURI1");
        accessEvent2.setRequestURI("/testURI2");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different requestURL
    @Test
    public void testEqualsDifferentRequestURL() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURL("http://test.com/testURI1");
        accessEvent2.setRequestURL("http://test.com/testURI2");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different responseHeaders
    @Test
    public void testEqualsDifferentResponseHeaders() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent2.setResponseHeaders(responseHeaders2);
        responseHeaders1.put("Header1", "Value1");
        responseHeaders2.put("Header1", "Value2");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test equals method with different serverName
    @Test
    public void testEqualsDifferentServerName() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setServerName("testServer1");
        accessEvent2.setServerName("testServer2");
        assertFalse("Should not be equal", accessEvent1.equals(accessEvent2));
    }

    // Test hashCode method with same object
    @Test
    public void testHashCodeSameObject() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;
        int expectedHashCode = accessEvent1.hashCode();
        int actualHashCode = accessEvent2.hashCode();
        assertEquals("Hashcodes should match", expectedHashCode, actualHashCode);
    }

    // Test hashCode method with different objects
    @Test
    public void testHashCodeDifferentObjects() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        int expectedHashCode1 = accessEvent1.hashCode();
        int expectedHashCode2 = accessEvent2.hashCode();
        assertNotEquals("Hashcodes should be different", expectedHashCode1, expectedHashCode2);
    }

    // Test toString method
    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("AccessEvent[loggerContext=");
        expectedResult.append(accessEvent.getLoggerContext());
        expectedResult.append(", ");
        expectedResult.append("timeStamp=");
        expectedResult.append(accessEvent.getTimeStamp());
        expectedResult.append("]");
        String actualResult = accessEvent.toString();
        assertEquals("ToString method should return expected string", expectedResult.toString(), actualResult);
    }
}