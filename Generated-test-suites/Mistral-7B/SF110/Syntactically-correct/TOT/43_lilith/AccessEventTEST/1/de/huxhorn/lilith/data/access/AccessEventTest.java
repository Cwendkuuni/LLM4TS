import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.*;
import static org.junit.Assert.*;;

public class AccessEventTest {

 private AccessEvent accessEvent;
 private LoggerContext loggerContextMock;

 @Before
 public void setup() {
 loggerContextMock = Mockito.mock(LoggerContext.class);
 accessEvent = new AccessEvent();
 accessEvent.setLoggerContext(loggerContextMock);
 }

 @Test
 public void testGetAndSetTimeStamp() {
 Long timeStamp = 123L;
 accessEvent.setTimeStamp(timeStamp);
 assertEquals(timeStamp, accessEvent.getTimeStamp());
 }

 @Test
 public void testGetAndSetLoggerContext() {
 assertNull(accessEvent.getLoggerContext());
 accessEvent.setLoggerContext(loggerContextMock);
 assertEquals(loggerContextMock, accessEvent.getLoggerContext());
 }

 @Test
 public void testGetAndSetRequestURI() {
 String requestURI = "/test";
 accessEvent.setRequestURI(requestURI);
 assertEquals(requestURI, accessEvent.getRequestURI());
 }

 @Test
 public void testGetAndSetRequestURL() {
 String requestURL = "http://test.com";
 accessEvent.setRequestURL(requestURL);
 assertEquals(requestURL, accessEvent.getRequestURL());
 }

 @Test
 public void testGetAndSetRemoteHost() {
 String remoteHost = "192.168.0.1";
 accessEvent.setRemoteHost(remoteHost);
 assertEquals(remoteHost, accessEvent.getRemoteHost());
 }

 @Test
 public void testGetAndSetRemoteUser() {
 String remoteUser = "user";
 accessEvent.setRemoteUser(remoteUser);
 assertEquals(remoteUser, accessEvent.getRemoteUser());
 }

 @Test
 public void testGetAndSetProtocol() {
 String protocol = "HTTP/1.1";
 accessEvent.setProtocol(protocol);
 assertEquals(protocol, accessEvent.getProtocol());
 }

 @Test
 public void testGetAndSetMethod() {
 String method = "GET";
 accessEvent.setMethod(method);
 assertEquals(method, accessEvent.getMethod());
 }

 @Test
 public void testGetAndSetServerName() {
 String serverName = "example.com";
 accessEvent.setServerName(serverName);
 assertEquals(serverName, accessEvent.getServerName());
 }

 @Test
 public void testGetAndSetRemoteAddress() {
 String remoteAddress = "192.168.0.2";
 accessEvent.setRemoteAddress(remoteAddress);
 assertEquals(remoteAddress, accessEvent.getRemoteAddress());
 }

 @Test
 public void testGetRequestHeadersMapIsEmpty() {
 assertTrue(accessEvent.getRequestHeaders().isEmpty());
 }

 @Test
 public void testSetRequestHeadersWithEmptyMap() {
 Map<String, String> requestHeaders = new HashMap<>();
 accessEvent.setRequestHeaders(requestHeaders);
 assertEquals(requestHeaders, accessEvent.getRequestHeaders());
 }

 @Test
 public void testSetRequestHeadersWithNonEmptyMap() {
 Map<String, String> requestHeaders = new HashMap<>();
 requestHeaders.put("Key", "Value");
 accessEvent.setRequestHeaders(requestHeaders);
 assertEquals(requestHeaders, accessEvent.getRequestHeaders());
 }

 @Test
 public void testGetResponseHeadersMapIsEmpty() {
 assertTrue(accessEvent.getResponseHeaders().isEmpty());
 }

 @Test
 public void testSetResponseHeadersWithEmptyMap() {
 Map<String, String> responseHeaders = new HashMap<>();
 accessEvent.setResponseHeaders(responseHeaders);
 assertEquals(responseHeaders, accessEvent.getResponseHeaders());
 }

 @Test
 public void testSetResponseHeadersWithNonEmptyMap() {
 Map<String, String> responseHeaders = new HashMap<>();
 responseHeaders.put("Key", "Value");
 accessEvent.setResponseHeaders(responseHeaders);
 assertEquals(responseHeaders, accessEvent.getResponseHeaders());
 }

