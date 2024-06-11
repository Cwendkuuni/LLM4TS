package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertNotSame(event1, event2);

        event1.setTimeStamp(1L);
        event2.setTimeStamp(1L);
        assertEquals(event1, event2);

        event2.setTimeStamp(2L);
        assertNotSame(event1, event2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        assertEquals(event1, event2);

        event2.setLoggerContext(new LoggerContext());
        assertNotEquals(event1, event2);

        event1.setRequestURI("/test");
        event2.setRequestURI("/test");
        assertEquals(event1, event2);

        event2.setRequestURI("/test2");
        assertNotEquals(event1, event2);

        event1.setRequestURL("http://test.com/test");
        event2.setRequestURL("http://test.com/test");
        assertEquals(event1, event2);

        event2.setRequestURL("http://test.com/test2");
        assertNotEquals(event1, event2);

        event1.setRemoteHost("localhost");
        event2.setRemoteHost("localhost");
        assertEquals(event1, event2);

        event2.setRemoteHost("127.0.0.1");
        assertNotEquals(event1, event2);

        event1.setRemoteUser("user");
        event2.setRemoteUser("user");
        assertEquals(event1, event2);

        event2.setRemoteUser("user2");
        assertNotEquals(event1, event2);

        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        assertEquals(event1, event2);

        event2.setProtocol("HTTP/1.2");
        assertNotEquals(event1, event2);

        event1.setMethod("GET");
        event2.setMethod("GET");
        assertEquals(event1, event2);

        event2.setMethod("POST");
        assertNotEquals(event1, event2);

        event1.setServerName("test.com");
        event2.setServerName("test.com");
        assertEquals(event1, event2);

        event2.setServerName("test2.com");
        assertNotEquals(event1, event2);

        event1.setRemoteAddress("127.0.0.1");
        event2.setRemoteAddress("127.0.0.1");
        assertEquals(event1, event2);

        event2.setRemoteAddress("127.0.0.2");
        assertNotEquals(event1, event2);

        Map<String, String> headers1 = new HashMap<>();
        Map<String, String> headers2 = new HashMap<>();
        headers1.put("Header1", "Value1");
        headers2.put("Header1", "Value1");
        event1.setRequestHeaders(headers1);
        event2.setRequestHeaders(headers2);
        assertEquals(event1, event2);

        headers2.put("Header2", "Value2");
        assertNotEquals(event1, event2);

        Map<String, String> parameters1 = new HashMap<>();
        Map<String, String[]> parameters2 = new HashMap<>();
        parameters1.put("param1", new String[]{"value1", "value2"});
        parameters2.put("param1", new String[]{"value1", "value2"});
        event1.setRequestParameters(parameters1);
        event2.setRequestParameters(parameters2);
        assertEquals(event1, event2);

        parameters2.put("param2", new String[]{"value3"});
        assertNotEquals(event1, event2);

        event1.setLocalPort(8080);
        event2.setLocalPort(8080);
        assertEquals(event1, event2);

        event2.setLocalPort(80);
        assertNotEquals(event1, event2);

        event1.setStatusCode(200);
        event2.setStatusCode(200);
        assertEquals(event1, event2);

        event2.setStatusCode(404);
        assertNotEquals(event1, event2);
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        int hash1 = event1.hashCode();
        event1.setTimeStamp(1L);
        int hash2 = event1.hashCode();
        assertEquals(hash1, hash2);

        event1.setLoggerContext(new LoggerContext());
        event2.setLoggerContext(new LoggerContext());
        int hash3 = event1.hashCode();
        int hash4 = event2.hashCode();
        assertEquals(hash3, hash4);

        event2.setLoggerContext(new LoggerContext());
        int hash5 = event1.hashCode();
        assertNotEquals(hash3, hash5);

        event1.setRequestURI("/test");
        event2.setRequestURI("/test");
        int hash6 = event1.hashCode();
        int hash7 = event2.hashCode();
        assertEquals(hash6, hash7);

        event2.setRequestURI("/test2");
        int hash8 = event1.hashCode();
        assertNotEquals(hash6, hash8);

        event1.setRequestURL("http://test.com/test");
        event2.setRequestURL("http://test.com/test");
        int hash9 = event1.hashCode();
        int hash10 = event2.hashCode();
        assertEquals(hash9, hash10);

        event2.setRequestURL("http://test.com/test2");
        int hash11 = event1.hashCode();
        assertNotEquals(hash9, hash11);

        event1.setRemoteHost("localhost");
        event2.setRemoteHost("localhost");
        int hash12 = event1.hashCode();
        int hash13 = event2.hashCode();
        assertEquals(hash12, hash13);

        event2.setRemoteHost("127.0.0.1");
        int hash14 = event1.hashCode();
        assertNotEquals(hash12, hash14);

        event1.setRemoteUser("user");
        event2.setRemoteUser("user");
        int hash15 = event1.hashCode();
        int hash16 = event2.hashCode();
        assertEquals(hash15, hash16);

        event2.setRemoteUser("user2");
        int hash17 = event1.hashCode();
        assertNotEquals(hash15, hash17);

        event1.setProtocol("HTTP/1.1");
        event2.setProtocol("HTTP/1.1");
        int hash18 = event1.hashCode();
        int hash19 = event2.hashCode();
        assertEquals(hash18, hash19);

        event2.setProtocol("HTTP/1.2");
        int hash20 = event1.hashCode();
        assertNotEquals(hash18, hash20);

        event1.setMethod("GET");
        event2.setMethod("GET");
        int hash21 = event1.hashCode();
        int hash22 = event2.hashCode();
        assertEquals(hash21, hash22);

        event2.setMethod("POST");
        int hash23 = event1.hashCode();
        assertNotEquals(hash21, hash23);

        event1.setServerName("test.com");
        event2.setServerName("test.com");
        int hash24 = event1.hashCode();
        int hash25 = event2.hashCode();
        assertEquals(hash24, hash25);

        event2.setServerName("test2.com");
        int hash26 = event1.hashCode();
        assertNotEquals(hash24, hash26);

        event1.setRemoteAddress("127.0.0.1");
        event2.setRemoteAddress("127.0.0.1");
        int hash27 = event1.hashCode();
        int hash28 = event2.hashCode();
        assertEquals(hash27, hash28);

        event2.setRemoteAddress("127.0.0.2");
        int hash29 = event1.hashCode();
        assertNotEquals(hash27, hash29);

        Map<String, String> headers1 = new HashMap<>();
        Map<String, String> headers2 = new HashMap<>();
        headers1.put("Header1", "Value1");
        headers2.put("Header1", "Value1");
        event1.setRequestHeaders(headers1);
        event2.setRequestHeaders(headers2);
        int hash30 = event1.hashCode();
        int hash31 = event2.hashCode();
        assertEquals(hash30, hash31);

        headers2.put("Header2", "Value2");
        int hash32 = event1.hashCode();
        assertNotEquals(hash30, hash32);

        Map<String, String> parameters1 = new HashMap<>();
        Map<String, String[]> parameters2 = new HashMap<>();
        parameters1.put("param1", new String[]{"value1", "value2"});
        parameters2.put("param1", new String[]{"value1", "value2"});
        event1.setRequestParameters(parameters1);
        event2.setRequestParameters(parameters2);
        int hash33 = event1.hashCode();
        int hash34 = event2.hashCode();
        assertEquals(hash33, hash34);

        parameters2.put("param2", new String[]{"value3"});
        int hash35 = event1.hashCode();
        assertNotEquals(hash33, hash35);

        event1.setLocalPort(8080);
        event2.setLocalPort(8080);
        int hash36 = event1.hashCode();
        int hash37 = event2.hashCode();
        assertEquals(hash36, hash37);

        event2.setLocalPort(80);
        int hash38 = event1.hashCode();
        assertNotEquals(hash36, hash38);

        event1.setStatusCode(200);
        event2.setStatusCode(200);
        int hash39 = event1.hashCode();
        int hash40 = event2.hashCode();
        assertEquals(hash39, hash40);

        event2.setStatusCode(404);
        int hash41 = event1.hashCode();
        assertNotEquals(hash39, hash41);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        assertEquals("AccessEvent[]", event.toString());

        event.setTimeStamp(1L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("/test");
        event.setRequestURL("http://test.com/test");
        event.setRemoteHost("localhost");
        event.setRemoteUser("user");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("test.com");
        event.setRemoteAddress("127.0.0.1");
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        event.setRequestHeaders(headers);
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("param1", new String[]{"value1", "value2"});
        event.setRequestParameters(parameters);
        event.setLocalPort(8080);
        event.setStatusCode(200);

        String expected = "AccessEvent[loggerContext=LoggerContext@5d3163f9, timeStamp=1, " +
                "]";
        assertEquals(expected, event.toString());
    }
}