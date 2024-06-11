package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testAccessEventEquality() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        assertEquals(event1, event2);

        event1.setTimeStamp(1L);
        assertNotEquals(event1, event2);
        event2.setTimeStamp(1L);
        assertEquals(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        assertNotEquals(event1, event2);
        event2.setLoggerContext(new LoggerContext());
        assertEquals(event1, event2);

        event1.setRequestURI("/test");
        assertNotEquals(event1, event2);
        event2.setRequestURI("/test");
        assertEquals(event1, event2);

        event1.setRequestURL("http://test.com/test");
        assertNotEquals(event1, event2);
        event2.setRequestURL("http://test.com/test");
        assertEquals(event1, event2);

        event1.setRemoteHost("127.0.0.1");
        assertNotEquals(event1, event2);
        event2.setRemoteHost("127.0.0.1");
        assertEquals(event1, event2);

        event1.setRemoteUser("user");
        assertNotEquals(event1, event2);
        event2.setRemoteUser("user");
        assertEquals(event1, event2);

        event1.setProtocol("HTTP/1.1");
        assertNotEquals(event1, event2);
        event2.setProtocol("HTTP/1.1");
        assertEquals(event1, event2);

        event1.setMethod("GET");
        assertNotEquals(event1, event2);
        event2.setMethod("GET");
        assertEquals(event1, event2);

        event1.setServerName("test.com");
        assertNotEquals(event1, event2);
        event2.setServerName("test.com");
        assertEquals(event1, event2);

        event1.setRemoteAddress("127.0.0.1");
        assertNotEquals(event1, event2);
        event2.setRemoteAddress("127.0.0.1");
        assertEquals(event1, event2);

        event1.setRequestHeaders(new HashMap<>());
        assertNotEquals(event1, event2);
        event2.setRequestHeaders(new HashMap<>());
        assertEquals(event1, event2);

        Map<String, String> headers1 = new HashMap<>();
        headers1.put("foo", "bar");
        event1.setRequestHeaders(headers1);
        Map<String, String> headers2 = Collections.singletonMap("foo", "bar");
        event2.setRequestHeaders(headers2);
        assertNotEquals(event1, event2);
        headers2.put("baz", "qux");
        assertEquals(event1, event2);

        event1.setRequestParameters(new HashMap<>());
        assertNotEquals(event1, event2);
        event2.setRequestParameters(new HashMap<>());
        assertEquals(event1, event2);

        event1.setLocalPort(8080);
        assertNotEquals(event1, event2);
        event2.setLocalPort(8080);
        assertEquals(event1, event2);

        event1.setStatusCode(200);
        assertNotEquals(event1, event2);
        event2.setStatusCode(200);
        assertEquals(event1, event2);

        event1.setLoggerContext(null);
        assertNotEquals(event1, event2);
        event2.setLoggerContext(null);
        assertEquals(event1, event2);

        event1.setMethod(null);
        assertNotEquals(event1, event2);
        event2.setMethod(null);
        assertEquals(event1, event2);
    }

    @Test
    public void testAccessEventHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setTimeStamp(1L);
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setTimeStamp(1L);
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setLoggerContext(new LoggerContext());
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setLoggerContext(new LoggerContext());
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestURI("/test");
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setRequestURI("/test");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestURL("http://test.com/test");
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setRequestURL("http://test.com/test");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteHost("127.0.0.1");
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setRemoteHost("127.0.0.1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteUser("user");
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setRemoteUser("user");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setProtocol("HTTP/1.1");
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setProtocol("HTTP/1.1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setMethod("GET");
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setMethod("GET");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setServerName("test.com");
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setServerName("test.com");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRemoteAddress("127.0.0.1");
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setRemoteAddress("127.0.0.1");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestHeaders(new HashMap<>());
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setRequestHeaders(new HashMap<>());
        assertEquals(event1.hashCode(), event2.hashCode());

        Map<String, String> headers1 = new HashMap<>();
        headers1.put("foo", "bar");
        event1.setRequestHeaders(headers1);
        Map<String, String> headers2 = Collections.singletonMap("foo", "bar");
        event2.setRequestHeaders(headers2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
        headers2.put("baz", "qux");
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestParameters(new HashMap<>());
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setRequestParameters(new HashMap<>());
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setLocalPort(8080);
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setLocalPort(8080);
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setStatusCode(200);
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setStatusCode(200);
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setLoggerContext(null);
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setLoggerContext(null);
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setMethod(null);
        assertNotEquals(event1.hashCode(), event2.hashCode());
        event2.setMethod(null);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testAccessEventToString() {
        AccessEvent event = new AccessEvent();
        StringBuilder expected = new StringBuilder();
        expected.append("AccessEvent[loggerContext=null, timeStamp=null]");
        assertEquals(expected.toString(), event.toString());

        event.setLoggerContext(new LoggerContext());
        expected.append("AccessEvent[loggerContext=").append(event.getLoggerContext()).append(", timeStamp=").append(event.getTimeStamp()).append("]");
        assertTrue(expected.toString().contains(event.toString()));

        event.setTimeStamp(1L);
        expected.replace(expected.length() - 2, expected.length(), "]");
        expected.append("timeStamp=").append(event.getTimeStamp());
        assertTrue(expected.toString().contains(event.toString()));
    }

    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();
        assertEquals(null, event.getTimeStamp());
        assertEquals(null, event.getLoggerContext());
        assertEquals(null, event.getRequestURI());
        assertEquals(null, event.getRequestURL());
        assertEquals(null, event.getRemoteHost());
        assertEquals(null, event.getRemoteUser());
        assertEquals(null, event.getProtocol());
        assertEquals(null, event.getMethod());
        assertEquals(null, event.getServerName());
        assertEquals(null, event.getRemoteAddress());
        assertEquals(null, event.getRequestHeaders());
        assertEquals(null, event.getResponseHeaders());
        assertEquals(null, event.getRequestParameters());
        assertEquals(0, event.getLocalPort());
        assertEquals(0, event.getStatusCode());

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

        event.setRemoteUser("user");
        assertEquals("user", event.getRemoteUser());

        event.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", event.getProtocol());

        event.setMethod("GET");
        assertEquals("GET", event.getMethod());

        event.setServerName("test.com");
        assertEquals("test.com", event.getServerName());

        event.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", event.getRemoteAddress());

        Map<String, String> headers = new HashMap<>();
        headers.put("foo", "bar");
        event.setRequestHeaders(headers);
        assertEquals(headers, event.getRequestHeaders());

        Map<String, String> headers2 = new HashMap<>();
        headers2.put("foo", "bar");
        headers2.put("baz", "qux");
        event.setRequestHeaders(headers2);
        assertEquals(headers2, event.getRequestHeaders());

        event.setRequestParameters(Collections.singletonMap("foo", new String[]{"bar"}));
        assertEquals(Collections.singletonMap("foo", new String[]{"bar"}), event.getRequestParameters());

        event.setLocalPort(8080);
        assertEquals(8080, event.getLocalPort());

        event.setStatusCode(200);
        assertEquals(200, event.getStatusCode());
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}