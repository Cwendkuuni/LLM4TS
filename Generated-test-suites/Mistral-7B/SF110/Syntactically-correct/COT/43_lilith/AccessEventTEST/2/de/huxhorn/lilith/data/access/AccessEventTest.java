package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

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
        LoggerContext expectedLoggerContext = new LoggerContext();
        accessEvent.setLoggerContext(expectedLoggerContext);
        LoggerContext actualLoggerContext = accessEvent.getLoggerContext();
        assertEquals(expectedLoggerContext, actualLoggerContext);
    }

    @Test
    public void testGettersAndSetters_RequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedRequestURI = "expectedURI";
        accessEvent.setRequestURI(expectedRequestURI);
        String actualRequestURI = accessEvent.getRequestURI();
        assertEquals(expectedRequestURI, actualRequestURI);
    }

    @Test
    public void testGettersAndSetters_RequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        String expectedRequestURL = "expectedURL";
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
    public void testSetters_NullArgument_LoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullArgument_RequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestHeaders(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullArgument_ResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setResponseHeaders(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetters_NullArgument_RequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestParameters(null);
    }

    @Test
    public void testEquals_SameObject() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;
        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentTimeStamp() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(2L);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentLoggerContext() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        accessEvent1.setLoggerContext(loggerContext1);
        accessEvent2.setLoggerContext(loggerContext2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentMethod() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setMethod("method1");
        accessEvent2.setMethod("method2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentProtocol() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setProtocol("protocol1");
        accessEvent2.setProtocol("protocol2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentRemoteHost() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteHost("remoteHost1");
        accessEvent2.setRemoteHost("remoteHost2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentRemoteUser() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteUser("remoteUser1");
        accessEvent2.setRemoteUser("remoteUser2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentRequestHeaders() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent2.setRequestHeaders(requestHeaders2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentRequestURI() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURI("requestURI1");
        accessEvent2.setRequestURI("requestURI2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentRequestURL() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURL("requestURL1");
        accessEvent2.setRequestURL("requestURL2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentResponseHeaders() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent2.setResponseHeaders(responseHeaders2);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEquals_DifferentObjects_DifferentServerName() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setServerName("serverName1");
        accessEvent2.setServerName("serverName2");
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode_SameObjects() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;
        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentTimeStamp() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(2L);
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentLoggerContext() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        accessEvent1.setLoggerContext(loggerContext1);
        accessEvent2.setLoggerContext(loggerContext2);
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentMethod() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setMethod("method1");
        accessEvent2.setMethod("method2");
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentProtocol() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setProtocol("protocol1");
        accessEvent2.setProtocol("protocol2");
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentRemoteHost() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteHost("remoteHost1");
        accessEvent2.setRemoteHost("remoteHost2");
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentRemoteUser() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRemoteUser("remoteUser1");
        accessEvent2.setRemoteUser("remoteUser2");
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentRequestHeaders() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent2.setRequestHeaders(requestHeaders2);
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentRequestURI() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURI("requestURI1");
        accessEvent2.setRequestURI("requestURI2");
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentRequestURL() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setRequestURL("requestURL1");
        accessEvent2.setRequestURL("requestURL2");
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentResponseHeaders() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent2.setResponseHeaders(responseHeaders2);
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_DifferentServerName() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setServerName("serverName1");
        accessEvent2.setServerName("serverName2");
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        assertEquals("AccessEvent[loggerContext=null, timeStamp=null]", accessEvent.toString());
    }
}