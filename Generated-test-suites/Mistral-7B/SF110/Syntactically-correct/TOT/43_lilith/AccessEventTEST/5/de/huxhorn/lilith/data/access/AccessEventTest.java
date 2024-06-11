import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class AccessEventTest {

    @Test
    public void testAccessEventWithValidValues() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test");
        event.setRequestURL("http://test.com/test");
        event.setRemoteHost("127.0.0.1");
        event.setRemoteUser("testUser");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("localhost");
        event.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
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
        Assert.assertEquals("/test", event.getRequestURI());
        Assert.assertEquals("http://test.com/test", event.getRequestURL());
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
        Assert.assertNull(event.getRequestParameters());
        Assert.assertEquals(0, event.getLocalPort());
        Assert.assertEquals(0, event.getStatusCode());
    }

    @Test
    public void testAccessEventWithEmptyMaps() {
        AccessEvent event = new AccessEvent();

        Map<String, String> emptyRequestHeaders = new HashMap<>();
        event.setRequestHeaders(emptyRequestHeaders);
        Assert.assertTrue(emptyRequestHeaders.isEmpty());

        Map<String, String> emptyResponseHeaders = new HashMap<>();
        event.setResponseHeaders(emptyResponseHeaders);
        Assert.assertTrue(emptyResponseHeaders.isEmpty());

        Map<String, String[]> emptyRequestParameters = new HashMap<>();
        event.setRequestParameters(emptyRequestParameters);
        Assert.assertTrue(emptyRequestParameters.isEmpty());
    }

    @Test
    public void testAccessEventEqualsAndHashCode() {
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
        event1.setServerName("localhost");
        event1.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders1 = new HashMap<>();
        requestHeaders1.put("Content-Type", "application/json");
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
        event2.setRequestURI("/test");
        event2.setRequestURL("http://test.com/test");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("testUser");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("localhost");
        event2.setRemoteAddress("127.0.0.1");
        Map<String, String> requestHeaders2 = requestHeaders1;
        event2.setRequestHeaders(requestHeaders2);
        Map<String, String> responseHeaders2 = responseHeaders1;
        event2.setResponseHeaders(responseHeaders2);
        Map<String, String[]> requestParameters2 = requestParameters1;
        event2.setRequestParameters(requestParameters2);
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        Assert.assertTrue(event1.equals(event2));
        Assert.assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteUser("testUser2");
        Assert.assertFalse(event1.equals(event2));
        Assert.assertFalse(event1.hashCode() == event2.hashCode());

        event2.setRemoteUser("testUser2");
        Assert.assertTrue(event1.equals(event2));
        Assert.assertEquals(event1.hashCode(), event2.hashCode());

        event1.setLocalPort(8081);
        Assert.assertFalse(event1.equals(event2));
        Assert.assertFalse(event1.hashCode() == event2.hashCode());

        event2.setLocalPort(8081);
        Assert.assertTrue(event1.equals(event2));
        Assert.assertEquals(event1.hashCode(), event2.hashCode());
    }
}