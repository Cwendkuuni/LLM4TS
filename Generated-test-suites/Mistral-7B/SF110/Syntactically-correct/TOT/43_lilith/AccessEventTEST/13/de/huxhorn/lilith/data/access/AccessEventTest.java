import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.*;

public class AccessEventTest {

    private AccessEvent event;

    @Before
    public void setUp() {
        LoggerContext loggerContext = new LoggerContext();
        event = new AccessEvent();
        event.setLoggerContext(loggerContext);
    }

    @Test
    public void testGettersAndSetters_timeStamp() {
        Long expectedTimeStamp = 123L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        assertEquals(expectedTimeStamp, actualTimeStamp);
    }

    @Test
    public void testGettersAndSetters_loggerContext() {
        LoggerContext expectedLoggerContext = new LoggerContext();
        event.setLoggerContext(expectedLoggerContext);
        LoggerContext actualLoggerContext = event.getLoggerContext();
        assertEquals(expectedLoggerContext, actualLoggerContext);
    }

    @Test
    public void testGettersAndSetters_requestURI() {
        String expectedRequestURI = "/testURI";
        event.setRequestURI(expectedRequestURI);
        String actualRequestURI = event.getRequestURI();
        assertEquals(expectedRequestURI, actualRequestURI);
    }

    @Test
    public void testGettersAndSetters_requestURL() {
        String expectedRequestURL = "http://test.com/testURL";
        event.setRequestURL(expectedRequestURL);
        String actualRequestURL = event.getRequestURL();
        assertEquals(expectedRequestURL, actualRequestURL);
    }

    @Test
    public void testGettersAndSetters_remoteHost() {
        String expectedRemoteHost = "192.168.1.1";
        event.setRemoteHost(expectedRemoteHost);
        String actualRemoteHost = event.getRemoteHost();
        assertEquals(expectedRemoteHost, actualRemoteHost);
    }

    @Test
    public void testGettersAndSetters_remoteUser() {
        String expectedRemoteUser = "testUser";
        event.setRemoteUser(expectedRemoteUser);
        String actualRemoteUser = event.getRemoteUser();
        assertEquals(expectedRemoteUser, actualRemoteUser);
    }

    @Test
    public void testGettersAndSetters_protocol() {
        String expectedProtocol = "HTTP/1.1";
        event.setProtocol(expectedProtocol);
        String actualProtocol = event.getProtocol();
        assertEquals(expectedProtocol, actualProtocol);
    }

    @Test
    public void testGettersAndSetters_method() {
        String expectedMethod = "GET";
        event.setMethod(expectedMethod);
        String actualMethod = event.getMethod();
        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGettersAndSetters_serverName() {
        String expectedServerName = "testServer.com";
        event.setServerName(expectedServerName);
        String actualServerName = event.getServerName();
        assertEquals(expectedServerName, actualServerName);
    }

    @Test
    public void testGettersAndSetters_remoteAddress() {
        String expectedRemoteAddress = "192.168.1.2";
        event.setRemoteAddress(expectedRemoteAddress);
        String actualRemoteAddress = event.getRemoteAddress();
        assertEquals(expectedRemoteAddress, actualRemoteAddress);
    }

    @Test
    public void testGettersAndSetters_requestHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        event.setRequestHeaders(expectedHeaders);
        Map<String, String> actualHeaders = event.getRequestHeaders();
        assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    public void testGettersAndSetters_responseHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        event.setResponseHeaders(expectedHeaders);
        Map<String, String> actualHeaders = event.getResponseHeaders();
        assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    public void testGettersAndSetters_requestParameters() {
        Map<String, String[]> expectedParameters = new HashMap<>();
        expectedParameters.put("param1", new String[]{"value1", "value2"});
        event.setRequestParameters(expectedParameters);
        Map<String, String[]> actualParameters = event.getRequestParameters();
        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testGettersAndSetters_localPort() {
        int expectedLocalPort = 8080;
        event.setLocalPort(expectedLocalPort);
        int actualLocalPort = event.getLocalPort();
        assertEquals(expectedLocalPort, actualLocalPort);
    }

    @Test
    public void testGettersAndSetters_statusCode() {
        int expectedStatusCode = 200;
        event.setStatusCode(expectedStatusCode);
        int actualStatusCode = event.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        // Different objects
        assertFalse(event1.equals(new Object()));

        // Same object
        assertTrue(event1.equals(event1));

        // Equal AccessEvent instances
        event1.setLocalPort(8080);
        event1.setStatusCode(200);
        event1.setLoggerContext(new LoggerContext());
        event1.setMethod("GET");
        event1.setProtocol("HTTP/1.1");
        event1.setRemoteAddress("192.168.1.1");
        event1.setRemoteHost("192.168.1.1");
        event1.setRemoteUser("testUser");
        event1.setRequestURI("/testURI");
        event1.setRequestURL("http://test.com/testURL");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());

        event2.setLocalPort(8080);
        event2.setStatusCode(200);
        event2.setLoggerContext(new LoggerContext());
        event2.setMethod("GET");
        event2.setProtocol("HTTP/1.1");
        event2.setRemoteAddress("192.168.1.1");
        event2.setRemoteHost("192.168.1.1");
        event2.setRemoteUser("testUser");
        event2.setRequestURI("/testURI");
        event2.setRequestURL("http://test.com/testURL");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());

        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setLocalPort(8080);
        event1.setStatusCode(200);
        event1.setLoggerContext(new LoggerContext());
        event1.setMethod("GET");
        event1.setProtocol("HTTP/1.1");
        event1.setRemoteAddress("192.168.1.1");
        event1.setRemoteHost("192.168.1.1");
        event1.setRemoteUser("testUser");
        event1.setRequestURI("/testURI");
        event1.setRequestURL("http://test.com/testURL");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());

        event2.setLocalPort(8080);
        event2.setStatusCode(200);
        event2.setLoggerContext(new LoggerContext());
        event2.setMethod("GET");
        event2.setProtocol("HTTP/1.1");
        event2.setRemoteAddress("192.168.1.1");
        event2.setRemoteHost("192.168.1.1");
        event2.setRemoteUser("testUser");
        event2.setRequestURI("/testURI");
        event2.setRequestURL("http://test.com/testURL");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());

        int expectedHashCode = 31 * 31 * 31 * 8080 + 200 + loggerContext.hashCode() + "GET".hashCode() + "HTTP/1.1".hashCode() + "192.168.1.1".hashCode() + "testUser".hashCode() + "/testURI".hashCode() + "http://test.com/testURL".hashCode();
        int actualHashCode1 = event1.hashCode();
        int actualHashCode2 = event2.hashCode();

        assertEquals(expectedHashCode, actualHashCode1);
        assertEquals(expectedHashCode, actualHashCode2);
    }
}