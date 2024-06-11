import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.*;

public class AccessEventTest {

    private AccessEvent accessEvent;

    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }

    @Test
    public void testGettersAndSetters_timeStamp() {
        Long testTimeStamp = 1L;
        accessEvent.setTimeStamp(testTimeStamp);
        assertEquals(testTimeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGettersAndSetters_loggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGettersAndSetters_requestURI() {
        String testRequestURI = "/test";
        accessEvent.setRequestURI(testRequestURI);
        assertEquals(testRequestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        String testRequestURL = "http://test.com/test";
        accessEvent.setRequestURL(testRequestURL);
        assertEquals(testRequestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        String testRemoteHost = "127.0.0.1";
        accessEvent.setRemoteHost(testRemoteHost);
        assertEquals(testRemoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        String testRemoteUser = "testUser";
        accessEvent.setRemoteUser(testRemoteUser);
        assertEquals(testRemoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGettersAndSetters_protocol() {
        String testProtocol = "HTTP/1.1";
        accessEvent.setProtocol(testProtocol);
        assertEquals(testProtocol, accessEvent.getProtocol());
    }

    @Test
    public void testGettersAndSetters_method() {
        String testMethod = "GET";
        accessEvent.setMethod(testMethod);
        assertEquals(testMethod, accessEvent.getMethod());
    }

    @Test
    public void testGettersAndSetters_serverName() {
        String testServerName = "testServer";
        accessEvent.setServerName(testServerName);
        assertEquals(testServerName, accessEvent.getServerName());
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        String testRemoteAddress = "192.168.0.1";
        accessEvent.setRemoteAddress(testRemoteAddress);
        assertEquals(testRemoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGettersAndSetters_requestHeaders() {
        Map<String, String> testRequestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(testRequestHeaders);
        assertEquals(testRequestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        Map<String, String> testResponseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(testResponseHeaders);
        assertEquals(testResponseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        Map<String, String[]> testRequestParameters = new HashMap<>();
        accessEvent.setRequestParameters(testRequestParameters);
        assertEquals(testRequestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGettersAndSetters_localPort() {
        int testLocalPort = 8080;
        accessEvent.setLocalPort(testLocalPort);
        assertEquals(testLocalPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        int testStatusCode = 200;
        accessEvent.setStatusCode(testStatusCode);
        assertEquals(testStatusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testHashCodeEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://test.com/test");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testUser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("testServer");
        event1.setRemoteAddress("192.168.0.1");
        event1.setRequestHeaders(new HashMap<String, String>());
        event1.setResponseHeaders(new HashMap<String, String>());
        event1.setRequestParameters(new HashMap<String, String[]>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://test.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testUser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("testServer");
        event2.setRemoteAddress("192.168.0.1");
        event2.setRequestHeaders(new HashMap<String, String>());
        event2.setResponseHeaders(new HashMap<String, String>());
        event2.setRequestParameters(new HashMap<String, String[]>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertTrue(event1.equals(event2));
        assertTrue(event1.hashCode() == event2.hashCode());
    }
}