 @Test
 public void testGetRequestParametersMapIsEmpty() {
 assertTrue(accessEvent.getRequestParameters().isEmpty());
 }

 @Test
 public void testSetRequestParametersWithEmptyMap() {
 Map<String, String[]> requestParameters = new HashMap<>();
 accessEvent.setRequestParameters(requestParameters);
 assertEquals(requestParameters, accessEvent.getRequestParameters());
 }

 @Test
 public void testSetRequestParametersWithNonEmptyMap() {
 Map<String, String[]> requestParameters = new HashMap<>();
 requestParameters.put("Key", new String[]{"Value1", "Value2"});
 accessEvent.setRequestParameters(requestParameters);
 assertEquals(requestParameters, accessEvent.getRequestParameters());
 }

 @Test
 public void testGetLocalPort() {
 int localPort = 8080;
 accessEvent.setLocalPort(localPort);
 assertEquals(localPort, accessEvent.getLocalPort());
 }

 @Test
 public void testSetStatusCode() {
 int statusCode = 200;
 accessEvent.setStatusCode(statusCode);
 assertEquals(statusCode, accessEvent.getStatusCode());
 }

 @Test
 public void testEqualsReturnsTrueForEqualObjects() {
 AccessEvent otherAccessEvent = new AccessEvent();
 otherAccessEvent.setTimeStamp(accessEvent.getTimeStamp());
 otherAccessEvent.setLoggerContext(loggerContextMock);
 otherAccessEvent.setRequestURI(accessEvent.getRequestURI());
 otherAccessEvent.setRequestURL(accessEvent.getRequestURL());
 otherAccessEvent.setRemoteHost(accessEvent.getRemoteHost());
 otherAccessEvent.setRemoteUser(accessEvent.getRemoteUser());
 otherAccessEvent.setProtocol(accessEvent.getProtocol());
 otherAccessEvent.setMethod(accessEvent.getMethod());
 otherAccessEvent.setServerName(accessEvent.getServerName());
 otherAccessEvent.setRemoteAddress(accessEvent.getRemoteAddress());
 otherAccessEvent.setRequestHeaders(accessEvent.getRequestHeaders());
 otherAccessEvent.setResponseHeaders(accessEvent.getResponseHeaders());
 otherAccessEvent.setRequestParameters(accessEvent.getRequestParameters());
 otherAccessEvent.setLocalPort(accessEvent.getLocalPort());
 otherAccessEvent.setStatusCode(accessEvent.getStatusCode());
 assertTrue(accessEvent.equals(otherAccessEvent));
 }

 @Test
 public void testEqualsReturnsFalseForDifferentObjects() {
 AccessEvent otherAccessEvent = new AccessEvent();
 otherAccessEvent.setTimeStamp(accessEvent.getTimeStamp() + 1L);
 assertFalse(accessEvent.equals(otherAccessEvent));
 }

 @Test
 public void testHashCode() {
 AccessEvent otherAccessEvent = new AccessEvent();
 otherAccessEvent.setTimeStamp(accessEvent.getTimeStamp());
 otherAccessEvent.setLoggerContext(loggerContextMock);
 otherAccessEvent.setRequestURI(accessEvent.getRequestURI());
 otherAccessEvent.setRequestURL(accessEvent.getRequestURL());
 otherAccessEvent.setRemoteHost(accessEvent.getRemoteHost());
 otherAccessEvent.setRemoteUser(accessEvent.getRemoteUser());
 otherAccessEvent.setProtocol(accessEvent.getProtocol());
 otherAccessEvent.setMethod(accessEvent.getMethod());
 otherAccessEvent.setServerName(accessEvent.getServerName());
 otherAccessEvent.setRemoteAddress(accessEvent.getRemoteAddress());
 otherAccessEvent.setRequestHeaders(accessEvent.getRequestHeaders());
 otherAccessEvent.setResponseHeaders(accessEvent.getResponseHeaders());
 otherAccessEvent.setRequestParameters(accessEvent.getRequestParameters());
 otherAccessEvent.setLocalPort(accessEvent.getLocalPort());
 otherAccessEvent.setStatusCode(accessEvent.getStatusCode());
 assertEquals(accessEvent.hashCode(), otherAccessEvent.hashCode());
 }
}