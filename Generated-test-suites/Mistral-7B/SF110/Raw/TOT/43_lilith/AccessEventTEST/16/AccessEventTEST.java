import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

 @Test
 public void testAccessEventBasic() {
 AccessEvent event = new AccessEvent();
 Long timeStamp = 1L;
 LoggerContext loggerContext = new LoggerContext();
 String requestURI = "/test";
 String requestURL = "http://test.com" + requestURI;
 String remoteHost = "127.0.0.1";
 String remoteUser = "user";
 String protocol = "HTTP/1.1";
 String method = "GET";
 String serverName = "test.com";
 String remoteAddress = "127.0.0.1";
 Map<String, String> requestHeaders = new HashMap<>();
 requestHeaders.put("Host", "test.com");
 Map<String, String> responseHeaders = new HashMap<>();
 Map<String, String[]> requestParameters = new HashMap<>();
 int localPort = 8080;
 int statusCode = 200;

 event.setTimeStamp(timeStamp);
 event.setLoggerContext(loggerContext);
 event.setRequestURI(requestURI);
 event.setRequestURL(requestURL);
 event.setRemoteHost(remoteHost);
 event.setRemoteUser(remoteUser);
 event.setProtocol(protocol);
 event.setMethod(method);
 event.setServerName(serverName);
 event.setRemoteAddress(remoteAddress);
 event.setRequestHeaders(requestHeaders);
 event.setResponseHeaders(responseHeaders);
 event.setRequestParameters(requestParameters);
 event.setLocalPort(localPort);
 event.setStatusCode(statusCode);

 // Test getters
 assertEquals(timeStamp, event.getTimeStamp());
 assertEquals(loggerContext, event.getLoggerContext());
 assertEquals(requestURI, event.getRequestURI());
 assertEquals(requestURL, event.getRequestURL());
 assertEquals(remoteHost, event.getRemoteHost());
 assertEquals(remoteUser, event.getRemoteUser());
 assertEquals(protocol, event.getProtocol());
 assertEquals(method, event.getMethod());
 assertEquals(serverName, event.getServerName());
 assertEquals(remoteAddress, event.getRemoteAddress());
 assertEquals(requestHeaders, event.getRequestHeaders());
 assertEquals(responseHeaders, event.getResponseHeaders());
 assertEquals(requestParameters, event.getRequestParameters());
 assertEquals(localPort, event.getLocalPort());
 assertEquals(statusCode, event.getStatusCode());
 }

 @Test
 public void testAccessEventEmpty() {
 AccessEvent event = new AccessEvent();

 // Test empty constructor
 assertNull(event.getTimeStamp());
 assertNull(event.getLoggerContext());
 assertNull(event.getRequestURI());
 assertNull(event.getRequestURL());
 assertNull(event.getRemoteHost());
 assertNull(event.getRemoteUser());
 assertNull(event.getProtocol());
 assertNull(event.getMethod());
 assertNull(event.getServerName());
 assertNull(event.getRemoteAddress());
 assertNull(event.getRequestHeaders());
 assertNull(event.getResponseHeaders());
 assertNull(event.getRequestParameters());
 assertNull(event.getLocalPort());
 assertNull(event.getStatusCode());
 }

 @Test
 public void testAccessEventSetters() {
 AccessEvent event = new AccessEvent();
 Long timeStamp = 1L;
 LoggerContext loggerContext = new LoggerContext();
 String requestURI = "/test";
 String requestURL = "http://test.com" + requestURI;
 String remoteHost = "127.0.0.1";
 String remoteUser = "user";
 String protocol = "HTTP/1.1";
 String method = "GET";
 String serverName = "test.com";
 String remoteAddress = "127.0.0.1";
 Map<String, String> requestHeaders = new HashMap<>();
 requestHeaders.put("Host", "test.com");
 Map<String, String> responseHeaders = new HashMap<>();
 Map<String, String[]> requestParameters = new HashMap<>();
 int localPort = 8080;
 int statusCode = 200;

 // Set properties
 event.setTimeStamp(timeStamp);
 event.setLoggerContext(loggerContext);
 event.setRequestURI(requestURI);
 event.setRequestURL(requestURL);
 event.setRemoteHost(remoteHost);
 event.setRemoteUser(remoteUser);
 event.setProtocol(protocol);
 event.setMethod(method);
 event.setServerName(serverName);
 event.setRemoteAddress(remoteAddress);
 event.setRequestHeaders(requestHeaders);
 event.setResponseHeaders(responseHeaders);
 event.setRequestParameters(requestParameters);
 event.setLocalPort(localPort);
 event.setStatusCode(statusCode);

 // Test getters
 assertEquals(timeStamp, event.getTimeStamp());
 assertEquals(loggerContext, event.getLoggerContext());
 assertEquals(requestURI, event.getRequestURI());
 assertEquals(requestURL, event.getRequestURL());
 assertEquals(remoteHost, event.getRemoteHost());
 assertEquals(remoteUser, event.getRemoteUser());
 assertEquals(protocol, event.getProtocol());
 assertEquals(method, event.getMethod());
 assertEquals(serverName, event.getServerName());
 assertEquals(remoteAddress, event.getRemoteAddress());
 assertEquals(requestHeaders, event.getRequestHeaders());
 assertEquals(responseHeaders, event.getResponseHeaders());
 assertEquals(requestParameters, event.getRequestParameters());
 assertEquals(localPort, event.getLocalPort());
 assertEquals(statusCode, event.getStatusCode());
 }

 @Test
 public void testAccessEventEquals() {
 AccessEvent event1 = new AccessEvent();
 AccessEvent event2 = new AccessEvent();
 AccessEvent event3 = new AccessEvent();

 event1.setTimeStamp(1L);
 event1.setLoggerContext(new LoggerContext());
 event1.setRequestURI("/test");
 event1.setRequestURL("http://test.com/test");
 event1.setRemoteHost("127.0.0.1");
 event1.setRemoteUser("user");
 event1.setProtocol("HTTP/1.1");
 event1.setMethod("GET");
 event1.setServerName("test.com");
 event1.setRemoteAddress("127.0.0.1");
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
 event2.setRemoteUser("user");
 event2.setProtocol("HTTP/1.1");
 event2.setMethod("GET");
 event2.setServerName("test.com");
 event2.setRemoteAddress("127.0.0.1");
 event2.setRequestHeaders(new HashMap<String, String>());
 event2.setResponseHeaders(new HashMap<String, String>());
 event2.setRequestParameters(new HashMap<String, String[]>());
 event2.setLocalPort(8080);
 event2.setStatusCode(200);

 assertEquals(event1, event2);

 event3.setTimeStamp(2L);
 event3.setLoggerContext(new LoggerContext());
 event3.setRequestURI("/test");
 event3.setRequestURL("http://test.com/test");
 event3.setRemoteHost("127.0.0.1");
 event3.setRemoteUser("user");
 event3.setProtocol("HTTP/1.1");
 event3.setMethod("GET");
 event3.setServerName("test.com");
 event3.setRemoteAddress("127.0.0.1");
 event3.setRequestHeaders(new HashMap<String, String>());
 event3.setResponseHeaders(new HashMap<String, String>());
 event3.setRequestParameters(new HashMap<String, String[]>());
 event3.setLocalPort(8080);
 event3.setStatusCode(200);

 assertNotEquals(event1, event3);

 event1.setTimeStamp(null);
 assertNotEquals(event1, event2);
 }

 @Test
 public void testAccessEventHashCode() {
 AccessEvent event1 = new AccessEvent();
 AccessEvent event2 = new AccessEvent();
 AccessEvent event3 = new AccessEvent();

 event1.setTimeStamp(1L);
 event1.setLoggerContext(new LoggerContext());
 event1.setRequestURI("/test");
 event1.setRequestURL("http://test.com/test");
 event1.setRemoteHost("127.0.0.1");
 event1.setRemoteUser("user");
 event1.setProtocol("HTTP/1.1");
 event1.setMethod("GET");
 event1.setServerName("test.com");
 event1.setRemoteAddress("127.0.0.1");
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
 event2.setRemoteUser("user");
 event2.setProtocol("HTTP/1.1");
 event2.setMethod("GET");
 event2.setServerName("test.com");
 event2.setRemoteAddress("127.0.0.1");
 event2.setRequestHeaders(new HashMap<String, String>());
 event2.setResponseHeaders(new HashMap<String, String>());
 event2.setRequestParameters(new HashMap<String, String[]>());
 event2.setLocalPort(8080);
 event2.setStatusCode(200);

 int hash1 = event1.hashCode();
 int hash2 = event2.hashCode();
 assertEquals(hash1, hash2);

 event3.setTimeStamp(1L);
 event3.setLoggerContext(new LoggerContext());
 event3.setRequestURI("/test");
 event3.setRequestURL("http://test.com/test");
 event3.setRemoteHost("127.0.0.2"); // Different remote address
 event3.setRemoteUser("user");
 event3.setProtocol("HTTP/1.1");
 event3.setMethod("GET");
 event3.setServerName("test.com");
 event3.setRemoteAddress("127.0.0.2");
 event3.setRequestHeaders(new HashMap<String, String>());
 event3.setResponseHeaders(new HashMap<String, String>());
 event3.setRequestParameters(new HashMap<String, String[]>());
 event3.setLocalPort(8080);
 event3.setStatusCode(200);

 int hash3 = event3.hashCode();
 assertNotEquals(hash1, hash3);
 }

 @Test
 public void testAccessEventToString() {
 AccessEvent event = new AccessEvent();
 Long timeStamp = 1L;
 LoggerContext loggerContext = new LoggerContext();
 String requestURI = "/test";
 String requestURL = "http://test.com" + requestURI;
 String remoteHost = "127.0.0.1";
 String remoteUser = "user";
 String protocol = "HTTP/1.1";
 String method = "GET";
 String serverName = "test.com";
 String remoteAddress = "127.0.0.1";
 Map<String, String> requestHeaders = new HashMap<>();
 Map<String, String> responseHeaders = new HashMap<>();
 Map<String, String[]> requestParameters = new HashMap<>();
 int localPort = 8080;
 int statusCode = 200;

 event.setTimeStamp(timeStamp);
 event.setLoggerContext(loggerContext);
 event.setRequestURI(requestURI);
 event.setRequestURL(requestURL);
 event.setRemoteHost(remoteHost);
 event.setRemoteUser(remoteUser);
 event.setProtocol(protocol);
 event.setMethod(method);
 event.setServerName(serverName);
 event.setRemoteAddress(remoteAddress);
 event.setRequestHeaders(requestHeaders);
 event.setResponseHeaders(responseHeaders);
 event.setRequestParameters(requestParameters);
 event.setLocalPort(localPort);
 event.setStatusCode(statusCode);

 String toString = event.toString();
 String expected = "AccessEvent[loggerContext=LoggerContext[], timeStamp=1, ]";
 assertEquals(expected, toString);
 }
}