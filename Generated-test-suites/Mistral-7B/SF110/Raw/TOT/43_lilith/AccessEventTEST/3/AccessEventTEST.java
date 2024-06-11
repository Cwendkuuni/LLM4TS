import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccessEventTest {

    private AccessEvent accessEvent;

    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }

    @Test
    public void testGettersAndSetters_validValues() {
        accessEvent.setTimeStamp(1L);
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setRequestURI("/path/to/resource");
        accessEvent.setRequestURL("http://example.com/path/to/resource");
        accessEvent.setRemoteHost("192.168.1.1");
        accessEvent.setRemoteUser("user");
        accessEvent.setProtocol("HTTP/1.1");
        accessEvent.setMethod("GET");
        accessEvent.setServerName("example.com");
        accessEvent.setRemoteAddress("192.168.1.2");
        accessEvent.setRequestHeaders(new HashMap<>());
        accessEvent.setResponseHeaders(new HashMap<>());
        accessEvent.setRequestParameters(new HashMap<>());
        accessEvent.setLocalPort(8080);
        accessEvent.setStatusCode(200);

        assertEquals(1L, accessEvent.getTimeStamp());
        assertEquals(accessEvent.getLoggerContext(), accessEvent.getLoggerContext());
        assertEquals("/path/to/resource", accessEvent.getRequestURI());
        assertEquals("http://example.com/path/to/resource", accessEvent.getRequestURL());
        assertEquals("192.168.1.1", accessEvent.getRemoteHost());
        assertEquals("user", accessEvent.getRemoteUser());
        assertEquals("HTTP/1.1", accessEvent.getProtocol());
        assertEquals("GET", accessEvent.getMethod());
        assertEquals("example.com", accessEvent.getServerName());
        assertEquals("192.168.1.2", accessEvent.getRemoteAddress());
        assertNotNull(accessEvent.getRequestHeaders());
        assertNotNull(accessEvent.getResponseHeaders());
        assertNotNull(accessEvent.getRequestParameters());
        assertEquals(8080, accessEvent.getLocalPort());
        assertEquals(200, accessEvent.getStatusCode());
    }

    @Test
    public void testGettersAndSetters_nullValues() {
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
        accessEvent.setLocalPort(0);
        accessEvent.setStatusCode(0);

        assertNull(accessEvent.getTimeStamp());
        assertNull(accessEvent.getLoggerContext());
        assertNull(accessEvent.getRequestURI());
        assertNull(accessEvent.getRequestURL());
        assertNull(accessEvent.getRemoteHost());
        assertNull(accessEvent.getRemoteUser());
        assertNull(accessEvent.getProtocol());
        assertNull(accessEvent.getMethod());
        assertNull(accessEvent.getServerName());
        assertNull(accessEvent.getRemoteAddress());
        assertTrue(accessEvent.getRequestHeaders().isEmpty());
        assertTrue(accessEvent.getResponseHeaders().isEmpty());
        assertTrue(accessEvent.getRequestParameters().isEmpty());
        assertEquals(0, accessEvent.getLocalPort());
        assertEquals(0, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals_sameObject() {
        final AccessEvent accessEvent1 = new AccessEvent();
        final AccessEvent accessEvent2 = accessEvent1;

        assertTrue(accessEvent1.equals(accessEvent2));
        assertTrue(accessEvent2.equals(accessEvent1));
    }

    @Test
    public void testEquals_differentObjects_sameValues() {
        final AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/path/to/resource");
        accessEvent1.setRequestURL("http://example.com/path/to/resource");
        accessEvent1.setRemoteHost("192.168.1.1");
        accessEvent1.setRemoteUser("user");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent1.setServerName("example.com");
        accessEvent1.setRemoteAddress("192.168.1.2");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        final AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(1L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("/path/to/resource");
        accessEvent2.setRequestURL("http://example.com/path/to/resource");
        accessEvent2.setRemoteHost("192.168.1.1");
        accessEvent2.setRemoteUser("user");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent2.setMethod("GET");
        accessEvent2.setServerName("example.com");
        accessEvent2.setRemoteAddress("192.168.1.2");
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent2.setLocalPort(8080);
        accessEvent2.setStatusCode(200);

        assertTrue(accessEvent1.equals(accessEvent2));
        assertTrue(accessEvent2.equals(accessEvent1));
    }

    @Test
    public void testEquals_differentObjects_differentValues() {
        final AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/path/to/resource");
        accessEvent1.setRequestURL("http://example.com/path/to/resource");
        accessEvent1.setRemoteHost("192.168.1.1");
        accessEvent1.setRemoteUser("user");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent1.setServerName("example.com");
        accessEvent1.setRemoteAddress("192.168.1.2");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        final AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(2L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("/path/to/another/resource");
        accessEvent2.setRequestURL("http://example.com/path/to/another/resource");
        accessEvent2.setRemoteHost("192.168.1.3");
        accessEvent2.setRemoteUser("otheruser");
        accessEvent2.setProtocol("HTTP/1.0");
        accessEvent2.setMethod("POST");
        accessEvent2.setServerName("example.net");
        accessEvent2.setRemoteAddress("192.168.1.4");
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent2.setLocalPort(80);
        accessEvent2.setStatusCode(404);

        assertFalse(accessEvent1.equals(accessEvent2));
        assertFalse(accessEvent2.equals(accessEvent1));
    }

    @Test
    public void testHashCode() {
        final AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setTimeStamp(1L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/path/to/resource");
        accessEvent1.setRequestURL("http://example.com/path/to/resource");
        accessEvent1.setRemoteHost("192.168.1.1");
        accessEvent1.setRemoteUser("user");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent1.setServerName("example.com");
        accessEvent1.setRemoteAddress("192.168.1.2");
        accessEvent1.setRequestHeaders(new HashMap<>());
        accessEvent1.setResponseHeaders(new HashMap<>());
        accessEvent1.setRequestParameters(new HashMap<>());
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        final AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(2L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("/path/to/another/resource");
        accessEvent2.setRequestURL("http://example.com/path/to/another/resource");
        accessEvent2.setRemoteHost("192.168.1.3");
        accessEvent2.setRemoteUser("otheruser");
        accessEvent2.setProtocol("HTTP/1.0");
        accessEvent2.setMethod("POST");
        accessEvent2.setServerName("example.net");
        accessEvent2.setRemoteAddress("192.168.1.4");
        accessEvent2.setRequestHeaders(new HashMap<>());
        accessEvent2.setResponseHeaders(new HashMap<>());
        accessEvent2.setRequestParameters(new HashMap<>());
        accessEvent2.setLocalPort(80);
        accessEvent2.setStatusCode(404);

        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }
}