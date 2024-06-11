package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();

        assertNotEquals(event1, event2);
        assertNotEquals(event1, event3);

        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        event3.setTimeStamp(2L);

        assertNotEquals(event1, event2);
        assertNotEquals(event1, event3);

        event2.setTimeStamp(1L);

        assertEquals(event1, event2);
        assertEquals(event2, event1);

        event3.setTimeStamp(1L);
        assertEquals(event1, event3);
        assertEquals(event3, event1);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event3.setLoggerContext(null);

        assertNotEquals(event1, event2);
        assertNotEquals(event1, event3);

        event2.setLoggerContext(event1.getLoggerContext());
        assertEquals(event1, event2);
        assertEquals(event2, event1);

        event3.setLoggerContext(event1.getLoggerContext());
        assertEquals(event1, event3);
        assertEquals(event3, event1);

        event1.setMethod("GET");
        event2.setMethod("POST");
        event3.setMethod("GET");

        assertNotEquals(event1, event2);
        assertNotEquals(event1, event3);

        event2.setMethod("GET");
        assertEquals(event1, event2);
        assertEquals(event2, event1);

        event3.setMethod("GET");
        assertEquals(event1, event3);
        assertEquals(event3, event1);
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        AccessEvent event3 = new AccessEvent();

        assertEquals(event1.hashCode(), event1.hashCode());

        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        event3.setTimeStamp(2L);

        assertNotEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());

        event2.setTimeStamp(1L);

        assertEquals(event1.hashCode(), event2.hashCode());
        assertEquals(event2.hashCode(), event1.hashCode());

        event3.setTimeStamp(1L);
        assertEquals(event1.hashCode(), event3.hashCode());
        assertEquals(event3.hashCode(), event1.hashCode());

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        event3.setLoggerContext(null);

        assertNotEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());

        event2.setLoggerContext(event1.getLoggerContext());
        assertEquals(event1.hashCode(), event2.hashCode());
        assertEquals(event2.hashCode(), event1.hashCode());

        event3.setLoggerContext(event1.getLoggerContext());
        assertEquals(event1.hashCode(), event3.hashCode());
        assertEquals(event3.hashCode(), event1.hashCode());

        event1.setMethod("GET");
        event2.setMethod("POST");
        event3.setMethod("GET");

        assertNotEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());

        event2.setMethod("GET");
        assertEquals(event1.hashCode(), event2.hashCode());
        assertEquals(event2.hashCode(), event1.hashCode());

        event3.setMethod("GET");
        assertEquals(event1.hashCode(), event3.hashCode());
        assertEquals(event3.hashCode(), event1.hashCode());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        AccessEvent event = new AccessEvent();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(event);

        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        AccessEvent deserializedEvent = (AccessEvent)ois.readObject();

        assertEquals(event, deserializedEvent);
    }

    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();

        event.setTimeStamp(1L);
        assertEquals(1L, event.getTimeStamp());

        event.setLoggerContext(new LoggerContext());
        assertEquals(event.getLoggerContext(), event.getLoggerContext());

        String requestURI = "testRequestURI";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());

        String requestURL = "testRequestURL";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());

        String remoteHost = "testRemoteHost";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());

        String remoteUser = "testRemoteUser";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());

        String protocol = "testProtocol";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());

        String method = "testMethod";
        event.setMethod(method);
        assertEquals(method, event.getMethod());

        String serverName = "testServerName";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());

        String remoteAddress = "testRemoteAddress";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("testKey1", "testValue1");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("testKey2", "testValue2");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("testKey3", new String[]{"testValue3", "testValue4"});
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());

        int localPort = 1234;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());

        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();

        String expected = "AccessEvent[loggerContext=, timeStamp=]";
        String result = event.toString();

        assertTrue(expected.equals(result));

        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());

        expected = "AccessEvent[loggerContext=" + event.getLoggerContext() + ", timeStamp=" + event.getTimeStamp() + "]";
        result = event.toString();

        assertTrue(expected.equals(result));
    }

    @Test
    public void testEmptyConstructors() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertEquals(event1, event2);
        assertEquals(event2, event1);
    }

    @Test
    public void testCopyConstructor() {
        AccessEvent originalEvent = new AccessEvent();
        originalEvent.setTimeStamp(1L);
        originalEvent.setLoggerContext(new LoggerContext());

        AccessEvent copyEvent = new AccessEvent(originalEvent);

        assertEquals(originalEvent, copyEvent);
        assertEquals(copyEvent, originalEvent);
    }

    @Test
    public void testAccessEventWithNullValues() {
        AccessEvent event = new AccessEvent();

        event.setLoggerContext(null);
        event.setRequestURI(null);
        event.setRequestURL(null);
        event.setRemoteHost(null);
        event.setRemoteUser(null);
        event.setProtocol(null);
        event.setMethod(null);
        event.setServerName(null);
        event.setRemoteAddress(null);
        event.setRequestHeaders(null);
        event.setResponseHeaders(null);
        event.setRequestParameters(null);
        event.setLocalPort(-1);
        event.setStatusCode(-1);

        Map<String, String> emptyMap = Collections.emptyMap();
        Map<String, String[]> emptyMapArray = Collections.emptyMap();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put(null, null);

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put(null, new String[] { null });

        assertEquals(emptyMap, event.getRequestHeaders());
        assertEquals(emptyMapArray, event.getRequestParameters());

        event.setRequestHeaders(requestHeaders);
        event.setRequestParameters(requestParameters);

        assertEquals(requestHeaders, event.getRequestHeaders());
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testAccessEventWithEmptyStrings() {
        AccessEvent event = new AccessEvent();

        event.setRequestURI("");
        event.setRequestURL("");
        event.setRemoteHost("");
        event.setRemoteUser("");
        event.setProtocol("");
        event.setMethod("");
        event.setServerName("");
        event.setRemoteAddress("");

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("", "");

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("", new String[] { "", "" });

        assertEquals(requestHeaders, event.getRequestHeaders());
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testToStringWithNullValues() {
        AccessEvent event = new AccessEvent();

        event.setLoggerContext(null);
        event.setRequestURI(null);
        event.setRequestURL(null);
        event.setRemoteHost(null);
        event.setRemoteUser(null);
        event.setProtocol(null);
        event.setMethod(null);
        event.setServerName(null);
        event.setRemoteAddress(null);
        event.setRequestHeaders(null);
        event.setResponseHeaders(null);
        event.setRequestParameters(null);
        event.setLocalPort(-1);
        event.setStatusCode(-1);

        String expected = "AccessEvent[loggerContext=null, timeStamp=null]";
        String result = event.toString();

        assertTrue(expected.equals(result));
    }
}