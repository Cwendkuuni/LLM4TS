package de.huxhorn.lilith.data.access.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;

public class AccessEventTest {

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        // Test when both objects are equal
        event1.setLocalPort(8080);
        event1.setStatusCode(200);
        event2.setLocalPort(8080);
        event2.setStatusCode(200);
        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        Map<String, String> headers1 = new HashMap<>();
        Map<String, String> headers2 = new HashMap<>();
        event1.setRequestHeaders(headers1);
        event2.setRequestHeaders(headers2);
        Map<String, String[]> params1 = new HashMap<>();
        Map<String, String[]> params2 = new HashMap<>();
        event1.setRequestParameters(params1);
        event2.setRequestParameters(params2);
        event1.setMethod("GET");
        event2.setMethod("GET");
        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        event1.setRequestURI("/test-request");
        event2.setRequestURI("/test-request");
        event1.setRequestURL("http://test.com/test-request");
        event2.setRequestURL("http://test.com/test-request");
        event1.setRemoteAddress("127.0.0.1");
        event2.setRemoteAddress("127.0.0.1");
        event1.setRemoteHost("localhost");
        event2.setRemoteHost("localhost");
        event1.setRemoteUser("testUser");
        event2.setRemoteUser("testUser");
        event1.setServerName("testServer");
        event2.setServerName("testServer");
        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);

        assertTrue("Both AccessEvent objects should be equal", event1.equals(event2));

        // Test when objects are not equal due to localPort
        event2.setLocalPort(8081);
        assertFalse("AccessEvent objects should not be equal when localPort is different", event1.equals(event2));

        // Test when objects are not equal due to statusCode
        event2.setStatusCode(201);
        assertFalse("AccessEvent objects should not be equal when statusCode is different", event1.equals(event2));

        // Test when one object has null loggerContext while the other does not
        event2.setLoggerContext(null);
        assertFalse("AccessEvent objects should not be equal when one has null loggerContext", event1.equals(event2));

        // Test when both objects have different methods
        event1.setMethod("POST");
        assertFalse("AccessEvent objects should not be equal when methods are different", event1.equals(event2));

        // Test when both objects have different protocols
        event1.setProtocol("HTTP/1.0");
        assertFalse("AccessEvent objects should not be equal when protocols are different", event1.equals(event2));

        // Test when both objects have different remoteAddresses
        event1.setRemoteAddress("192.168.1.1");
        assertFalse("AccessEvent objects should not be equal when remoteAddresses are different", event1.equals(event2));

        // Test when both objects have different remoteHosts
        event1.setRemoteHost("192.168.1.2");
        assertFalse("AccessEvent objects should not be equal when remoteHosts are different", event1.equals(event2));

        // Test when both objects have different remoteUsers
        event1.setRemoteUser("user1");
        assertFalse("AccessEvent objects should not be equal when remoteUsers are different", event1.equals(event2));

        // Test when both objects have different requestHeaders
        Map<String, String> headers3 = new HashMap<>(headers1);
        headers3.put("X-Custom-Header", "test");
        event2.setRequestHeaders(headers3);
        assertFalse("AccessEvent objects should not be equal when requestHeaders are different", event1.equals(event2));

        // Test when both objects have different requestURIs
        event1.setRequestURI("/test-request2");
        assertFalse("AccessEvent objects should not be equal when requestURIs are different", event1.equals(event2));

        // Test when both objects have different requestURLs
        event1.setRequestURL("http://test.com/test-request2");
        assertFalse("AccessEvent objects should not be equal when requestURLs are different", event1.equals(event2));

        // Test when both objects have different responseHeaders
        Map<String, String> headers4 = new HashMap<>(headers2);
        headers4.put("X-Custom-Header", "test");
        event2.setResponseHeaders(headers4);
        assertFalse("AccessEvent objects should not be equal when responseHeaders are different", event1.equals(event2));

        // Test when both objects have different serverNames
        event1.setServerName("testServer2");
        assertFalse("AccessEvent objects should not be equal when serverNames are different", event1.equals(event2));

        // Test when both objects have different timeStamps
        event2.setTimeStamp(2L);
        assertFalse("AccessEvent objects should not be equal when timeStamps are different", event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setLocalPort(8080);
        event1.setStatusCode(200);
        event1.setLoggerContext(new LoggerContext());
        Map<String, String> headers1 = new HashMap<>();
        event1.setRequestHeaders(headers1);
        Map<String, String[]> params1 = new HashMap<>();
        event1.setRequestParameters(params1);
        event1.setMethod("GET");
        event1.setProtocol("HTTP/1.1");
        event1.setRequestURI("/test-request");
        event1.setRequestURL("http://test.com/test-request");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRemoteHost("localhost");
        event1.setRemoteUser("testUser");
        event1.setServerName("testServer");
        event1.setTimeStamp(1L);

        event2.setLocalPort(8081);
        event2.setStatusCode(201);
        event2.setLoggerContext(null);
        Map<String, String> headers2 = new HashMap<>();
        event2.setRequestHeaders(headers2);
        Map<String, String[]> params2 = new HashMap<>();
        event2.setRequestParameters(params2);
        event2.setMethod("POST");
        event2.setProtocol("HTTP/1.0");
        event2.setRequestURI("/test-request2");
        event2.setRequestURL("http://test.com/test-request2");
        event2.setRemoteAddress("192.168.1.1");
        event2.setRemoteHost("192.168.1.2");
        event2.setRemoteUser("user2");
        event2.setServerName("testServer2");
        event2.setTimeStamp(2L);

        int expectedHashCode1 = 1 * 31 + event1.loggerContext.hashCode() + event1.requestURI.hashCode() + event1.requestURL.hashCode() + event1.remoteHost.hashCode() + event1.remoteUser.hashCode() + event1.protocol.hashCode() + event1.method.hashCode() + event1.serverName.hashCode() + event1.remoteAddress.hashCode() + event1.localPort + event1.statusCode;
        int expectedHashCode2 = 2 * 31 + Arrays.hashCode(event2.requestParameters.values().toArray()) + event2.responseHeaders.hashCode() + event2.serverName.hashCode() + event2.remoteAddress.hashCode() + event2.remoteHost.hashCode() + event2.remoteUser.hashCode() + event2.protocol.hashCode() + event2.method.hashCode() + event2.loggerContext.hashCode() + event2.requestURI.hashCode() + event2.requestURL.hashCode() + event2.localPort + event2.statusCode;

        int actualHashCode1 = event1.hashCode();
        int actualHashCode2 = event2.hashCode();

        assertEquals("Both AccessEvent objects should have different hashcodes", expectedHashCode1, actualHashCode1);
        assertEquals("Both AccessEvent objects should have different hashcodes", expectedHashCode2, actualHashCode2);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        event.setTimeStamp(1L);

        String expectedToString = "AccessEvent[loggerContext=LoggerContext{}, timeStamp=1]";
        String actualToString = event.toString();

        assertEquals("toString() should return the correct representation of the AccessEvent object", expectedToString, actualToString);
    }
}