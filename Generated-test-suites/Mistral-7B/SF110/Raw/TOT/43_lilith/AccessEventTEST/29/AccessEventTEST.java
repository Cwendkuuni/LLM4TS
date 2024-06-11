import org.junit.Assert;
import org.junit.Test;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

    @Test
    public void testAccessEvent() {
        // Test constructor with valid arguments
        Long timeStamp = 1L;
        LoggerContext loggerContext = new LoggerContext();
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
        AccessEvent event = new AccessEvent(timeStamp, loggerContext, requestURI, requestURL, remoteHost, remoteUser, protocol, method, serverName, remoteAddress, requestHeaders, responseHeaders, requestParameters, localPort, statusCode);
        Assert.assertEquals(timeStamp, event.getTimeStamp());
        Assert.assertEquals(loggerContext, event.getLoggerContext());
        Assert.assertEquals(requestURI, event.getRequestURI());
        Assert.assertEquals(requestURL, event.getRequestURL());
        Assert.assertEquals(remoteHost, event.getRemoteHost());
        Assert.assertEquals(remoteUser, event.getRemoteUser());
        Assert.assertEquals(protocol, event.getProtocol());
        Assert.assertEquals(method, event.getMethod());
        Assert.assertEquals(serverName, event.getServerName());
        Assert.assertEquals(remoteAddress, event.getRemoteAddress());
        Assert.assertEquals(requestHeaders, event.getRequestHeaders());
        Assert.assertEquals(responseHeaders, event.getResponseHeaders());
        Assert.assertEquals(requestParameters, event.getRequestParameters());
        Assert.assertEquals(localPort, event.getLocalPort());
        Assert.assertEquals(statusCode, event.getStatusCode());

        // Test constructor with null arguments
        AccessEvent nullEvent = new AccessEvent(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0);
        Assert.assertNull(nullEvent.getTimeStamp());
        Assert.assertNull(nullEvent.getLoggerContext());

        // Test setters and getters
        AccessEvent setEvent = new AccessEvent(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0);
        Long testTimeStamp = 2L;
        setEvent.setTimeStamp(testTimeStamp);
        Assert.assertEquals(testTimeStamp, setEvent.getTimeStamp());

        LoggerContext testLoggerContext = new LoggerContext();
        setEvent.setLoggerContext(testLoggerContext);
        Assert.assertEquals(testLoggerContext, setEvent.getLoggerContext());

        String testRequestURI = "/test2";
        setEvent.setRequestURI(testRequestURI);
        Assert.assertEquals(testRequestURI, setEvent.getRequestURI());

        String testRequestURL = "http://test2.com/test2";
        setEvent.setRequestURL(testRequestURL);
        Assert.assertEquals(testRequestURL, setEvent.getRequestURL());

        // Test equals method
        AccessEvent equalEvent = new AccessEvent(timeStamp, loggerContext, requestURI, requestURL, remoteHost, remoteUser, protocol, method, serverName, remoteAddress, requestHeaders, responseHeaders, requestParameters, localPort, statusCode);
        AccessEvent notEqualEvent = new AccessEvent(timeStamp, loggerContext, requestURI, requestURL, remoteHost, remoteUser, protocol, method, serverName, remoteAddress, requestHeaders, responseHeaders, requestParameters, localPort + 1, statusCode);
        Assert.assertTrue(setEvent.equals(equalEvent));
        Assert.assertFalse(setEvent.equals(notEqualEvent));

        // Test hashCode method
        int testHashCode = setEvent.hashCode();
        AccessEvent sameEvent = new AccessEvent(timeStamp, loggerContext, requestURI, requestURL, remoteHost, remoteUser, protocol, method, serverName, remoteAddress, requestHeaders, responseHeaders, requestParameters, localPort, statusCode);
        Assert.assertEquals(testHashCode, sameEvent.hashCode());

        // Test toString method
        String testToString = setEvent.toString();
        String expectedToString = "AccessEvent[loggerContext=LoggerContext[], timeStamp=2]";
        Assert.assertEquals(expectedToString, testToString);
    }

    @Test
    public void testGettersAndSettersWithInvalidArguments() {
        // Test setters with invalid arguments
        AccessEvent event = new AccessEvent(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0);

        try {
            event.setTimeStamp("invalidTimeStamp");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setLoggerContext("invalidLoggerContext");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setRequestURI("invalidRequestURI");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setRequestURL("invalidRequestURL");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setRemoteHost("invalidRemoteHost");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setRemoteUser("invalidRemoteUser");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setProtocol("invalidProtocol");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setMethod("invalidMethod");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setServerName("invalidServerName");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setRemoteAddress("invalidRemoteAddress");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setRequestHeaders(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setResponseHeaders(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setRequestParameters(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setLocalPort(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setStatusCode(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test getters with invalid arguments
        try {
            event.getTimeStamp();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getLoggerContext();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getRequestURI();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getRequestURL();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getRemoteHost();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getRemoteUser();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getProtocol();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getMethod();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getServerName();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getRemoteAddress();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getRequestHeaders();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getResponseHeaders();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getRequestParameters();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getLocalPort();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            event.getStatusCode();
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEmptyCollections() {
        AccessEvent event = new AccessEvent(null, null, null, null, null, null, null, null, null, null, Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap(), 0, 0);
        Map<String, String> requestHeaders = event.getRequestHeaders();
        Map<String, String> responseHeaders = event.getResponseHeaders();
        Map<String, String[]> requestParameters = event.getRequestParameters();
        Assert.assertEquals(Collections.emptyMap(), requestHeaders);
        Assert.assertEquals(Collections.emptyMap(), responseHeaders);
        Assert.assertEquals(Collections.emptyMap(), requestParameters);
    }

    @Test
    public void testRequestHeadersWithValidArguments() {
        AccessEvent event = new AccessEvent(null, null, null, null, null, null, null, null, null, null, new HashMap<>(), new HashMap<>(), new HashMap<>(), 0, 0);
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        event.setRequestHeaders(requestHeaders);
        Map<String, String> testRequestHeaders = event.getRequestHeaders();
        Assert.assertEquals(requestHeaders, testRequestHeaders);
    }

    @Test
    public void testResponseHeadersWithValidArguments() {
        AccessEvent event = new AccessEvent(null, null, null, null, null, null, null, null, null, null, new HashMap<>(), new HashMap<>(), new HashMap<>(), 0, 0);
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        event.setResponseHeaders(responseHeaders);
        Map<String, String> testResponseHeaders = event.getResponseHeaders();
        Assert.assertEquals(responseHeaders, testResponseHeaders);
    }

    @Test
    public void testRequestParametersWithValidArguments() {
        AccessEvent event = new AccessEvent(null, null, null, null, null, null, null, null, null, null, new HashMap<>(), new HashMap<>(), new HashMap<>(), 0, 0);
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        event.setRequestParameters(requestParameters);
        Map<String, String[]> testRequestParameters = event.getRequestParameters();
        Assert.assertEquals(requestParameters, testRequestParameters);
    }

    @Test
    public void testRequestParametersWithEmptyMap() {
        AccessEvent event = new AccessEvent(null, null, null, null, null, null, null, null, null, null, new HashMap<>(), new HashMap<>(), Collections.emptyMap(), 0, 0);
        Map<String, String[]> requestParameters = event.getRequestParameters();
        Assert.assertEquals(Collections.emptyMap(), requestParameters);
    }

    @Test
    public void testSetRequestParametersWithNullMap() {
        AccessEvent event = new AccessEvent(null, null, null, null, null, null, null, null, null, null, new HashMap<>(), new HashMap<>(), null, 0, 0);
        try {
            event.setRequestParameters(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSetRequestParametersWithInvalidMap() {
        AccessEvent event = new AccessEvent(null, null, null, null, null, null, null, null, null, null, new HashMap<>(), new HashMap<>(), new HashMap<String, List<String>>() {{
            put("invalidKey", Collections.singletonList("invalidValue"));
        }}, 0, 0);
        try {
            event.setRequestParameters(new HashMap<String, List<String>>() {{
                put("invalidKey", null);
            }});
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            event.setRequestParameters(new HashMap<String, List<String>>() {{
                put("invalidKey", Arrays.asList("invalidValue1", "invalidValue2"));
            }});
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}