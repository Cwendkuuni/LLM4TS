import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;

public class AccessEventTest {

    private AccessEvent accessEvent;
    private LoggerContext loggerContext;

    @Before
    public void setUp() {
        loggerContext = mock(LoggerContext.class);
        accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(loggerContext);

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("header1", "value1");
        accessEvent.setRequestHeaders(requestHeaders);

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("header1", "value1");
        accessEvent.setResponseHeaders(responseHeaders);

        Map<String, String[]> requestParameters = new HashMap<>();
        accessEvent.setRequestParameters(requestParameters);
    }

    @Test
    public void testGettersAndSetters_TimeStamp() {
        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGettersAndSetters_LoggerContext() {
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_RequestURI() {
        String requestURI = "/test";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_RequestURL() {
        String requestURL = "http://test.com/test";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_RemoteHost() {
        String remoteHost = "127.0.0.1";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_RemoteUser() {
        String remoteUser = "testuser";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_Protocol() {
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGettersAndSetters_Method() {
        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGettersAndSetters_ServerName() {
        String serverName = "test.com";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGettersAndSetters_RemoteAddress() {
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_RequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("header1", "value1");
        accessEvent.setRequestHeaders(requestHeaders);
        Map<String, String> actualRequestHeaders = accessEvent.getRequestHeaders();
        assertEquals(requestHeaders, actualRequestHeaders);
    }

    @Test
    public void testGettersAndSetters_ResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("header1", "value1");
        accessEvent.setResponseHeaders(responseHeaders);
        Map<String, String> actualResponseHeaders = accessEvent.getResponseHeaders();
        assertEquals(responseHeaders, actualResponseHeaders);
    }

    @Test
    public void testGettersAndSetters_RequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        accessEvent.setRequestParameters(requestParameters);
        Map<String, String[]> actualRequestParameters = accessEvent.getRequestParameters();
        assertEquals(requestParameters, actualRequestParameters);
    }

    @Test
    public void testGettersAndSetters_LocalPort() {
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_StatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        AccessEvent accessEvent3 = new AccessEvent();

        accessEvent1.setLoggerContext(loggerContext);
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setMethod("GET");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setRemoteHost("127.0.0.1");
        accessEvent1.setRemoteUser("testuser");
        accessEvent1.setServerName("test.com");
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent1.setRequestHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent1.setResponseHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent1.setRequestParameters(Collections.emptyMap());
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        accessEvent2.setLoggerContext(loggerContext);
        accessEvent2.setTimeStamp(1L);
        accessEvent2.setMethod("GET");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent2.setRemoteHost("127.0.0.1");
        accessEvent2.setRemoteUser("testuser");
        accessEvent2.setServerName("test.com");
        accessEvent2.setRemoteAddress("127.0.0.1");
        accessEvent2.setRequestHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent2.setResponseHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent2.setRequestParameters(Collections.emptyMap());
        accessEvent2.setLocalPort(8080);
        accessEvent2.setStatusCode(200);

        assertTrue(accessEvent1.equals(accessEvent2));

        accessEvent3.setLoggerContext(loggerContext);
        accessEvent3.setTimeStamp(2L);
        accessEvent3.setMethod("GET");
        accessEvent3.setProtocol("HTTP/1.1");
        accessEvent3.setRemoteHost("127.0.0.1");
        accessEvent3.setRemoteUser("testuser");
        accessEvent3.setServerName("test.com");
        accessEvent3.setRemoteAddress("127.0.0.1");
        accessEvent3.setRequestHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent3.setResponseHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent3.setRequestParameters(Collections.emptyMap());
        accessEvent3.setLocalPort(8080);
        accessEvent3.setStatusCode(200);

        assertFalse(accessEvent1.equals(accessEvent3));

        accessEvent3.setTimeStamp(1L);

        assertTrue(accessEvent1.equals(accessEvent3));
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        AccessEvent accessEvent3 = new AccessEvent();

        accessEvent1.setLoggerContext(loggerContext);
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setMethod("GET");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setRemoteHost("127.0.0.1");
        accessEvent1.setRemoteUser("testuser");
        accessEvent1.setServerName("test.com");
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent1.setRequestHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent1.setResponseHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent1.setRequestParameters(Collections.emptyMap());
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        accessEvent2.setLoggerContext(loggerContext);
        accessEvent2.setTimeStamp(1L);
        accessEvent2.setMethod("GET");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent2.setRemoteHost("127.0.0.1");
        accessEvent2.setRemoteUser("testuser");
        accessEvent2.setServerName("test.com");
        accessEvent2.setRemoteAddress("127.0.0.1");
        accessEvent2.setRequestHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent2.setResponseHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent2.setRequestParameters(Collections.emptyMap());
        accessEvent2.setLocalPort(8080);
        accessEvent2.setStatusCode(200);

        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());

        accessEvent3.setLoggerContext(loggerContext);
        accessEvent3.setTimeStamp(2L);
        accessEvent3.setMethod("GET");
        accessEvent3.setProtocol("HTTP/1.1");
        accessEvent3.setRemoteHost("127.0.0.1");
        accessEvent3.setRemoteUser("testuser");
        accessEvent3.setServerName("test.com");
        accessEvent3.setRemoteAddress("127.0.0.1");
        accessEvent3.setRequestHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent3.setResponseHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent3.setRequestParameters(Collections.emptyMap());
        accessEvent3.setLocalPort(8080);
        accessEvent3.setStatusCode(200);

        assertNotEquals(accessEvent1.hashCode(), accessEvent3.hashCode());

        accessEvent3.setTimeStamp(1L);

        assertEquals(accessEvent1.hashCode(), accessEvent3.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(loggerContext);
        accessEvent.setTimeStamp(1L);

        String expectedToString = "AccessEvent[loggerContext=null, timeStamp=1]";
        String actualToString = accessEvent.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testClone() {
        AccessEvent accessEvent = new AccessEvent();
        AccessEvent clonedAccessEvent = null;

        try {
            clonedAccessEvent = (AccessEvent) accessEvent.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            fail("Clone should not throw CloneNotSupportedException");
        }

        assertNotNull(clonedAccessEvent);
        assertTrue(clonedAccessEvent instanceof AccessEvent);

        AccessEvent originalAccessEvent = new AccessEvent();
        originalAccessEvent.setLoggerContext(loggerContext);
        originalAccessEvent.setTimeStamp(1L);
        originalAccessEvent.setMethod("GET");
        originalAccessEvent.setProtocol("HTTP/1.1");
        originalAccessEvent.setRemoteHost("127.0.0.1");
        originalAccessEvent.setRemoteUser("testuser");
        originalAccessEvent.setServerName("test.com");
        originalAccessEvent.setRemoteAddress("127.0.0.1");
        originalAccessEvent.setRequestHeaders(Collections.singletonMap("header1", "value1"));
        originalAccessEvent.setResponseHeaders(Collections.singletonMap("header1", "value1"));
        originalAccessEvent.setRequestParameters(Collections.emptyMap());
        originalAccessEvent.setLocalPort(8080);
        originalAccessEvent.setStatusCode(200);

        accessEvent.setLoggerContext(loggerContext);
        accessEvent.setTimeStamp(1L);
        accessEvent.setMethod("GET");
        accessEvent.setProtocol("HTTP/1.1");
        accessEvent.setRemoteHost("127.0.0.1");
        accessEvent.setRemoteUser("testuser");
        accessEvent.setServerName("test.com");
        accessEvent.setRemoteAddress("127.0.0.1");
        accessEvent.setRequestHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent.setResponseHeaders(Collections.singletonMap("header1", "value1"));
        accessEvent.setRequestParameters(Collections.emptyMap());
        accessEvent.setLocalPort(8080);
        accessEvent.setStatusCode(200);

        assertEquals(originalAccessEvent, accessEvent);
        assertEquals(originalAccessEvent.hashCode(), accessEvent.hashCode());

        clonedAccessEvent.setLoggerContext(null);
        clonedAccessEvent.setTimeStamp(2L);
        clonedAccessEvent.setMethod("POST");
        clonedAccessEvent.setProtocol("HTTP/1.1");
        clonedAccessEvent.setRemoteHost("192.168.0.1");
        clonedAccessEvent.setRemoteUser("admin");
        clonedAccessEvent.setServerName("test.com");
        clonedAccessEvent.setRemoteAddress("192.168.0.1");
        clonedAccessEvent.setRequestHeaders(Collections.singletonMap("header1", "value2"));
        clonedAccessEvent.setResponseHeaders(Collections.singletonMap("header1", "value2"));
        clonedAccessEvent.setRequestParameters(Collections.singletonMap("param1", new String[]{"value3", "value4"}));
        clonedAccessEvent.setLocalPort(80);
        clonedAccessEvent.setStatusCode(404);

        assertNotEquals(originalAccessEvent, clonedAccessEvent);
        assertNotEquals(originalAccessEvent.hashCode(), clonedAccessEvent.hashCode());
    }
}