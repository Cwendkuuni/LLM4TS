import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

 private AccessEvent event;

 @Before
 public void setUp() {
 Long timeStamp = 1L;
 LoggerContext loggerContext = new LoggerContext("TestLogger");
 String requestURI = "/test";
 String requestURL = "http://test.com" + requestURI;
 String remoteHost = "127.0.0.1";
 String remoteUser = "testUser";
 String protocol = "HTTP/1.1";
 String method = "GET";
 String serverName = "testServer";
 String remoteAddress = "127.0.0.1";
 Map<String, String> requestHeaders = new HashMap<>();
 requestHeaders.put("Content-Type", "application/json");
 Map<String, String> responseHeaders = new HashMap<>();
 responseHeaders.put("Content-Type", "application/json");
 Map<String, String[]> requestParameters = new HashMap<>();
 int localPort = 8080;
 int statusCode = 200;

 event = new AccessEvent(timeStamp, loggerContext, requestURI, requestURL, remoteHost, remoteUser, protocol, method, serverName, remoteAddress, requestHeaders, responseHeaders, requestParameters, localPort, statusCode);
 }

 @Test
 public void testGettersAndSetters_timeStamp() {
 Long expectedTimeStamp = 1L;
 Long actualTimeStamp = event.getTimeStamp();
 assertEquals("TimeStamp should be set correctly", expectedTimeStamp, actualTimeStamp);

 Long newTimeStamp = 2L;
 event.setTimeStamp(newTimeStamp);
 actualTimeStamp = event.getTimeStamp();
 assertEquals("TimeStamp should be updated correctly", newTimeStamp, actualTimeStamp);
 }

 @Test
 public void testGettersAndSetters_loggerContext() {
 LoggerContext expectedLoggerContext = new LoggerContext("TestLogger");
 LoggerContext actualLoggerContext = event.getLoggerContext();
 assertEquals("LoggerContext should be set correctly", expectedLoggerContext, actualLoggerContext);

 LoggerContext newLoggerContext = new LoggerContext("TestLoggerNew");
 event.setLoggerContext(newLoggerContext);
 actualLoggerContext = event.getLoggerContext();
 assertEquals("LoggerContext should be updated correctly", newLoggerContext, actualLoggerContext);
 }

 @Test
 public void testGettersAndSetters_requestURI() {
 String expectedRequestURI = "/test";
 String actualRequestURI = event.getRequestURI();
 assertEquals("RequestURI should be set correctly", expectedRequestURI, actualRequestURI);

 String newRequestURI = "/newTest";
 event.setRequestURI(newRequestURI);
 actualRequestURI = event.getRequestURI();
 assertEquals("RequestURI should be updated correctly", newRequestURI, actualRequestURI);
 }

 @Test
 public void testGettersAndSetters_requestURL() {
 String expectedRequestURL = "http://test.com/test";
 String actualRequestURL = event.getRequestURL();
 assertEquals("RequestURL should be set correctly", expectedRequestURL, actualRequestURL);

 String newRequestURL = "http://newtest.com/newTest";
 event.setRequestURL(newRequestURL);
 actualRequestURL = event.getRequestURL();
 assertEquals("RequestURL should be updated correctly", newRequestURL, actualRequestURL);
 }

 @Test
 public void testGettersAndSetters_remoteHost() {
 String expectedRemoteHost = "127.0.0.1";
 String actualRemoteHost = event.getRemoteHost();
 assertEquals("RemoteHost should be set correctly", expectedRemoteHost, actualRemoteHost);

 String newRemoteHost = "192.168.1.1";
 event.setRemoteHost(newRemoteHost);
 actualRemoteHost = event.getRemoteHost();
 assertEquals("RemoteHost should be updated correctly", newRemoteHost, actualRemoteHost);
 }

 @Test
 public void testGettersAndSetters_remoteUser() {
 String expectedRemoteUser = "testUser";
 String actualRemoteUser = event.getRemoteUser();
 assertEquals("RemoteUser should be set correctly", expectedRemoteUser, actualRemoteUser);

 String newRemoteUser = "testUserNew";
 event.setRemoteUser(newRemoteUser);
 actualRemoteUser = event.getRemoteUser();
 assertEquals("RemoteUser should be updated correctly", newRemoteUser, actualRemoteUser);
 }

 @Test
 public void testGettersAndSetters_protocol() {
 String expectedProtocol = "HTTP/1.1";
 String actualProtocol = event.getProtocol();
 assertEquals("Protocol should be set correctly", expectedProtocol, actualProtocol);

 String newProtocol = "HTTP/2.0";
 event.setProtocol(newProtocol);
 actualProtocol = event.getProtocol();
 assertEquals("Protocol should be updated correctly", newProtocol, actualProtocol);
 }

 @Test
 public void testGettersAndSetters_method() {
 String expectedMethod = "GET";
 String actualMethod = event.getMethod();
 assertEquals("Method should be set correctly", expectedMethod, actualMethod);

 String newMethod = "POST";
 event.setMethod(newMethod);
 actualMethod = event.getMethod();
 assertEquals("Method should be updated correctly", newMethod, actualMethod);
 }

 @Test
 public void testGettersAndSetters_serverName() {
 String expectedServerName = "testServer";
 String actualServerName = event.getServerName();
 assertEquals("ServerName should be set correctly", expectedServerName, actualServerName);

 String newServerName = "testServerNew";
 event.setServerName(newServerName);
 actualServerName = event.getServerName();
 assertEquals("ServerName should be updated correctly", newServerName, actualServerName);
 }

 @Test
 public void testGettersAndSetters_remoteAddress() {
 String expectedRemoteAddress = "127.0.0.1";
 String actualRemoteAddress = event.getRemoteAddress();
 assertEquals("RemoteAddress should be set correctly", expectedRemoteAddress, actualRemoteAddress);

 String newRemoteAddress = "192.168.1.1";
 event.setRemoteAddress(newRemoteAddress);
 actualRemoteAddress = event.getRemoteAddress();
 assertEquals("RemoteAddress should be updated correctly", newRemoteAddress, actualRemoteAddress);
 }

 @Test
 public void testGettersAndSetters_requestHeaders() {
 Map<String, String> expectedRequestHeaders = new HashMap<>();
 expectedRequestHeaders.put("Content-Type", "application/json");
 Map<String, String> actualRequestHeaders = event.getRequestHeaders();
 assertEquals("RequestHeaders should be set correctly", expectedRequestHeaders, actualRequestHeaders);

 Map<String, String> newRequestHeaders = new HashMap<>();
 newRequestHeaders.put("Content-Type", "application/xml");
 event.setRequestHeaders(newRequestHeaders);
 actualRequestHeaders = event.getRequestHeaders();
 assertEquals("RequestHeaders should be updated correctly", newRequestHeaders, actualRequestHeaders);
 }

 @Test
 public void testGettersAndSetters_responseHeaders() {
 Map<String, String> expectedResponseHeaders = new HashMap<>();
 expectedResponseHeaders.put("Content-Type", "application/json");
 Map<String, String> actualResponseHeaders = event.getResponseHeaders();
 assertEquals("ResponseHeaders should be set correctly", expectedResponseHeaders, actualResponseHeaders);

 Map<String, String> newResponseHeaders = new HashMap<>();
 newResponseHeaders.put("Content-Type", "application/xml");
 event.setResponseHeaders(newResponseHeaders);
 actualResponseHeaders = event.getResponseHeaders();
 assertEquals("ResponseHeaders should be updated correctly", newResponseHeaders, actualResponseHeaders);
 }

 @Test
 public void testGettersAndSetters_requestParameters() {
 Map<String, String[]> expectedRequestParameters = new HashMap<>();
 expectedRequestParameters.put("param1", new String[]{"value1", "value2"});
 Map<String, String[]> actualRequestParameters = event.getRequestParameters();
 assertEquals("RequestParameters should be set correctly", expectedRequestParameters, actualRequestParameters);

 Map<String, String[]> newRequestParameters = new HashMap<>();
 newRequestParameters.put("param1", new String[]{"value3", "value4"});
 event.setRequestParameters(newRequestParameters);
 actualRequestParameters = event.getRequestParameters();
 assertEquals("RequestParameters should be updated correctly", newRequestParameters, actualRequestParameters);
 }

 @Test
 public void testGettersAndSetters_localPort() {
 int expectedLocalPort = 8080;
 int actualLocalPort = event.getLocalPort();
 assertEquals("LocalPort should be set correctly", expectedLocalPort, actualLocalPort);

 int newLocalPort = 8081;
 event.setLocalPort(newLocalPort);
 actualLocalPort = event.getLocalPort();
 assertEquals("LocalPort should be updated correctly", newLocalPort, actualLocalPort);
 }

 @Test
 public void testGettersAndSetters_statusCode() {
 int expectedStatusCode = 200;
 int actualStatusCode = event.getStatusCode();
 assertEquals("Status code should be set correctly", expectedStatusCode, actualStatusCode);

 int newStatusCode = 404;
 event.setStatusCode(newStatusCode);
 actualStatusCode = event.getStatusCode();
 assertEquals("Status code should be updated correctly", newStatusCode, actualStatusCode);
 }

 @Test
 public void testEquals_sameObject() {
 final AccessEvent event1 = this.event;
 assertTrue("Two identical objects should be considered equal", event1.equals(event1));
 }

 @Test
 public void testEquals_null() {
 assertFalse("Null object should not be considered equal", event1.equals(null));
 }

 @Test
 public void testEquals_otherClass() {
 assertFalse("Object of different class should not be considered equal", event1.equals(new Object()));
 }

 @Test
 public void testEquals_differentAccessEvent() {
 final AccessEvent event2 = new AccessEvent(2L, null, null, null, null, null, null, null, null, null, new HashMap<>(), new HashMap<>(), new HashMap<>(), 8080, 201);
 assertFalse("Two different AccessEvents should not be considered equal", event1.equals(event2));
 }

 @Test
 public void testEquals_sameFieldsButDifferentOrder() {
 final AccessEvent event2 = new AccessEvent(1L, null, null, null, null, null, null, null, null, null, new HashMap<>(Arrays.asList(new HashMap.SimpleEntry("Content-Type", "application/json"))), new HashMap<>(Arrays.asList(new HashMap.SimpleEntry("Content-Type", "application/json"))), new HashMap<>(Arrays.asList(new HashMap.SimpleEntry("param1", new String[]{"value1", "value2"}))), 8080, 200);
 assertTrue("Two AccessEvents with same fields but different order should be considered equal", event1.equals(event2));
 }

 @Test
 public void testHashCode_sameObject() {
 final AccessEvent event1 = this.event;
 int expectedHashCode = event1.hashCode();
 int actualHashCode = event1.hashCode();
 assertEquals("Two identical objects should have the same hashcode", expectedHashCode, actualHashCode);
 }

 @Test
 public void testHashCode_differentObjects() {
 final AccessEvent event1 = this.event;
 final AccessEvent event2 = new AccessEvent(2L, null, null, null, null, null, null, null, null, null, new HashMap<>(), new HashMap<>(), new HashMap<>(), 8080, 201);
 int expectedHashCode1 = event1.hashCode();
 int actualHashCode1 = event1.hashCode();
 int expectedHashCode2 = event2.hashCode();
 int actualHashCode2 = event2.hashCode();
 assertNotSame("Two different objects should not have the same hashcode", expectedHashCode1, expectedHashCode2);
 assertNotSame("Two different objects should not have the same hashcode", actualHashCode1, actualHashCode2);
 }

 @Test
 public void testToString() {
 String expectedToString = "AccessEvent[loggerContext=TestLogger, timeStamp=1]";
 String actualToString = event.toString();
 assertEquals("ToString method should return the correct string representation", expectedToString, actualToString);
 }
}