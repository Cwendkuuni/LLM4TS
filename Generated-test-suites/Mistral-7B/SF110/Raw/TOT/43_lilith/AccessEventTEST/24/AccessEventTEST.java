import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();

        event.setTimeStamp(1L);
        assertEquals(1L, event.getTimeStamp());

        event.setLoggerContext(new LoggerContext());
        assertEquals(event.getLoggerContext(), event.getLoggerContext());

        event.setRequestURI("/test");
        assertEquals("/test", event.getRequestURI());

        event.setRequestURL("http://test.com/test");
        assertEquals("http://test.com/test", event.getRequestURL());

        event.setRemoteHost("127.0.0.1");
        assertEquals("127.0.0.1", event.getRemoteHost());

        event.setRemoteUser("test");
        assertEquals("test", event.getRemoteUser());

        event.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", event.getProtocol());

        event.setMethod("GET");
        assertEquals("GET", event.getMethod());

        event.setServerName("test.com");
        assertEquals("test.com", event.getServerName());

        event.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", event.getRemoteAddress());

        event.setRequestHeaders(new HashMap<>());
        assertNotNull(event.getRequestHeaders());

        event.setResponseHeaders(new HashMap<>());
        assertNotNull(event.getResponseHeaders());

        event.setRequestParameters(new HashMap<>());
        assertNotNull(event.getRequestParameters());

        event.setLocalPort(8080);
        assertEquals(8080, event.getLocalPort());

        event.setStatusCode(200);
        assertEquals(200, event.getStatusCode());
    }
}