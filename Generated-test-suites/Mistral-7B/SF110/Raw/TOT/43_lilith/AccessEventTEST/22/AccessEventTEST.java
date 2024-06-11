import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class AccessEventTest {

    @Test
    public void testAccessEventWithValidValues() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test/path");
        event.setRequestURL("http://localhost:8080/test/path");
        event.setRemoteHost("127.0.0.1");
        event.setRemoteUser("testUser");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("localhost");
        event.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Host", "localhost:8080");
        requestHeaders.put("User-Agent", "Mozilla/5.0");
        event.setRequestHeaders(requestHeaders);
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        event.setResponseHeaders(responseHeaders);
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        event.setRequestParameters(requestParameters);
        event.setLocalPort(8080);
        event.setStatusCode(200);

        Assert.assertEquals(1L, event.getTimeStamp());
        Assert.assertNotNull(event.getLoggerContext());
        Assert.assertEquals("/test/path", event.getRequestURI());
        Assert.assertEquals("http://localhost:8080/test/path", event.getRequestURL());
        Assert.assertEquals("127.0.0.1", event.getRemoteHost());
        Assert.assertEquals("testUser", event.getRemoteUser());
        Assert.assertEquals("HTTP/1.1", event.getProtocol());
        Assert.assertEquals("GET", event.getMethod());
        Assert.assertEquals("localhost", event.getServerName());
        Assert.assertEquals("127.0.0.1", event.getRemoteAddress());
        Assert.assertEquals(requestHeaders, event.getRequestHeaders());
        Assert.assertEquals(responseHeaders, event.getResponseHeaders());
        Assert.assertEquals(requestParameters, event.getRequestParameters());
        Assert.assertEquals(8080, event.getLocalPort());
        Assert.assertEquals(200, event.getStatusCode());
    }

    @Test
    public void testAccessEventWithNullValues() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(null);
        event.setLoggerContext(null);
        event.setRequestURI(null);
        event.setRequestURL(null);
        event.setRemoteHost(null);
        event.setRemoteUser(null);
        event.setProtocol(null);
        event.setMethod(null);
        event.setServerName(null);
        event.setRemoteAddress(null);
        event.setRequestHeaders(null);
        event.setResponseHeaders(null);
        event.setRequestParameters(null);
        event.setLocalPort(-1);
        event.setStatusCode(0);

        Assert.assertNull(event.getTimeStamp());
        Assert.assertNull(event.getLoggerContext());
        Assert.assertNull(event.getRequestURI());
        Assert.assertNull(event.getRequestURL());
        Assert.assertNull(event.getRemoteHost());
        Assert.assertNull(event.getRemoteUser());
        Assert.assertNull(event.getProtocol());
        Assert.assertNull(event.getMethod());
        Assert.assertNull(event.getServerName());
        Assert.assertNull(event.getRemoteAddress());
        Assert.assertNull(event.getRequestHeaders());
        Assert.assertNull(event.getResponseHeaders());
        Assert.assertNotNull(event.getRequestParameters());
        Assert.assertEquals(-1, event.getLocalPort());
        Assert.assertEquals(0, event.getStatusCode());
    }

    @Test
    public void testAccessEventWithEmptyMaps() {
        AccessEvent event = new AccessEvent();
        Map<String, String> emptyRequestHeaders = new HashMap<>();
        Map<String, String> emptyResponseHeaders = new HashMap<>();
        Map<String, String[]> emptyRequestParameters = new HashMap<>();

        event.setRequestHeaders(emptyRequestHeaders);
        event.setResponseHeaders(emptyResponseHeaders);
        event.setRequestParameters(emptyRequestParameters);

        Assert.assertEquals(emptyRequestHeaders, event.getRequestHeaders());
        Assert.assertEquals(emptyResponseHeaders, event.getResponseHeaders());
        Assert.assertEquals(emptyRequestParameters, event.getRequestParameters());
    }

    @Test
    public void testAccessEventEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setTimeStamp(1L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test/path");
        event1.setRequestURL("http://localhost:8080/test/path");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("testUser");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("localhost");
        event1.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Host", "localhost:8080");
        requestHeaders1.put("User-Agent", "Mozilla/5.0");
        event1.setRequestHeaders(requestHeaders1);
        Map<String, String> responseHeaders1 = new HashMap<>();
        responseHeaders1.put("Content-Type", "application/json");
        event1.setResponseHeaders(responseHeaders1);
        Map<String, String[]> requestParameters1 = new HashMap<>();
        requestParameters1.put("param1", new String[]{"value1", "value2"});
        event1.setRequestParameters(requestParameters1);
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(1L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test/path");
        event2.setRequestURL("http://localhost:8080/test/path");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testUser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("localhost");
        event2.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders2 = new HashMap<>();
        requestHeaders2.put("Host", "localhost:8080");
        requestHeaders2.put("User-Agent", "Mozilla/5.0");
        event2.setRequestHeaders(requestHeaders2);
        Map<String, String> responseHeaders2 = new HashMap<>();
        responseHeaders2.put("Content-Type", "application/json");
        event2.setResponseHeaders(responseHeaders2);
        Map<String, String[]> requestParameters2 = new HashMap<>();
        requestParameters2.put("param1", new String[]{"value1", "value2"});
        event2.setRequestParameters(requestParameters2);
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        Assert.assertTrue(event1.equals(event2));
    }

    @Test
    public void testAccessEventHashCode() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test/path";
        String requestURL = "http://localhost:8080/test/path";
        String remoteHost = "127.0.0.1";
        String remoteUser = "testUser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "localhost";
        String remoteAddress = "127.0.0.1";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Host", "localhost:8080");
        requestHeaders.put("User-Agent", "Mozilla/5.0");
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        int localPort = 8080;
        int statusCode = 200;

        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(timeStamp);
        event1.setLoggerContext(loggerContext);
        event1.setRequestURI(requestURI);
        event1.setRequestURL(requestURL);
        event1.setRemoteHost(remoteHost);
        event1.setRemoteUser(remoteUser);
        event1.setProtocol(protocol);
        event1.setMethod(method);
        event1.setServerName(serverName);
        event1.setRemoteAddress(remoteAddress);
        event1.setRequestHeaders(requestHeaders);
        event1.setResponseHeaders(responseHeaders);
        event1.setRequestParameters(requestParameters);
        event1.setLocalPort(localPort);
        event1.setStatusCode(statusCode);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(timeStamp);
        event2.setLoggerContext(loggerContext);
        event2.setRequestURI(requestURI);
        event2.setRequestURL(requestURL);
        event2.setRemoteHost(remoteHost);
        event2.setRemoteUser(remoteUser);
        event2.setProtocol(protocol);
        event2.setMethod(method);
        event2.setServerName(serverName);
        event2.setRemoteAddress(remoteAddress);
        event2.setRequestHeaders(requestHeaders);
        event2.setResponseHeaders(responseHeaders);
        event2.setRequestParameters(requestParameters);
        event2.setLocalPort(localPort);
        event2.setStatusCode(statusCode);

        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }
}