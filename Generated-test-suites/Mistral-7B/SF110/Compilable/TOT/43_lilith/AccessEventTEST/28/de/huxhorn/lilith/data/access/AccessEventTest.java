package de.huxhorn.lilith.data.access;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.*;

public class AccessEventTest {

 private AccessEvent event;
 private LoggerContext loggerContext;

 @Before
 public void setUp() {
 loggerContext = new LoggerContext();
 event = new AccessEvent();
 event.setLoggerContext(loggerContext);
 event.setTimeStamp(System.currentTimeMillis());
 }

 @Test
 public void testGetTimeStamp() {
 Long expected = event.getTimeStamp();
 Long actual = System.currentTimeMillis();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetTimeStamp() {
 Long expected = System.currentTimeMillis() + 1000L;
 event.setTimeStamp(expected);
 Long actual = event.getTimeStamp();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLoggerContext() {
 assertSame(loggerContext, event.getLoggerContext());
 }

 @Test
 public void testSetLoggerContext() {
 LoggerContext newLoggerContext = new LoggerContext();
 event.setLoggerContext(newLoggerContext);
 assertSame(newLoggerContext, event.getLoggerContext());
 }

 @Test
 public void testEmptyGettersAndSetters() {
 event.setRequestURI(null);
 event.setRequestURL(null);
 event.setRemoteHost(null);
 event.setRemoteUser(null);
 event.setProtocol(null);
 event.setMethod(null);
 event.setServerName(null);
 event.setRemoteAddress(null);
 event.setRequestHeaders(Collections.emptyMap());
 event.setResponseHeaders(Collections.emptyMap());
 event.setRequestParameters(Collections.emptyMap());
 event.setLocalPort(0);
 event.setStatusCode(0);

 assertNull(event.getRequestURI());
 assertNull(event.getRequestURL());
 assertNull(event.getRemoteHost());
 assertNull(event.getRemoteUser());
 assertNull(event.getProtocol());
 assertNull(event.getMethod());
 assertNull(event.getServerName());
 assertNull(event.getRemoteAddress());
 assertTrue(event.getRequestHeaders().isEmpty());
 assertTrue(event.getResponseHeaders().isEmpty());
 assertTrue(event.getRequestParameters().isEmpty());
 assertEquals(0, event.getLocalPort());
 assertEquals(0, event.getStatusCode());
 }

 @Test
 public void testNonEmptyGettersAndSetters() {
 event.setRequestURI("/path/to/resource1");
 event.setRequestURL("http://example.com/path/to/resource1");
 event.setRemoteHost("192.168.1.1");
 event.setRemoteUser("user");
 event.setProtocol("HTTP/1.1");
 event.setMethod("GET");
 event.setServerName("example.com");
 event.setRemoteAddress("192.168.1.2");
 event.setRequestHeaders(new HashMap<String, String>() {{
 put("Content-Type", "text/plain");
 put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
 }});
 event.setResponseHeaders(new HashMap<String, String>() {{
 put("Content-Type", "text/html");
 put("Cache-Control", "max-age=3600");
 }});
 event.setRequestParameters(new HashMap<String, String[]>() {{
 put("param1", new String[] {"value1", "value2"});
 put("param2", new String[] {"value3"});
 }});
 event.setLocalPort(8080);
 event.setStatusCode(200);

 assertEquals("/path/to/resource1", event.getRequestURI());
 assertEquals("http://example.com/path/to/resource1", event.getRequestURL());
 assertEquals("192.168.1.1", event.getRemoteHost());
 assertEquals("user", event.getRemoteUser());
 assertEquals("HTTP/1.1", event.getProtocol());
 assertEquals("GET", event.getMethod());
 assertEquals("example.com", event.getServerName());
 assertEquals("192.168.1.2", event.getRemoteAddress());
 assertFalse(event.getRequestHeaders().isEmpty());
 assertFalse(event.getResponseHeaders().isEmpty());
 assertFalse(event.getRequestParameters().isEmpty());
 assertEquals(8080, event.getLocalPort());
 assertEquals(200, event.getStatusCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTimeStampWithNegativeNumber() {
 event.setTimeStamp(-1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLocalPortWithNegativeNumber() {
 event.setLocalPort(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStatusCodeWithNegativeNumber() {
 event.setStatusCode(-1);
 }
}