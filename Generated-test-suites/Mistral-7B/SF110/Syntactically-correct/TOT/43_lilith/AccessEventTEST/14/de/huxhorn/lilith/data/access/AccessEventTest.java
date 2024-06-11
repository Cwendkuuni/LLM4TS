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
 event.setTimeStamp(1L);
 }

 @Test
 public void testGetTimeStamp() {
 Long expected = 1L;
 Long actual = event.getTimeStamp();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetTimeStamp() {
 Long newTimeStamp = 2L;
 event.setTimeStamp(newTimeStamp);
 Long actual = event.getTimeStamp();
 assertEquals(newTimeStamp, actual);
 }

 @Test
 public void testGetLoggerContext() {
 LoggerContext expected = loggerContext;
 LoggerContext actual = event.getLoggerContext();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetLoggerContext() {
 LoggerContext newLoggerContext = new LoggerContext();
 event.setLoggerContext(newLoggerContext);
 LoggerContext actual = event.getLoggerContext();
 assertEquals(newLoggerContext, actual);
 }

 @Test
 public void testGetRequestURI() {
 String expected = "";
 String actual = event.getRequestURI();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRequestURI() {
 String newRequestURI = "/test";
 event.setRequestURI(newRequestURI);
 String actual = event.getRequestURI();
 assertEquals(newRequestURI, actual);
 }

 @Test
 public void testGetRequestURL() {
 String expected = "";
 String actual = event.getRequestURL();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRequestURL() {
 String newRequestURL = "http://test.com";
 event.setRequestURL(newRequestURL);
 String actual = event.getRequestURL();
 assertEquals(newRequestURL, actual);
 }

 @Test
 public void testGetRemoteHost() {
 String expected = "";
 String actual = event.getRemoteHost();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRemoteHost() {
 String newRemoteHost = "127.0.0.1";
 event.setRemoteHost(newRemoteHost);
 String actual = event.getRemoteHost();
 assertEquals(newRemoteHost, actual);
 }

 @Test
 public void testGetRemoteUser() {
 String expected = "";
 String actual = event.getRemoteUser();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRemoteUser() {
 String newRemoteUser = "testUser";
 event.setRemoteUser(newRemoteUser);
 String actual = event.getRemoteUser();
 assertEquals(newRemoteUser, actual);
 }

 @Test
 public void testGetProtocol() {
 String expected = "";
 String actual = event.getProtocol();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetProtocol() {
 String newProtocol = "HTTP/1.1";
 event.setProtocol(newProtocol);
 String actual = event.getProtocol();
 assertEquals(newProtocol, actual);
 }

 @Test
 public void testGetMethod() {
 String expected = "";
 String actual = event.getMethod();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetMethod() {
 String newMethod = "GET";
 event.setMethod(newMethod);
 String actual = event.getMethod();
 assertEquals(newMethod, actual);
 }

 @Test
 public void testGetServerName() {
 String expected = "";
 String actual = event.getServerName();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetServerName() {
 String newServerName = "testServer";
 event.setServerName(newServerName);
 String actual = event.getServerName();
 assertEquals(newServerName, actual);
 }

 @Test
 public void testGetRemoteAddress() {
 String expected = "";
 String actual = event.getRemoteAddress();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRemoteAddress() {
 String newRemoteAddress = "127.0.0.1";
 event.setRemoteAddress(newRemoteAddress);
 String actual = event.getRemoteAddress();
 assertEquals(newRemoteAddress, actual);
 }

 @Test
 public void testGetRequestHeaders() {
 Map<String, String> expected = new HashMap<>();
 Map<String, String> actual = event.getRequestHeaders();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRequestHeaders() {
 Map<String, String> newHeaders = new HashMap<>();
 newHeaders.put("testKey", "testValue");
 event.setRequestHeaders(newHeaders);
 Map<String, String> actual = event.getRequestHeaders();
 assertEquals(newHeaders, actual);
 }

 @Test
 public void testGetResponseHeaders() {
 Map<String, String> expected = new HashMap<>();
 Map<String, String> actual = event.getResponseHeaders();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetResponseHeaders() {
 Map<String, String> newHeaders = new HashMap<>();
 newHeaders.put("testKey", "testValue");
 event.setResponseHeaders(newHeaders);
 Map<String, String> actual = event.getResponseHeaders();
 assertEquals(newHeaders, actual);
 }

 @Test
 public void testGetRequestParameters() {
 Map<String, String[]> expected = new HashMap<>();
 Map<String, String[]> actual = event.getRequestParameters();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetRequestParameters() {
 Map<String, String[]> newParameters = new HashMap<>();
 newParameters.put("testParam", new String[]{"testValue1", "testValue2"});
 event.setRequestParameters(newParameters);
 Map<String, String[]> actual = event.getRequestParameters();
 assertEquals(newParameters, actual);
 }

 @Test
 public void testGetLocalPort() {
 int expected = 0;
 int actual = event.getLocalPort();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetLocalPort() {
 int newLocalPort = 8080;
 event.setLocalPort(newLocalPort);
 int actual = event.getLocalPort();
 assertEquals(newLocalPort, actual);
 }

 @Test
 public void testGetStatusCode() {
 int expected = 0;
 int actual = event.getStatusCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetStatusCode() {
 int newStatusCode = 200;
 event.setStatusCode(newStatusCode);
 int actual = event.getStatusCode();
 assertEquals(newStatusCode, actual);
 }

 @Test
 public void testEquals() {
 AccessEvent otherEvent = new AccessEvent();
 assertFalse(event.equals(otherEvent));
 otherEvent.setLoggerContext(loggerContext);
 otherEvent.setTimeStamp(1L);
 assertTrue(event.equals(otherEvent));
 }

 @Test
 public void testHashCode() {
 int expected = 0;
 int actual = event.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 StringBuilder expected = new StringBuilder();
 StringBuilder actual = event.toString();
 assertEquals(expected.toString(), actual.toString());
 }
}