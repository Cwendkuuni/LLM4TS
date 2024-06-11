import org.junit.Assert;import org.junit.Test;import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

 private AccessEvent accessEvent;

 @Test
 public void testGettersAndSetters_timeStamp() {
 accessEvent = new AccessEvent();
 Long timeStamp = 1L;
 accessEvent.setTimeStamp(timeStamp);
 Assert.assertEquals(timeStamp, accessEvent.getTimeStamp());
 }

 @Test
 public void testGettersAndSetters_loggerContext() {
 LoggerContext loggerContext = new LoggerContext();
 accessEvent = new AccessEvent();
 accessEvent.setLoggerContext(loggerContext);
 Assert.assertEquals(loggerContext, accessEvent.getLoggerContext());
 }

 @Test
 public void testGettersAndSetters_requestURI() {
 String requestURI = "requestURI";
 accessEvent = new AccessEvent();
 accessEvent.setRequestURI(requestURI);
 Assert.assertEquals(requestURI, accessEvent.getRequestURI());
 }

 @Test
 public void testGettersAndSetters_requestURL() {
 String requestURL = "requestURL";
 accessEvent = new AccessEvent();
 accessEvent.setRequestURL(requestURL);
 Assert.assertEquals(requestURL, accessEvent.getRequestURL());
 }

 @Test
 public void testGettersAndSetters_remoteHost() {
 String remoteHost = "remoteHost";
 accessEvent = new AccessEvent();
 accessEvent.setRemoteHost(remoteHost);
 Assert.assertEquals(remoteHost, accessEvent.getRemoteHost());
 }

 @Test
 public void testGettersAndSetters_remoteUser() {
 String remoteUser = "remoteUser";
 accessEvent = new AccessEvent();
 accessEvent.setRemoteUser(remoteUser);
 Assert.assertEquals(remoteUser, accessEvent.getRemoteUser());
 }

 @Test
 public void testGettersAndSetters_protocol() {
 String protocol = "protocol";
 accessEvent = new AccessEvent();
 accessEvent.setProtocol(protocol);
 Assert.assertEquals(protocol, accessEvent.getProtocol());
 }

 @Test
 public void testGettersAndSetters_method() {
 String method = "method";
 accessEvent = new AccessEvent();
 accessEvent.setMethod(method);
 Assert.assertEquals(method, accessEvent.getMethod());
 }

 @Test
 public void testGettersAndSetters_serverName() {
 String serverName = "serverName";
 accessEvent = new AccessEvent();
 accessEvent.setServerName(serverName);
 Assert.assertEquals(serverName, accessEvent.getServerName());
 }

 @Test
 public void testGettersAndSetters_remoteAddress() {
 String remoteAddress = "remoteAddress";
 accessEvent = new AccessEvent();
 accessEvent.setRemoteAddress(remoteAddress);
 Assert.assertEquals(remoteAddress, accessEvent.getRemoteAddress());
 }

 @Test
 public void testGettersAndSetters_requestHeaders() {
 Map<String, String> requestHeaders = new HashMap<>();
 accessEvent = new AccessEvent();
 accessEvent.setRequestHeaders(requestHeaders);
 Assert.assertEquals(requestHeaders, accessEvent.getRequestHeaders());
 }

 @Test
 public void testGettersAndSetters_responseHeaders() {
 Map<String, String> responseHeaders = new HashMap<>();
 accessEvent = new AccessEvent();
 accessEvent.setResponseHeaders(responseHeaders);
 Assert.assertEquals(responseHeaders, accessEvent.getResponseHeaders());
 }

 @Test
 public void testGettersAndSetters_requestParameters() {
 Map<String, String[]> requestParameters = new HashMap<>();
 accessEvent = new AccessEvent();
 accessEvent.setRequestParameters(requestParameters);
 Assert.assertEquals(requestParameters, accessEvent.getRequestParameters());
 }

 @Test
 public void testGettersAndSetters_localPort() {
 int localPort = 1;
 accessEvent = new AccessEvent();
 accessEvent.setLocalPort(localPort);
 Assert.assertEquals(localPort, accessEvent.getLocalPort());
 }

 @Test
 public void testGettersAndSetters_statusCode() {
 int statusCode = 1;
 accessEvent = new AccessEvent();
 accessEvent.setStatusCode(statusCode);
 Assert.assertEquals(statusCode, accessEvent.getStatusCode());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullLoggerContext() {
 accessEvent = new AccessEvent();
 accessEvent.setLoggerContext(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullRequestURI() {
 accessEvent = new AccessEvent();
 accessEvent.setRequestURI(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullRequestURL() {
 accessEvent = new AccessEvent();
 accessEvent.setRequestURL(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullRemoteHost() {
 accessEvent = new AccessEvent();
 accessEvent.setRemoteHost(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullRemoteUser() {
 accessEvent = new AccessEvent();
 accessEvent.setRemoteUser(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullProtocol() {
 accessEvent = new AccessEvent();
 accessEvent.setProtocol(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullMethod() {
 accessEvent = new AccessEvent();
 accessEvent.setMethod(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullServerName() {
 accessEvent = new AccessEvent();
 accessEvent.setServerName(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullRemoteAddress() {
 accessEvent = new AccessEvent();
 accessEvent.setRemoteAddress(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullRequestHeaders() {
 accessEvent = new AccessEvent();
 accessEvent.setRequestHeaders(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullResponseHeaders() {
 accessEvent = new AccessEvent();
 accessEvent.setResponseHeaders(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullRequestParameters() {
 accessEvent = new AccessEvent();
 accessEvent.setRequestParameters(null);
 }

 @Test
 public void testEquals_sameObject() {
 AccessEvent accessEvent1 = new AccessEvent();
 Assert.assertThat(accessEvent1, AccessEvent.class, is(equalTo(accessEvent1)));
 }

 @Test
 public void testEquals_notSameObject() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentLocalPort() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setLocalPort(1);
 accessEvent2.setLocalPort(2);
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentStatusCode() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setStatusCode(1);
 accessEvent2.setStatusCode(2);
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentLoggerContext() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 LoggerContext loggerContext1 = new LoggerContext();
 LoggerContext loggerContext2 = new LoggerContext();
 accessEvent1.setLoggerContext(loggerContext1);
 accessEvent2.setLoggerContext(loggerContext2);
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentMethod() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setMethod("GET");
 accessEvent2.setMethod("POST");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentProtocol() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setProtocol("HTTP/1.1");
 accessEvent2.setProtocol("HTTP/1.0");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentRemoteAddress() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setRemoteAddress("127.0.0.1");
 accessEvent2.setRemoteAddress("192.168.1.1");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentRemoteHost() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setRemoteHost("localhost");
 accessEvent2.setRemoteHost("server.com");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentRemoteUser() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setRemoteUser("user1");
 accessEvent2.setRemoteUser("user2");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentRequestHeaders() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 Map<String, String> requestHeaders1 = new HashMap<>();
 Map<String, String> requestHeaders2 = new HashMap<>();
 accessEvent1.setRequestHeaders(requestHeaders1);
 accessEvent2.setRequestHeaders(requestHeaders2);
 requestHeaders1.put("X-Forwarded-For", "192.168.1.1");
 requestHeaders2.put("X-Forwarded-For", "127.0.0.1");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentRequestURI() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setRequestURI("/api/v1/users");
 accessEvent2.setRequestURI("/api/v1/posts");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentRequestURL() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setRequestURL("http://localhost:8080/api/v1/users");
 accessEvent2.setRequestURL("http://localhost:8080/api/v1/posts");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentResponseHeaders() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 Map<String, String> responseHeaders1 = new HashMap<>();
 Map<String, String> responseHeaders2 = new HashMap<>();
 accessEvent1.setResponseHeaders(responseHeaders1);
 accessEvent2.setResponseHeaders(responseHeaders2);
 responseHeaders1.put("Content-Type", "application/json; charset=UTF-8");
 responseHeaders2.put("Content-Type", "application/xml; charset=UTF-8");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testEquals_differentServerName() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 accessEvent1.setServerName("localhost");
 accessEvent2.setServerName("server.com");
 Assert.assertThat(accessEvent1, is(not(equalTo(accessEvent2))));
 }

 @Test
 public void testHashCode_sameObject() {
 AccessEvent accessEvent1 = new AccessEvent();
 Assert.assertEquals(accessEvent1.hashCode(), accessEvent1.hashCode());
 }

 @Test
 public void testHashCode_differentObjects() {
 AccessEvent accessEvent1 = new AccessEvent();
 AccessEvent accessEvent2 = new AccessEvent();
 assertFalse(accessEvent1.hashCode() == accessEvent2.hashCode());
 }
}