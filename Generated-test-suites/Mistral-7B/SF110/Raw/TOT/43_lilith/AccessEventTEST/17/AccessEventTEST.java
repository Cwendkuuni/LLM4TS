import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

    private AccessEvent validAccessEvent;

    @Before
    public void setUp() {
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext("TestLogger");
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testServer";
        String remoteAddress = "127.0.0.2";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
        int statusCode = 200;

        validAccessEvent = new AccessEvent();
        validAccessEvent.setTimeStamp(timeStamp);
        validAccessEvent.setLoggerContext(loggerContext);
        validAccessEvent.setRequestURI(requestURI);
        validAccessEvent.setRequestURL(requestURL);
        validAccessEvent.setRemoteHost(remoteHost);
        validAccessEvent.setRemoteUser(remoteUser);
        validAccessEvent.setProtocol(protocol);
        validAccessEvent.setMethod(method);
        validAccessEvent.setServerName(serverName);
        validAccessEvent.setRemoteAddress(remoteAddress);
        validAccessEvent.setRequestHeaders(requestHeaders);
        validAccessEvent.setResponseHeaders(responseHeaders);
        validAccessEvent.setRequestParameters(requestParameters);
        validAccessEvent.setLocalPort(localPort);
        validAccessEvent.setStatusCode(statusCode);
    }

    @Test
    public void testGettersAndSetters_ValidAccessEvent() {
        assertEquals(validAccessEvent.getTimeStamp(), 1L);
        assertEquals(validAccessEvent.getLoggerContext(), loggerContext);
        assertEquals(validAccessEvent.getRequestURI(), requestURI);
        assertEquals(validAccessEvent.getRequestURL(), requestURL);
        assertEquals(validAccessEvent.getRemoteHost(), remoteHost);
        assertEquals(validAccessEvent.getRemoteUser(), remoteUser);
        assertEquals(validAccessEvent.getProtocol(), protocol);
        assertEquals(validAccessEvent.getMethod(), method);
        assertEquals(validAccessEvent.getServerName(), serverName);
        assertEquals(validAccessEvent.getRemoteAddress(), remoteAddress);
        assertEquals(validAccessEvent.getRequestHeaders(), requestHeaders);
        assertEquals(validAccessEvent.getResponseHeaders(), responseHeaders);
        assertEquals(validAccessEvent.getRequestParameters(), requestParameters);
        assertEquals(validAccessEvent.getLocalPort(), localPort);
        assertEquals(validAccessEvent.getStatusCode(), statusCode);

        // Set values
        Long newTimeStamp = 2L;
        LoggerContext newLoggerContext = new LoggerContext("NewLogger");
        String newRequestURI = "/newTest";
        String newRequestURL = "http://newtest.com/newTest";
        String newRemoteHost = "192.168.0.1";
        String newRemoteUser = "newUser";
        String newProtocol = "HTTP/1.1";
        String newMethod = "POST";
        String newServerName = "newServer";
        String newRemoteAddress = "192.168.0.2";
        Map<String, String> newRequestHeaders = new HashMap<>();
        newRequestHeaders.put("Content-Type", "text/plain");
        Map<String, String> newResponseHeaders = new HashMap<>();
        Map<String, String[]> newRequestParameters = new HashMap<>();
        int newLocalPort = 80;
        int newStatusCode = 404;

        validAccessEvent.setTimeStamp(newTimeStamp);
        validAccessEvent.setLoggerContext(newLoggerContext);
        validAccessEvent.setRequestURI(newRequestURI);
        validAccessEvent.setRequestURL(newRequestURL);
        validAccessEvent.setRemoteHost(newRemoteHost);
        validAccessEvent.setRemoteUser(newRemoteUser);
        validAccessEvent.setProtocol(newProtocol);
        validAccessEvent.setMethod(newMethod);
        validAccessEvent.setServerName(newServerName);
        validAccessEvent.setRemoteAddress(newRemoteAddress);
        validAccessEvent.setRequestHeaders(newRequestHeaders);
        validAccessEvent.setResponseHeaders(newResponseHeaders);
        validAccessEvent.setRequestParameters(newRequestParameters);
        validAccessEvent.setLocalPort(newLocalPort);
        validAccessEvent.setStatusCode(newStatusCode);

        assertEquals(validAccessEvent.getTimeStamp(), newTimeStamp);
        assertEquals(validAccessEvent.getLoggerContext(), newLoggerContext);
        assertEquals(validAccessEvent.getRequestURI(), newRequestURI);
        assertEquals(validAccessEvent.getRequestURL(), newRequestURL);
        assertEquals(validAccessEvent.getRemoteHost(), newRemoteHost);
        assertEquals(validAccessEvent.getRemoteUser(), newRemoteUser);
        assertEquals(validAccessEvent.getProtocol(), newProtocol);
        assertEquals(validAccessEvent.getMethod(), newMethod);
        assertEquals(validAccessEvent.getServerName(), newServerName);
        assertEquals(validAccessEvent.getRemoteAddress(), newRemoteAddress);
        assertEquals(validAccessEvent.getRequestHeaders(), newRequestHeaders);
        assertEquals(validAccessEvent.getResponseHeaders(), newResponseHeaders);
        assertEquals(validAccessEvent.getRequestParameters(), newRequestParameters);
        assertEquals(validAccessEvent.getLocalPort(), newLocalPort);
        assertEquals(validAccessEvent.getStatusCode(), newStatusCode);
    }

    @Test
    public void testEquals_EqualObjects() {
        AccessEvent equalAccessEvent = new AccessEvent();
        equalAccessEvent.setTimeStamp(1L);
        equalAccessEvent.setLoggerContext(loggerContext);
        equalAccessEvent.setRequestURI(requestURI);
        equalAccessEvent.setRequestURL(requestURL);
        equalAccessEvent.setRemoteHost(remoteHost);
        equalAccessEvent.setRemoteUser(remoteUser);
        equalAccessEvent.setProtocol(protocol);
        equalAccessEvent.setMethod(method);
        equalAccessEvent.setServerName(serverName);
        equalAccessEvent.setRemoteAddress(remoteAddress);
        equalAccessEvent.setRequestHeaders(requestHeaders);
        equalAccessEvent.setResponseHeaders(responseHeaders);
        equalAccessEvent.setRequestParameters(requestParameters);
        equalAccessEvent.setLocalPort(localPort);
        equalAccessEvent.setStatusCode(statusCode);

        assertTrue(validAccessEvent.equals(equalAccessEvent));
        assertTrue(equalAccessEvent.equals(validAccessEvent));
    }

    @Test
    public void testEquals_NotEqualObjects() {
        AccessEvent notEqualAccessEvent = new AccessEvent();
        notEqualAccessEvent.setTimeStamp(null);
        notEqualAccessEvent.setLoggerContext(null);
        notEqualAccessEvent.setRequestURI("notEqualURI");
        notEqualAccessEvent.setRequestURL("notEqualURL");
        notEqualAccessEvent.setRemoteHost("notEqualHost");
        notEqualAccessEvent.setRemoteUser("notEqualUser");
        notEqualAccessEvent.setProtocol("notEqualProtocol");
        notEqualAccessEvent.setMethod("notEqualMethod");
        notEqualAccessEvent.setServerName("notEqualServer");
        notEqualAccessEvent.setRemoteAddress("notEqualAddress");
        notEqualAccessEvent.setRequestHeaders(null);
        notEqualAccessEvent.setResponseHeaders(null);
        notEqualAccessEvent.setRequestParameters(null);
        notEqualAccessEvent.setLocalPort(1);
        notEqualAccessEvent.setStatusCode(201);

        assertFalse(validAccessEvent.equals(notEqualAccessEvent));
        assertFalse(notEqualAccessEvent.equals(validAccessEvent));
    }

    @Test
    public void testHashCode_ValidAccessEvent() {
        int expectedHashCode = 68514017;
        int calculatedHashCode = validAccessEvent.hashCode();
        assertEquals(expectedHashCode, calculatedHashCode);
    }

    @Test
    public void testToString_ValidAccessEvent() {
        String expectedToString = "AccessEvent[loggerContext=TestLogger, timeStamp=1]";
        String actualToString = validAccessEvent.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testToString_EmptyAccessEvent() {
        AccessEvent emptyAccessEvent = new AccessEvent();
        String expectedToString = "AccessEvent[]";
        String actualToString = emptyAccessEvent.toString();
        assertEquals(expectedToString, actualToString);
    }
}