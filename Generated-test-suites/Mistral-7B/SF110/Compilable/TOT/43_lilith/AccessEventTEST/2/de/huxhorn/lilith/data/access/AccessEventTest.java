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
 public void setup() {
 loggerContext = new LoggerContext();
 event = new AccessEvent();
 event.setLoggerContext(loggerContext);
 event.setTimeStamp(System.currentTimeMillis());
 }

 @Test
 public void testGettersAndSetters_timeStamp() {
 Long expectedTimeStamp = System.currentTimeMillis();
 event.setTimeStamp(expectedTimeStamp);
 Long actualTimeStamp = event.getTimeStamp();
 assertEquals(expectedTimeStamp, actualTimeStamp);
 }

 @Test
 public void testGettersAndSetters_loggerContext() {
 LoggerContext expectedLoggerContext = new LoggerContext();
 event.setLoggerContext(expectedLoggerContext);
 LoggerContext actualLoggerContext = event.getLoggerContext();
 assertEquals(expectedLoggerContext, actualLoggerContext);
 }

 @Test
 public void testGettersAndSetters_requestURI() {
 String expectedRequestURI = "/test";
 event.setRequestURI(expectedRequestURI);
 String actualRequestURI = event.getRequestURI();
 assertEquals(expectedRequestURI, actualRequestURI);
 }

 @Test
 public void testGettersAndSetters_requestURL() {
 String expectedRequestURL = "http://test.com/test";
 event.setRequestURL(expectedRequestURL);
 String actualRequestURL = event.getRequestURL();
 assertEquals(expectedRequestURL, actualRequestURL);
 }

 @Test
 public void testGettersAndSetters_remoteHost() {
 String expectedRemoteHost = "127.0.0.1";
 event.setRemoteHost(expectedRemoteHost);
 String actualRemoteHost = event.getRemoteHost();
 assertEquals(expectedRemoteHost, actualRemoteHost);
 }

 @Test
 public void testGettersAndSetters_remoteUser() {
 String expectedRemoteUser = "testUser";
 event.setRemoteUser(expectedRemoteUser);
 String actualRemoteUser = event.getRemoteUser();
 assertEquals(expectedRemoteUser, actualRemoteUser);
 }

 @Test
 public void testGettersAndSetters_protocol() {
 String expectedProtocol = "HTTP/1.1";
 event.setProtocol(expectedProtocol);
 String actualProtocol = event.getProtocol();
 assertEquals(expectedProtocol, actualProtocol);
 }

 @Test
 public void testGettersAndSetters_method() {
 String expectedMethod = "GET";
 event.setMethod(expectedMethod);
 String actualMethod = event.getMethod();
 assertEquals(expectedMethod, actualMethod);
 }

 @Test
 public void testGettersAndSetters_serverName() {
 String expectedServerName = "test.com";
 event.setServerName(expectedServerName);
 String actualServerName = event.getServerName();
 assertEquals(expectedServerName, actualServerName);
 }

 @Test
 public void testGettersAndSetters_remoteAddress() {
 String expectedRemoteAddress = "127.0.0.1";
 event.setRemoteAddress(expectedRemoteAddress);
 String actualRemoteAddress = event.getRemoteAddress();
 assertEquals(expectedRemoteAddress, actualRemoteAddress);
 }

 @Test
 public void testGettersAndSetters_requestHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("X-Forwarded-For", "127.0.0.1");
 event.setRequestHeaders(expectedHeaders);
 Map<String, String> actualHeaders = event.getRequestHeaders();
 assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testGettersAndSetters_responseHeaders() {
 Map<String, String> expectedHeaders = new HashMap<>();
 expectedHeaders.put("Content-Type", "application/json");
 event.setResponseHeaders(expectedHeaders);
 Map<String, String> actualHeaders = event.getResponseHeaders();
 assertEquals(expectedHeaders, actualHeaders);
 }

 @Test
 public void testGettersAndSetters_requestParameters() {
 Map<String, String[]> expectedParameters = new HashMap<>();
 expectedParameters.put("param", new String[]{"value1", "value2"});
 event.setRequestParameters(expectedParameters);
 Map<String, String[]> actualParameters = event.getRequestParameters();
 assertEquals(expectedParameters, actualParameters);
 }

 @Test
 public void testGettersAndSetters_localPort() {
 int expectedLocalPort = 8080;
 event.setLocalPort(expectedLocalPort);
 int actualLocalPort = event.getLocalPort();
 assertEquals(expectedLocalPort, actualLocalPort);
 }

 @Test
 public void testGettersAndSetters_statusCode() {
 int expectedStatusCode = 200;
 event.setStatusCode(expectedStatusCode);
 int actualStatusCode = event.getStatusCode();
 assertEquals(expectedStatusCode, actualStatusCode);
 }

 @Test
 public void testEquals() {
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setTimeStamp(System.currentTimeMillis());
 anotherEvent.setLoggerContext(loggerContext);
 assertTrue(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentLoggerContext() {
 LoggerContext anotherLoggerContext = new LoggerContext();
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setLoggerContext(anotherLoggerContext);
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentMethod() {
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setMethod("POST");
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentRemoteAddress() {
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setRemoteAddress("127.0.0.2");
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentRemoteHost() {
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setRemoteHost("192.168.1.1");
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentRemoteUser() {
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setRemoteUser("testUser2");
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentRequestHeaders() {
 Map<String, String> anotherHeaders = new HashMap<>();anotherHeaders.put("X-Forwarded-For", "127.0.0.2");
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setRequestHeaders(anotherHeaders);
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentRequestURI() {
 String anotherRequestURI = "/test2";
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setRequestURI(anotherRequestURI);
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentRequestURL() {
 String anotherRequestURL = "http://test2.com/test2";
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setRequestURL(anotherRequestURL);
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentResponseHeaders() {
 Map<String, String> anotherHeaders = new HashMap<>();anotherHeaders.put("Content-Type", "application/xml");
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setResponseHeaders(anotherHeaders);
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentServerName() {
 String anotherServerName = "test2.com";
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setServerName(anotherServerName);
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testEquals_differentTimeStamp() {
 Long anotherTimeStamp = System.currentTimeMillis() - 1000;
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setTimeStamp(anotherTimeStamp);
 assertFalse(event.equals(anotherEvent));
 }

 @Test
 public void testHashCode_sameObjects() {
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setTimeStamp(System.currentTimeMillis());
 anotherEvent.setLoggerContext(loggerContext);
 assertEquals(event.hashCode(), anotherEvent.hashCode());
 }

 @Test
 public void testHashCode_differentObjects() {
 AccessEvent anotherEvent = new AccessEvent();anotherEvent.setLoggerContext(new LoggerContext());
 anotherEvent.setTimeStamp(System.currentTimeMillis() + 1000);
 assertNotEquals(event.hashCode(), anotherEvent.hashCode());
 }
}