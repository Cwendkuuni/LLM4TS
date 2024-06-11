package de.huxhorn.lilith.data.access.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    private AccessEvent accessEvent;

    @Before
    public void setup() {
        this.accessEvent = new AccessEvent();
        this.accessEvent.setTimeStamp(System.currentTimeMillis());
        this.accessEvent.setLoggerContext(new LoggerContext());
        this.accessEvent.setRequestURI("/test");
        this.accessEvent.setRequestURL("http://test.com/test");
        this.accessEvent.setRemoteHost("127.0.0.1");
        this.accessEvent.setRemoteUser("testUser");
        this.accessEvent.setProtocol("HTTP/1.1");
        this.accessEvent.setMethod("GET");
        this.accessEvent.setServerName("testServer.com");
        this.accessEvent.setRemoteAddress("127.0.0.1");
        this.accessEvent.setRequestHeaders(new HashMap<>());
        this.accessEvent.setResponseHeaders(new HashMap<>());
        this.accessEvent.setRequestParameters(new HashMap<>());
        this.accessEvent.setLocalPort(8080);
        this.accessEvent.setStatusCode(200);
    }

    @Test
    public void testGettersAndSetters() {
        // Test timeStamp
        Long expectedTimeStamp = this.accessEvent.getTimeStamp();
        this.accessEvent.setTimeStamp(expectedTimeStamp + 1);
        Long newExpectedTimeStamp = this.accessEvent.getTimeStamp();
        assertEquals(expectedTimeStamp + 1, newExpectedTimeStamp);

        // Test loggerContext
        LoggerContext loggerContext = new LoggerContext();
        this.accessEvent.setLoggerContext(loggerContext);
        LoggerContext retrievedLoggerContext = this.accessEvent.getLoggerContext();
        assertEquals(loggerContext, retrievedLoggerContext);

        // Test requestURI
        String expectedRequestURI = "/test";
        this.accessEvent.setRequestURI("/newURI");
        String newExpectedRequestURI = this.accessEvent.getRequestURI();
        assertEquals(expectedRequestURI, "/test");
        assertEquals(newExpectedRequestURI, "/newURI");

        // Test requestURL
        String expectedRequestURL = "http://test.com/test";
        this.accessEvent.setRequestURL("http://new.com/newTest");
        String newExpectedRequestURL = this.accessEvent.getRequestURL();
        assertEquals(expectedRequestURL, "http://test.com/test");
        assertEquals(newExpectedRequestURL, "http://new.com/newTest");

        // Test remoteHost
        String expectedRemoteHost = "127.0.0.1";
        this.accessEvent.setRemoteHost("192.168.0.1");
        String newExpectedRemoteHost = this.accessEvent.getRemoteHost();
        assertEquals(expectedRemoteHost, "127.0.0.1");
        assertEquals(newExpectedRemoteHost, "192.168.0.1");

        // Test remoteUser
        String expectedRemoteUser = "testUser";
        this.accessEvent.setRemoteUser("newTestUser");
        String newExpectedRemoteUser = this.accessEvent.getRemoteUser();
        assertEquals(expectedRemoteUser, "testUser");
        assertEquals(newExpectedRemoteUser, "newTestUser");

        // Test protocol
        String expectedProtocol = "HTTP/1.1";
        this.accessEvent.setProtocol("HTTP/1.2");
        String newExpectedProtocol = this.accessEvent.getProtocol();
        assertEquals(expectedProtocol, "HTTP/1.1");
        assertEquals(newExpectedProtocol, "HTTP/1.2");

        // Test method
        String expectedMethod = "GET";
        this.accessEvent.setMethod("POST");
        String newExpectedMethod = this.accessEvent.getMethod();
        assertEquals(expectedMethod, "GET");
        assertEquals(newExpectedMethod, "POST");

        // Test serverName
        String expectedServerName = "testServer.com";
        this.accessEvent.setServerName("newTestServer.com");
        String newExpectedServerName = this.accessEvent.getServerName();
        assertEquals(expectedServerName, "testServer.com");
        assertEquals(newExpectedServerName, "newTestServer.com");

        // Test remoteAddress
        String expectedRemoteAddress = "127.0.0.1";
        this.accessEvent.setRemoteAddress("192.168.0.1");
        String newExpectedRemoteAddress = this.accessEvent.getRemoteAddress();
        assertEquals(expectedRemoteAddress, "127.0.0.1");
        assertEquals(newExpectedRemoteAddress, "192.168.0.1");

        // Test requestHeaders
        Map<String, String> expectedRequestHeaders = new HashMap<>();
        this.accessEvent.setRequestHeaders(expectedRequestHeaders);
        Map<String, String> retrievedRequestHeaders = this.accessEvent.getRequestHeaders();
        assertEquals(expectedRequestHeaders, retrievedRequestHeaders);

        // Test responseHeaders
        Map<String, String> expectedResponseHeaders = new HashMap<>();
        this.accessEvent.setResponseHeaders(expectedResponseHeaders);
        Map<String, String> retrievedResponseHeaders = this.accessEvent.getResponseHeaders();
        assertEquals(expectedResponseHeaders, retrievedResponseHeaders);

        // Test requestParameters
        Map<String, String[]> expectedRequestParameters = new HashMap<>();
        this.accessEvent.setRequestParameters(expectedRequestParameters);
        Map<String, String[]> retrievedRequestParameters = this.accessEvent.getRequestParameters();
        assertEquals(expectedRequestParameters, retrievedRequestParameters);

        // Test localPort
        int expectedLocalPort = 8080;
        this.accessEvent.setLocalPort(8081);
        int newExpectedLocalPort = this.accessEvent.getLocalPort();
        assertEquals(expectedLocalPort, 8080);
        assertEquals(newExpectedLocalPort, 8081);

        // Test statusCode
        int expectedStatusCode = 200;
        this.accessEvent.setStatusCode(201);
        int newExpectedStatusCode = this.accessEvent.getStatusCode();
        assertEquals(expectedStatusCode, 200);
        assertEquals(newExpectedStatusCode, 201);
    }

    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        AccessEvent accessEvent3 = new AccessEvent();

        // Test when both objects are the same
        assertTrue(accessEvent1.equals(accessEvent1));

        // Test when first object is equal to second object
        accessEvent1.setTimeStamp(System.currentTimeMillis());
        accessEvent2.setTimeStamp(System.currentTimeMillis());
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/test");
        accessEvent2.setRequestURI("/test");
        accessEvent1.setRequestURL("http://test.com/test");
        accessEvent2.setRequestURL("http://test.com/test");
        accessEvent1.setRemoteHost("127.0.0.1");
        accessEvent2.setRemoteHost("127.0.0.1");
        accessEvent1.setRemoteUser("testUser");
        accessEvent2.setRemoteUser("testUser");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent2.setMethod("GET");
        accessEvent1.setServerName("testServer.com");
        accessEvent2.setServerName("testServer.com");
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent2.setRemoteAddress("127.0.0.1");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(8080);
        accessEvent2.setLocalPort(8080);
        accessEvent1.setStatusCode(200);
        accessEvent2.setStatusCode(200);

        assertTrue(accessEvent1.equals(accessEvent2));

        // Test when first and second objects have different localPort
        accessEvent1.setLocalPort(8081);
        accessEvent2.setLocalPort(8080);
        assertFalse(accessEvent1.equals(accessEvent2));

        // Test when first and second objects have different statusCode
        accessEvent1.setStatusCode(201);
        accessEvent2.setStatusCode(200);
        assertFalse(accessEvent1.equals(accessEvent2));

        // Test when first object is null
        accessEvent1 = null;
        assertFalse(accessEvent1.equals(accessEvent2));

        // Test when second object is null
        accessEvent2 = null;
        assertFalse(accessEvent1.equals(accessEvent2));

        // Test when first object is not an instance of AccessEvent
        Object notAccessEvent = new Object();
        assertFalse(accessEvent1.equals(notAccessEvent));
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        // Test when both objects have the same fields
        accessEvent1.setTimeStamp(System.currentTimeMillis());
        accessEvent2.setTimeStamp(System.currentTimeMillis());
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/test");
        accessEvent2.setRequestURI("/test");
        accessEvent1.setRequestURL("http://test.com/test");
        accessEvent2.setRequestURL("http://test.com/test");
        accessEvent1.setRemoteHost("127.0.0.1");
        accessEvent2.setRemoteHost("127.0.0.1");
        accessEvent1.setRemoteUser("testUser");
        accessEvent2.setRemoteUser("testUser");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent2.setMethod("GET");
        accessEvent1.setServerName("testServer.com");
        accessEvent2.setServerName("testServer.com");
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent2.setRemoteAddress("127.0.0.1");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(8080);
        accessEvent2.setLocalPort(8080);
        accessEvent1.setStatusCode(200);
        accessEvent2.setStatusCode(200);

        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());

        // Test when first and second objects have different localPort
        accessEvent1.setLocalPort(8081);
        accessEvent2.setLocalPort(8080);
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());

        // Test when first and second objects have different statusCode
        accessEvent1.setStatusCode(201);
        accessEvent2.setStatusCode(200);
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());

        // Test when first object is null
        accessEvent1 = null;
        assertNull(accessEvent1.hashCode());

        // Test when second object is null
        accessEvent2 = null;
        assertNull(accessEvent2.hashCode());
    }
}