package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class AccessEventTest {

 private AccessEvent accessEvent;
 private AccessEvent accessEvent2;

 @Before
 public void setUp() {
 accessEvent = new AccessEvent();
 accessEvent2 = new AccessEvent();

 accessEvent.setTimeStamp(1L);
 accessEvent.setLoggerContext(new LoggerContext());
 accessEvent.setRequestURI("/testURI");
 accessEvent.setRequestURL("http://test.com/testURI");
 accessEvent.setRemoteHost("127.0.0.1");
 accessEvent.setRemoteUser("testUser");
 accessEvent.setProtocol("HTTP/1.1");
 accessEvent.setMethod("GET");
 accessEvent.setServerName("testServer");
 accessEvent.setRemoteAddress("127.0.0.1");
 accessEvent.setLocalPort(8080);
 accessEvent.setStatusCode(200);

 accessEvent2.setTimeStamp(1L);
 accessEvent2.setLoggerContext(new LoggerContext());
 accessEvent2.setRequestURI("/testURI2");
 accessEvent2.setRequestURL("http://test.com/testURI2");
 accessEvent2.setRemoteHost("127.0.0.1");
 accessEvent2.setRemoteUser("testUser2");
 accessEvent2.setProtocol("HTTP/1.1");
 accessEvent2.setMethod("POST");
 accessEvent2.setServerName("testServer2");
 accessEvent2.setRemoteAddress("127.0.0.1");
 accessEvent2.setLocalPort(8081);
 accessEvent2.setStatusCode(201);
 }

 @Test
 public void testGetTimeStamp() {
 // Test getTimeStamp method
 Long expectedTimeStamp = 1L;
 Long accessEventTimeStamp = accessEvent.getTimeStamp();
 assertEquals("TimeStamp should be equal", expectedTimeStamp, accessEventTimeStamp);
 }

 @Test
 public void testSetTimeStamp() {
 // Test setTimeStamp method
 Long newTimeStamp = 2L;
 accessEvent.setTimeStamp(newTimeStamp);
 Long accessEventNewTimeStamp = accessEvent.getTimeStamp();
 assertEquals("New TimeStamp should be equal to set value", newTimeStamp, accessEventNewTimeStamp);
 }

 @Test
 public void testGetLoggerContext() {
 // Test getLoggerContext method
 LoggerContext expectedLoggerContext = accessEvent.getLoggerContext();
 assertEquals("Logger context should be equal", expectedLoggerContext, accessEvent2.getLoggerContext());
 }

 @Test
 public void testSetLoggerContext() {
 // Test setLoggerContext method
 LoggerContext newLoggerContext = new LoggerContext();
 accessEvent.setLoggerContext(newLoggerContext);
 LoggerContext accessEventNewLoggerContext = accessEvent.getLoggerContext();
 assertEquals("New Logger context should be equal to set value", newLoggerContext, accessEventNewLoggerContext);
 }

 @Test
 public void testGetRequestURI() {
 // Test getRequestURI method
 String expectedRequestURI = "/testURI";
 String accessEventRequestURI = accessEvent.getRequestURI();
 assertEquals("RequestURI should be equal", expectedRequestURI, accessEventRequestURI);
 }

 @Test
 public void testSetRequestURI() {
 // Test setRequestURI method
 String newRequestURI = "/testURI3";
 accessEvent.setRequestURI(newRequestURI);
 String accessEventNewRequestURI = accessEvent.getRequestURI();
 assertEquals("New RequestURI should be equal to set value", newRequestURI, accessEventNewRequestURI);
 }

 @Test
 public void testGetRequestURL() {
 // Test getRequestURL method
 String expectedRequestURL = "http://test.com/testURI";
 String accessEventRequestURL = accessEvent.getRequestURL();
 assertEquals("RequestURL should be equal", expectedRequestURL, accessEventRequestURL);
 }

 @Test
 public void testSetRequestURL() {
 // Test setRequestURL method
 String newRequestURL = "http://test.com/testURI4";
 accessEvent.setRequestURL(newRequestURL);
 String accessEventNewRequestURL = accessEvent.getRequestURL();
 assertEquals("New RequestURL should be equal to set value", newRequestURL, accessEventNewRequestURL);
 }

 @Test
 public void testGetRemoteHost() {
 // Test getRemoteHost method
 String expectedRemoteHost = "127.0.0.1";
 String accessEventRemoteHost = accessEvent.getRemoteHost();
 assertEquals("RemoteHost should be equal", expectedRemoteHost, accessEventRemoteHost);
 }

 @Test
 public void testSetRemoteHost() {
 // Test setRemoteHost method
 String newRemoteHost = "127.0.0.2";
 accessEvent.setRemoteHost(newRemoteHost);
 String accessEventNewRemoteHost = accessEvent.getRemoteHost();
 assertEquals("New RemoteHost should be equal to set value", newRemoteHost, accessEventNewRemoteHost);
 }

 @Test
 public void testGetRemoteUser() {
 // Test getRemoteUser method
 String expectedRemoteUser = "testUser";
 String accessEventRemoteUser = accessEvent.getRemoteUser();
 assertEquals("RemoteUser should be equal", expectedRemoteUser, accessEventRemoteUser);
 }

 @Test
 public void testSetRemoteUser() {
 // Test setRemoteUser method
 String newRemoteUser = "testUser3";
 accessEvent.setRemoteUser(newRemoteUser);
 String accessEventNewRemoteUser = accessEvent.getRemoteUser();
 assertEquals("New RemoteUser should be equal to set value", newRemoteUser, accessEventNewRemoteUser);
 }

 @Test
 public void testGetProtocol() {
 // Test getProtocol method
 String expectedProtocol = "HTTP/1.1";
 String accessEventProtocol = accessEvent.getProtocol();
 assertEquals("Protocol should be equal", expectedProtocol, accessEventProtocol);
 }

 @Test
 public void testSetProtocol() {
 // Test setProtocol method
 String newProtocol = "HTTPS/1.1";
 accessEvent.setProtocol(newProtocol);
 String accessEventNewProtocol = accessEvent.getProtocol();
 assertEquals("New Protocol should be equal to set value", newProtocol, accessEventNewProtocol);
 }

 @Test
 public void testGetMethod() {
 // Test getMethod method
 String expectedMethod = "GET";
 String accessEventMethod = accessEvent.getMethod();
 assertEquals("Method should be equal", expectedMethod, accessEventMethod);
 }

 @Test
 public void testSetMethod() {
 // Test setMethod method
 String newMethod = "POST";
 accessEvent.setMethod(newMethod);
 String accessEventNewMethod = accessEvent.getMethod();
 assertEquals("New Method should be equal to set value", newMethod, accessEventNewMethod);
 }

 @Test
 public void testGetServerName() {
 // Test getServerName method
 String expectedServerName = "testServer";
 String accessEventServerName = accessEvent.getServerName();
 assertEquals("ServerName should be equal", expectedServerName, accessEventServerName);
 }

 @Test
 public void testSetServerName() {
 // Test setServerName method
 String newServerName = "testServer3";
 accessEvent.setServerName(newServerName);
 String accessEventNewServerName = accessEvent.getServerName();
 assertEquals("New ServerName should be equal to set value", newServerName, accessEventNewServerName);
 }

 @Test
 public void testGetRemoteAddress() {
 // Test getRemoteAddress method
 String expectedRemoteAddress = "127.0.0.1";
 String accessEventRemoteAddress = accessEvent.getRemoteAddress();
 assertEquals("RemoteAddress should be equal", expectedRemoteAddress, accessEventRemoteAddress);
 }

 @Test
 public void testSetRemoteAddress() {
 // Test setRemoteAddress method
 String newRemoteAddress = "127.0.0.2";
 accessEvent.setRemoteAddress(newRemoteAddress);
 String accessEventNewRemoteAddress = accessEvent.getRemoteAddress();
 assertEquals("New RemoteAddress should be equal to set value", newRemoteAddress, accessEventNewRemoteAddress);
 }

 @Test
 public void testGetRequestHeaders() {
 // Test getRequestHeaders method
 Map<String, String> expectedHeaders = new HashMap<>();
 Map<String, String> accessEventHeaders = accessEvent.getRequestHeaders();
 assertEquals("Headers should be equal", expectedHeaders, accessEventHeaders);
 }

 @Test
 public void testSetRequestHeaders() {
 // Test setRequestHeaders method
 Map<String, String> newHeaders = new HashMap<>();
 accessEvent.setRequestHeaders(newHeaders);
 Map<String, String> accessEventNewHeaders = accessEvent.getRequestHeaders();
 assertEquals("New Headers should be equal to set value", newHeaders, accessEventNewHeaders);
 }

 @Test
 public void testGetResponseHeaders() {
 // Test getResponseHeaders method
 Map<String, String> expectedHeaders = new HashMap<>();
 Map<String, String> accessEventHeaders = accessEvent.getResponseHeaders();
 assertEquals("Headers should be equal", expectedHeaders, accessEventHeaders);
 }

 @Test
 public void testSetResponseHeaders() {
 // Test setResponseHeaders method
 Map<String, String> newHeaders = new HashMap<>();
 accessEvent.setResponseHeaders(newHeaders);
 Map<String, String> accessEventNewHeaders = accessEvent.getResponseHeaders();
 assertEquals("New Headers should be equal to set value", newHeaders, accessEventNewHeaders);
 }

 @Test
 public void testGetRequestParameters() {
 // Test getRequestParameters method
 Map<String, String[]> expectedParameters = new HashMap<>();
 Map<String, String[]> accessEventParameters = accessEvent.getRequestParameters();
 assertEquals("Parameters should be equal", expectedParameters, accessEventParameters);
 }

 @Test
 public void testSetRequestParameters() {
 // Test setRequestParameters method
 Map<String, String[]> newParameters = new HashMap<>();
 accessEvent.setRequestParameters(newParameters);
 Map<String, String[]> accessEventNewParameters = accessEvent.getRequestParameters();
 assertEquals("New Parameters should be equal to set value", newParameters, accessEventNewParameters);
 }

 @Test
 public void testGetLocalPort() {
 // Test getLocalPort method
 int expectedLocalPort = 8080;
 int accessEventLocalPort = accessEvent.getLocalPort();
 assertEquals("LocalPort should be equal", expectedLocalPort, accessEventLocalPort);
 }

 @Test
 public void testSetLocalPort() {
 // Test setLocalPort method
 int newLocalPort = 8082;
 accessEvent.setLocalPort(newLocalPort);
 int accessEventNewLocalPort = accessEvent.getLocalPort();
 assertEquals("New LocalPort should be equal to set value", newLocalPort, accessEventNewLocalPort);
 }

 @Test
 public void testGetStatusCode() {
 // Test getStatusCode method
 int expectedStatusCode = 200;
 int accessEventStatusCode = accessEvent.getStatusCode();
 assertEquals("StatusCode should be equal", expectedStatusCode, accessEventStatusCode);
 }

 @Test
 public void testSetStatusCode() {
 // Test setStatusCode method
 int newStatusCode = 202;
 accessEvent.setStatusCode(newStatusCode);
 int accessEventNewStatusCode = accessEvent.getStatusCode();
 assertEquals("New StatusCode should be equal to set value", newStatusCode, accessEventNewStatusCode);
 }

 @Test
 public void testEquals() {
 // Test equals method
 AccessEvent accessEvent3 = new AccessEvent();
 accessEvent3.setTimeStamp(1L);
 accessEvent3.setLoggerContext(new LoggerContext());
 accessEvent3.setRequestURI("/testURI");
 accessEvent3.setRequestURL("http://test.com/testURI");
 accessEvent3.setRemoteHost("127.0.0.1");
 accessEvent3.setRemoteUser("testUser");
 accessEvent3.setProtocol("HTTP/1.1");
 accessEvent3.setMethod("GET");
 accessEvent3.setServerName("testServer");
 accessEvent3.setRemoteAddress("127.0.0.1");
 accessEvent3.setLocalPort(8080);
 accessEvent3.setStatusCode(200);

 boolean result = accessEvent.equals(accessEvent3);
 assertTrue("AccessEvents should be equal", result);

 AccessEvent accessEvent4 = new AccessEvent();
 accessEvent4.setTimeStamp(1L);
 accessEvent4.setLoggerContext(new LoggerContext());
 accessEvent4.setRequestURI("/testURI2");
 accessEvent4.setRequestURL("http://test.com/testURI2");
 accessEvent4.setRemoteHost("127.0.0.1");
 accessEvent4.setRemoteUser("testUser2");
 accessEvent4.setProtocol("HTTP/1.1");
 accessEvent4.setMethod("POST");
 accessEvent4.setServerName("testServer2");
 accessEvent4.setRemoteAddress("127.0.0.1");
 accessEvent4.setLocalPort(8081);
 accessEvent4.setStatusCode(201);

 boolean result2 = accessEvent.equals(accessEvent4);
 assertFalse("AccessEvents should not be equal", result2);
 }

 @Test
 public void testHashCode() {
 // Test hashCode method
 int expectedHashCode = 861618041;
 int accessEventHashCode = accessEvent.hashCode();
 assertEquals("HashCode should be equal", expectedHashCode, accessEventHashCode);
 }

 @Test
 public void testToString() {
 // Test toString method
 String expectedToString = "AccessEvent[loggerContext=LoggerContext{}, timeStamp=1]";
 String accessEventToString = accessEvent.toString();
 assertEquals("ToString should be equal", expectedToString, accessEventToString);
 }
}