import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.*;

public class AccessEventTest {

    @Test
    public void testAccessEventConstructor() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getTimeStamp());
        assertNull(event.getLoggerContext());
        assertNull(event.getRequestURI());
        assertNull(event.getRequestURL());
        assertNull(event.getRemoteHost());
        assertNull(event.getRemoteUser());
        assertNull(event.getProtocol());
        assertNull(event.getMethod());
        assertNull(event.getServerName());
        assertNull(event.getRemoteAddress());
        assertNull(event.getRequestHeaders());
        assertNull(event.getResponseHeaders());
        assertNull(event.getRequestParameters());
        assertEquals(0, event.getLocalPort());
        assertEquals(0, event.getStatusCode());
    }

    @Test
    public void testAccessEventSettersAndGetters() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com/test";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testuser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testserver.com";
        String remoteAddress = "192.168.0.1";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
        int statusCode = 200;

        event.setTimeStamp(timeStamp);
        event.setLoggerContext(loggerContext);
        event.setRequestURI(requestURI);
        event.setRequestURL(requestURL);
        event.setRemoteHost(remoteHost);
        event.setRemoteUser(remoteUser);
        event.setProtocol(protocol);
        event.setMethod(method);
        event.setServerName(serverName);
        event.setRemoteAddress(remoteAddress);
        event.setRequestHeaders(requestHeaders);
        event.setResponseHeaders(responseHeaders);
        event.setRequestParameters(requestParameters);
        event.setLocalPort(localPort);
        event.setStatusCode(statusCode);

        assertEquals(timeStamp, event.getTimeStamp());
        assertEquals(loggerContext, event.getLoggerContext());
        assertEquals(requestURI, event.getRequestURI());
        assertEquals(requestURL, event.getRequestURL());
        assertEquals(remoteHost, event.getRemoteHost());
        assertEquals(remoteUser, event.getRemoteUser());
        assertEquals(protocol, event.getProtocol());
        assertEquals(method, event.getMethod());
        assertEquals(serverName, event.getServerName());
        assertEquals(remoteAddress, event.getRemoteAddress());
        assertEquals(requestHeaders, event.getRequestHeaders());
        assertEquals(responseHeaders, event.getResponseHeaders());
        assertEquals(requestParameters, event.getRequestParameters());
        assertEquals(localPort, event.getLocalPort());
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testAccessEventEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testuser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("testserver.com");
        event1.setRemoteAddress("192.168.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://test.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testuser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("testserver.com");
        event2.setRemoteAddress("192.168.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertTrue(event1.equals(event2));

        event3.setLocalPort(8081);
        assertFalse(event1.equals(event3));
    }

    @Test
    public void testAccessEventHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testuser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("testserver.com");
        event1.setRemoteAddress("192.168.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://test.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testuser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("testserver.com");
        event2.setRemoteAddress("192.168.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();

        assertTrue(hashCode1 == hashCode2);

        event3.setLocalPort(8081);
        int hashCode3 = event3.hashCode();

        assertFalse(hashCode1 == hashCode3);
    }

    @Test
    public void testAccessEventToString() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test");
        event.setRequestURL("http://test.com/test");
        event.setRemoteHost("127.0.0.1");
        event.setRemoteUser("testuser");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("testserver.com");
        event.setRemoteAddress("192.168.0.1");
        event.setRequestHeaders(new HashMap<>());
        event.setResponseHeaders(new HashMap<>());
        event.setRequestParameters(new HashMap<>());
        event.setLocalPort(8080);
        event.setStatusCode(200);

        String expectedResult = "AccessEvent[loggerContext=LoggerContext[], timeStamp=1]";
        String result = event.toString();

        assertEquals(expectedResult, result);
    }
}