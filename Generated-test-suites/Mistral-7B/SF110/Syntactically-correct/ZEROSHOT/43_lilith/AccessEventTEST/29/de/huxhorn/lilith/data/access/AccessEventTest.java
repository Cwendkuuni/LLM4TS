package de.huxhorn.lilith.data.access;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

    @Test
    public void testAccessEvent() {
        // Create an AccessEvent object with default values and test getters
        AccessEvent accessEvent = new AccessEvent();
        Assert.assertNull(accessEvent.getTimeStamp());
        Assert.assertNull(accessEvent.getLoggerContext());
        Assert.assertNull(accessEvent.getRequestURI());
        Assert.assertNull(accessEvent.getRequestURL());
        Assert.assertNull(accessEvent.getRemoteHost());
        Assert.assertNull(accessEvent.getRemoteUser());
        Assert.assertNull(accessEvent.getProtocol());
        Assert.assertNull(accessEvent.getMethod());
        Assert.assertNull(accessEvent.getServerName());
        Assert.assertNull(accessEvent.getRemoteAddress());
        Assert.assertNull(accessEvent.getRequestHeaders());
        Assert.assertNull(accessEvent.getResponseHeaders());
        Assert.assertNull(accessEvent.getRequestParameters());
        Assert.assertEquals(0, accessEvent.getLocalPort());
        Assert.assertEquals(0, accessEvent.getStatusCode());

        // Create an AccessEvent object with custom values and test getters
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testuser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testserver.com";
        String remoteAddress = "192.168.1.1";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
        int statusCode = 200;

        AccessEvent accessEventCustom = new AccessEvent();
        accessEventCustom.setTimeStamp(timeStamp);
        accessEventCustom.setLoggerContext(loggerContext);
        accessEventCustom.setRequestURI(requestURI);
        accessEventCustom.setRequestURL(requestURL);
        accessEventCustom.setRemoteHost(remoteHost);
        accessEventCustom.setRemoteUser(remoteUser);
        accessEventCustom.setProtocol(protocol);
        accessEventCustom.setMethod(method);
        accessEventCustom.setServerName(serverName);
        accessEventCustom.setRemoteAddress(remoteAddress);
        accessEventCustom.setRequestHeaders(requestHeaders);
        accessEventCustom.setResponseHeaders(responseHeaders);
        accessEventCustom.setRequestParameters(requestParameters);
        accessEventCustom.setLocalPort(localPort);
        accessEventCustom.setStatusCode(statusCode);

        Assert.assertEquals(timeStamp, accessEventCustom.getTimeStamp());
        Assert.assertEquals(loggerContext, accessEventCustom.getLoggerContext());
        Assert.assertEquals(requestURI, accessEventCustom.getRequestURI());
        Assert.assertEquals(requestURL, accessEventCustom.getRequestURL());
        Assert.assertEquals(remoteHost, accessEventCustom.getRemoteHost());
        Assert.assertEquals(remoteUser, accessEventCustom.getRemoteUser());
        Assert.assertEquals(protocol, accessEventCustom.getProtocol());
        Assert.assertEquals(method, accessEventCustom.getMethod());
        Assert.assertEquals(serverName, accessEventCustom.getServerName());
        Assert.assertEquals(remoteAddress, accessEventCustom.getRemoteAddress());
        Assert.assertEquals(requestHeaders, accessEventCustom.getRequestHeaders());
        Assert.assertEquals(responseHeaders, accessEventCustom.getResponseHeaders());
        Assert.assertEquals(requestParameters, accessEventCustom.getRequestParameters());
        Assert.assertEquals(localPort, accessEventCustom.getLocalPort());
        Assert.assertEquals(statusCode, accessEventCustom.getStatusCode());

        // Test equals method
        AccessEvent accessEventEqual = new AccessEvent();
        accessEventEqual.setTimeStamp(timeStamp);
        accessEventEqual.setLoggerContext(loggerContext);
        accessEventEqual.setRequestURI(requestURI);
        accessEventEqual.setRequestURL(requestURL);
        accessEventEqual.setRemoteHost(remoteHost);
        accessEventEqual.setRemoteUser(remoteUser);
        accessEventEqual.setProtocol(protocol);
        accessEventEqual.setMethod(method);
        accessEventEqual.setServerName(serverName);
        accessEventEqual.setRemoteAddress(remoteAddress);
        accessEventEqual.setRequestHeaders(requestHeaders);
        accessEventEqual.setResponseHeaders(responseHeaders);
        accessEventEqual.setRequestParameters(requestParameters);
        accessEventEqual.setLocalPort(localPort);
        accessEventEqual.setStatusCode(statusCode);

        Assert.assertTrue(accessEventCustom.equals(accessEventEqual));

        // Test not equals method with different values
        AccessEvent accessEventNotEqual1 = new AccessEvent();
        accessEventNotEqual1.setTimeStamp(timeStamp + 1);
        AccessEvent accessEventNotEqual2 = new AccessEvent();
        accessEventNotEqual2.setLoggerContext(new LoggerContext());

        Assert.assertFalse(accessEventCustom.equals(accessEventNotEqual1));
        Assert.assertFalse(accessEventCustom.equals(accessEventNotEqual2));

        // Test hashCode method
        int hashCodeCustom = accessEventCustom.hashCode();
        int hashCodeDefault = accessEvent.hashCode();
        int hashCodeEqual = accessEventEqual.hashCode();

        Assert.assertNotEquals(hashCodeCustom, hashCodeDefault);
        Assert.assertEquals(hashCodeCustom, hashCodeEqual);

        // Test toString method
        String toStringCustom = accessEventCustom.toString();
        String toStringDefault = accessEvent.toString();

        Assert.assertNotEquals(toStringCustom, toStringDefault);
    }

    @Test
    public void testSettersWithNullValues() {
        AccessEvent accessEvent = new AccessEvent();

        // Test setting null values for all fields except localPort and statusCode
        accessEvent.setTimeStamp(null);
        accessEvent.setLoggerContext(null);
        accessEvent.setRequestURI(null);
        accessEvent.setRequestURL(null);
        accessEvent.setRemoteHost(null);
        accessEvent.setRemoteUser(null);
        accessEvent.setProtocol(null);
        accessEvent.setMethod(null);
        accessEvent.setServerName(null);
        accessEvent.setRemoteAddress(null);
        accessEvent.setRequestHeaders(null);
        accessEvent.setResponseHeaders(null);
        accessEvent.setRequestParameters(null);

        // Test getters for fields with null values
        Assert.assertNull(accessEvent.getTimeStamp());
        Assert.assertNull(accessEvent.getLoggerContext());
        Assert.assertNull(accessEvent.getRequestURI());
        Assert.assertNull(accessEvent.getRequestURL());
        Assert.assertNull(accessEvent.getRemoteHost());
        Assert.assertNull(accessEvent.getRemoteUser());
        Assert.assertNull(accessEvent.getProtocol());
        Assert.assertNull(accessEvent.getMethod());
        Assert.assertNull(accessEvent.getServerName());
        Assert.assertNull(accessEvent.getRemoteAddress());
        Assert.assertNull(accessEvent.getRequestHeaders());
        Assert.assertNull(accessEvent.getResponseHeaders());
        Assert.assertNull(accessEvent.getRequestParameters());

        // Test setting null values for localPort and statusCode
        accessEvent.setLocalPort(-1);
        accessEvent.setStatusCode(-1);

        // Test getters for fields with null values
        Assert.assertEquals(-1, accessEvent.getLocalPort());
        Assert.assertEquals(-1, accessEvent.getStatusCode());
    }

    @Test
    public void testSettersWithEmptyMaps() {
        AccessEvent accessEvent = new AccessEvent();

        // Test setting empty maps for requestHeaders and responseHeaders
        Map<String, String> emptyRequestHeaders = new HashMap<>();
        Map<String, String> emptyResponseHeaders = new HashMap<>();

        accessEvent.setRequestHeaders(emptyRequestHeaders);
        accessEvent.setResponseHeaders(emptyResponseHeaders);

        // Test getters for empty maps
        Assert.assertEquals(emptyRequestHeaders, accessEvent.getRequestHeaders());
        Assert.assertEquals(emptyResponseHeaders, accessEvent.getResponseHeaders());

        // Test setting empty array for requestParameters
        String[] emptyRequestParameters = new String[0];
        accessEvent.setRequestParameters(Arrays.asList(emptyRequestParameters));

        // Test getters for empty array
        Assert.assertEquals(Arrays.asList(emptyRequestParameters), accessEvent.getRequestParameters().values());
    }
}