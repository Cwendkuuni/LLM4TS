import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccessEventTest {

    private AccessEvent accessEvent;
    private LoggerContext loggerContextMock;

    @Before
    public void setUp() {
        loggerContextMock = Mockito.mock(LoggerContext.class);
        accessEvent = new AccessEvent();
    }

    @Test
    public void testGettersAndSettersForTimeStamp() {
        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGettersAndSettersForLoggerContext() {
        accessEvent.setLoggerContext(loggerContextMock);
        assertNotNull(accessEvent.getLoggerContext());
        Mockito.verify(loggerContextMock).hashCode(); // check that loggerContext is not null and has a working hashCode method
    }

    @Test
    public void testGettersAndSettersForRequestURI() {
        String requestURI = "/testURI";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGettersAndSettersForRequestURL() {
        String requestURL = "http://testURL";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGettersAndSettersForRemoteHost() {
        String remoteHost = "192.168.1.1";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGettersAndSettersForRemoteUser() {
        String remoteUser = "testUser";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGettersAndSettersForProtocol() {
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGettersAndSettersForMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGettersAndSettersForServerName() {
        String serverName = "testServer";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGettersAndSettersForRemoteAddress() {
        String remoteAddress = "192.168.1.2";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGettersAndSettersForRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGettersAndSettersForResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGettersAndSettersForRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGettersAndSettersForLocalPort() {
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGettersAndSettersForStatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEqualsMethod() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        Map<String, String[]> requestParameters1 = new HashMap<>();
        Map<String, String[]> requestParameters2 = new HashMap<>();

        accessEvent1.setLoggerContext(loggerContextMock);
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setRequestURI("/testURI");
        accessEvent1.setRequestURL("http://testURL");
        accessEvent1.setRemoteHost("192.168.1.1");
        accessEvent1.setRemoteUser("testUser");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent1.setServerName("testServer");
        accessEvent1.setRemoteAddress("192.168.1.2");
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(requestParameters1);
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        accessEvent2.setLoggerContext(loggerContextMock);
        accessEvent2.setTimeStamp(1L);
        accessEvent2.setRequestURI("/testURI");
        accessEvent2.setRequestURL("http://testURL");
        accessEvent2.setRemoteHost("192.168.1.1");
        accessEvent2.setRemoteUser("testUser");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent2.setMethod("GET");
        accessEvent2.setServerName("testServer");
        accessEvent2.setRemoteAddress("192.168.1.2");
        accessEvent2.setRequestHeaders(requestHeaders2);
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(requestParameters2);
        accessEvent2.setLocalPort(8080);
        accessEvent2.setStatusCode(200);

        assertEquals(accessEvent1, accessEvent2);

        requestHeaders1.put("header1", "value1");
        requestHeaders2.put("header1", "value2");

        assertFalse(accessEvent1.equals(accessEvent2));

        accessEvent2.setRequestHeaders(requestHeaders1);

        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCodeMethod() {
        AccessEvent accessEvent = new AccessEvent();

        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);

        assertEquals(accessEvent.hashCode(), accessEvent.hashCode());

        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(timeStamp);

        assertEquals(accessEvent.hashCode(), accessEvent2.hashCode());

        Map<String, String> requestHeaders = new HashMap<>();
        accessEvent.setRequestHeaders(requestHeaders);

        accessEvent2.setRequestHeaders(new HashMap<>(requestHeaders));

        assertEquals(accessEvent.hashCode(), accessEvent2.hashCode());

        requestHeaders.put("header1", "value1");

        Map<String, String> requestHeaders2 = new HashMap<>(requestHeaders);
        requestHeaders2.put("header2", "value2");

        assertNotEquals(accessEvent.hashCode(), accessEvent2.hashCode());

        accessEvent2.setRequestHeaders(requestHeaders);

        assertEquals(accessEvent.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testToStringMethod() {
        AccessEvent accessEvent = new AccessEvent();

        Long timeStamp = 1L;
        accessEvent.setTimeStamp(timeStamp);

        assertEquals("AccessEvent[loggerContext=null, timeStamp=1]", accessEvent.toString());

        LoggerContext loggerContextMock = Mockito.mock(LoggerContext.class);
        accessEvent.setLoggerContext(loggerContextMock);

        assertEquals("AccessEvent[loggerContext=" + loggerContextMock.hashCode() + ", timeStamp=1]", accessEvent.toString());
    }
}