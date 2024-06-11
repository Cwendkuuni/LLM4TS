package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    private AccessEvent accessEvent;
    private LoggerContext loggerContextMock;

    @Before
    public void setup() {
        loggerContextMock = mock(LoggerContext.class);
        accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(loggerContextMock);
    }

    @Test
    public void testGetTimeStamp() {
        Long testTimeStamp = 1L;
        accessEvent.setTimeStamp(testTimeStamp);
        Long result = accessEvent.getTimeStamp();
        assertEquals(testTimeStamp, result);
    }

    @Test
    public void testSetTimeStamp() {
        Long testTimeStamp = 1L;
        accessEvent.setTimeStamp(null);
        accessEvent.setTimeStamp(testTimeStamp);
        Long result = accessEvent.getTimeStamp();
        assertEquals(testTimeStamp, result);
    }

    @Test
    public void testGetLoggerContext() {
        assertSame(loggerContextMock, accessEvent.getLoggerContext());
    }

    @Test
    public void testSetLoggerContext() {
        assertNull(accessEvent.getLoggerContext());
        LoggerContext newLoggerContext = new LoggerContext();
        accessEvent.setLoggerContext(newLoggerContext);
        assertSame(newLoggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        String testRequestURI = "testRequestURI";
        accessEvent.setRequestURI(testRequestURI);
        String result = accessEvent.getRequestURI();
        assertEquals(testRequestURI, result);
    }

    @Test
    public void testSetRequestURI() {
        String testRequestURI = "testRequestURI";
        accessEvent.setRequestURI(null);
        accessEvent.setRequestURI(testRequestURI);
        String result = accessEvent.getRequestURI();
        assertEquals(testRequestURI, result);
    }

    @Test
    public void testGetRequestURL() {
        String testRequestURL = "testRequestURL";
        accessEvent.setRequestURL(testRequestURL);
        String result = accessEvent.getRequestURL();
        assertEquals(testRequestURL, result);
    }

    @Test
    public void testSetRequestURL() {
        String testRequestURL = "testRequestURL";
        accessEvent.setRequestURL(null);
        accessEvent.setRequestURL(testRequestURL);
        String result = accessEvent.getRequestURL();
        assertEquals(testRequestURL, result);
    }

    @Test
    public void testGetRemoteHost() {
        String testRemoteHost = "testRemoteHost";
        accessEvent.setRemoteHost(testRemoteHost);
        String result = accessEvent.getRemoteHost();
        assertEquals(testRemoteHost, result);
    }

    @Test
    public void testSetRemoteHost() {
        String testRemoteHost = "testRemoteHost";
        accessEvent.setRemoteHost(null);
        accessEvent.setRemoteHost(testRemoteHost);
        String result = accessEvent.getRemoteHost();
        assertEquals(testRemoteHost, result);
    }

    @Test
    public void testGetRemoteUser() {
        String testRemoteUser = "testRemoteUser";
        accessEvent.setRemoteUser(testRemoteUser);
        String result = accessEvent.getRemoteUser();
        assertEquals(testRemoteUser, result);
    }

    @Test
    public void testSetRemoteUser() {
        String testRemoteUser = "testRemoteUser";
        accessEvent.setRemoteUser(null);
        accessEvent.setRemoteUser(testRemoteUser);
        String result = accessEvent.getRemoteUser();
        assertEquals(testRemoteUser, result);
    }

    @Test
    public void testGetProtocol() {
        String testProtocol = "testProtocol";
        accessEvent.setProtocol(testProtocol);
        String result = accessEvent.getProtocol();
        assertEquals(testProtocol, result);
    }

    @Test
    public void testSetProtocol() {
        String testProtocol = "testProtocol";
        accessEvent.setProtocol(null);
        accessEvent.setProtocol(testProtocol);
        String result = accessEvent.getProtocol();
        assertEquals(testProtocol, result);
    }

    @Test
    public void testGetMethod() {
        String testMethod = "testMethod";
        accessEvent.setMethod(testMethod);
        String result = accessEvent.getMethod();
        assertEquals(testMethod, result);
    }

    @Test
    public void testSetMethod() {
        String testMethod = "testMethod";
        accessEvent.setMethod(null);
        accessEvent.setMethod(testMethod);
        String result = accessEvent.getMethod();
        assertEquals(testMethod, result);
    }

    @Test
    public void testGetServerName() {
        String testServerName = "testServerName";
        accessEvent.setServerName(testServerName);
        String result = accessEvent.getServerName();
        assertEquals(testServerName, result);
    }

    @Test
    public void testSetServerName() {
        String testServerName = "testServerName";
        accessEvent.setServerName(null);
        accessEvent.setServerName(testServerName);
        String result = accessEvent.getServerName();
        assertEquals(testServerName, result);
    }

    @Test
    public void testGetRemoteAddress() {
        String testRemoteAddress = "testRemoteAddress";
        accessEvent.setRemoteAddress(testRemoteAddress);
        String result = accessEvent.getRemoteAddress();
        assertEquals(testRemoteAddress, result);
    }

    @Test
    public void testSetRemoteAddress() {
        String testRemoteAddress = "testRemoteAddress";
        accessEvent.setRemoteAddress(null);
        accessEvent.setRemoteAddress(testRemoteAddress);
        String result = accessEvent.getRemoteAddress();
        assertEquals(testRemoteAddress, result);
    }

    @Test
    public void testGetRequestHeaders() {
        Map<String, String> testRequestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(testRequestHeaders);
        Map<String, String> result = accessEvent.getRequestHeaders();
        assertEquals(testRequestHeaders, result);
    }

    @Test
    public void testSetRequestHeaders() {
        Map<String, String> testRequestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(null);
        accessEvent.setRequestHeaders(testRequestHeaders);
        Map<String, String> result = accessEvent.getRequestHeaders();
        assertEquals(testRequestHeaders, result);
    }

    @Test
    public void testGetResponseHeaders() {
        Map<String, String> testResponseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(testResponseHeaders);
        Map<String, String> result = accessEvent.getResponseHeaders();
        assertEquals(testResponseHeaders, result);
    }

    @Test
    public void testSetResponseHeaders() {
        Map<String, String> testResponseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(null);
        accessEvent.setResponseHeaders(testResponseHeaders);
        Map<String, String> result = accessEvent.getResponseHeaders();
        assertEquals(testResponseHeaders, result);
    }

    @Test
    public void testGetRequestParameters() {
        Map<String, String[]> testRequestParameters = new HashMap<>();
        accessEvent.setRequestParameters(testRequestParameters);
        Map<String, String[]> result = accessEvent.getRequestParameters();
        assertEquals(testRequestParameters, result);
    }

    @Test
    public void testSetRequestParameters() {
        Map<String, String[]> testRequestParameters = new HashMap<>();
        accessEvent.setRequestParameters(null);
        accessEvent.setRequestParameters(testRequestParameters);
        Map<String, String[]> result = accessEvent.getRequestParameters();
        assertEquals(testRequestParameters, result);
    }

    @Test
    public void testGetLocalPort() {
        int testLocalPort = 1;
        accessEvent.setLocalPort(testLocalPort);
        int result = accessEvent.getLocalPort();
        assertEquals(testLocalPort, result);
    }

    @Test
    public void testSetLocalPort() {
        int testLocalPort = 1;
        accessEvent.setLocalPort(0);
        accessEvent.setLocalPort(testLocalPort);
        int result = accessEvent.getLocalPort();
        assertEquals(testLocalPort, result);
    }

    @Test
    public void testGetStatusCode() {
        int testStatusCode = 1;
        accessEvent.setStatusCode(testStatusCode);
        int result = accessEvent.getStatusCode();
        assertEquals(testStatusCode, result);
    }

    @Test
    public void testSetStatusCode() {
        int testStatusCode = 1;
        accessEvent.setStatusCode(0);
        accessEvent.setStatusCode(testStatusCode);
        int result = accessEvent.getStatusCode();
        assertEquals(testStatusCode, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLoggerContext() {
        accessEvent.setLoggerContext(null);
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();
        when(loggerContextMock.equals(loggerContextMock)).thenReturn(true);

        event1.setLoggerContext(loggerContextMock);
        event1.setTimeStamp(1L);
        event2.setLoggerContext(loggerContextMock);
        event2.setTimeStamp(1L);
        event3.setLoggerContext(loggerContextMock);
        event3.setTimeStamp(2L);

        accessEvent.setLoggerContext(loggerContextMock);
        accessEvent.setTimeStamp(1L);

        boolean result1 = event1.equals(event1);
        assertTrue(result1);

        boolean result2 = event1.equals(event2);
        assertTrue(result2);

        boolean result3 = event1.equals(event3);
        assertFalse(result3);

        boolean result4 = event1.equals(accessEvent);
        assertTrue(result4);
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();

        event1.setLoggerContext(loggerContextMock);
        event1.setTimeStamp(1L);
        event2.setLoggerContext(loggerContextMock);
        event2.setTimeStamp(1L);
        event3.setLoggerContext(loggerContextMock);
        event3.setTimeStamp(2L);

        int result1 = event1.hashCode();
        int result2 = event1.hashCode();

        accessEvent.setLoggerContext(loggerContextMock);
        accessEvent.setTimeStamp(1L);

        int testHashCode = event1.hashCode();

        int result3 = event1.hashCode();
        assertEquals(testHashCode, result3);

        int result4 = event2.hashCode();
        assertNotEquals(testHashCode, result4);

        int result5 = event3.hashCode();
        assertNotEquals(testHashCode, result5);
    }
}