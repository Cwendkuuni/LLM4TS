package org.scribe.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Test;
import org.scribe.exceptions.*;
import org.scribe.utils.*;
import org.scribe.model.Request;
import org.scribe.model.Verb;

public class RequestTest {

    @Test
    public void testInitialization() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertTrue(request.connectionKeepAlive == false);
        assertEquals(0, request.connection.getDoOutput());
        assertNull(request.headers);
        assertEmpty(request.getQueryStringParams());
        assertEmpty(request.getBodyParams());
    }

    @Test
    public void testSend() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        Request request = new Request(Verb.GET, "http://example.com");
        when(connectionMock.setRequestProperty(eq("Content-Length"), anyString())).thenReturn(connectionMock);
        when(connectionMock.setDoOutput(true)).thenReturn(connectionMock);
        when(connectionMock.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
            }
        });
        when(connectionMock.getInputStream()).thenReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        });

        Request requestWithHeaders = new Request(Verb.GET, "http://example.com")
                .addHeader("Header1", "Value1")
                .addHeader("Header2", "Value2");

        Request requestWithBody = new Request(Verb.POST, "http://example.com")
                .addBodyParameter("Param1", "Value1")
                .addPayload("Payload1");

        when(connectionMock.setRequestProperty(eq("Content-Type"), eq("application/x-www-form-urlencoded")))
                .thenReturn(connectionMock);

        Response response = requestWithBody.send();

        verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), eq("Payload1.length()"));
        verify(connectionMock, times(1)).setDoOutput(true);
        verify(connectionMock, times(1)).getOutputStream().write(any(byte[].class));
        verify(connectionMock, times(1)).setRequestProperty(eq("Content-Type"), eq("application/x-www-form-urlencoded"));

        assertEquals(response.getResponseCode(), HttpURLConnection.HTTP_OK);
        assertEquals(response.getContentType(), "text/html; charset=ISO-8859-1");
        assertNotNull(response.getInputStream());

        response = request.send();
        verify(connectionMock, never()).setRequestProperty(eq("Content-Length"), anyString());
        verify(connectionMock, never()).setDoOutput(true);
        verify(connectionMock, never()).getOutputStream().write(any(byte[].class));

        assertEquals(response.getResponseCode(), HttpURLConnection.HTTP_OK);
        assertEquals(response.getContentType(), "text/plain; charset=UTF-8");
        assertNull(response.getInputStream());

        response = requestWithHeaders.send();
        verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), eq("0"));
        verify(connectionMock, times(1)).setDoOutput(true);
        verify(connectionMock, times(1)).getOutputStream().write(any(byte[].class));
        verify(connectionMock, times(1)).setRequestProperty(eq("Content-Type"), eq("application/x-www-form-urlencoded"));

        assertEquals(response.getResponseCode(), HttpURLConnection.HTTP_OK);
        assertEquals(response.getContentType(), "text/html; charset=ISO-8859-1");
        assertNotNull(response.getInputStream());
    }

    @Test(expected = OAuthException.class)
    public void testSendWithException() throws Exception {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        Request request = new Request(Verb.GET, "http://example.com");

        when(connectionMock.setRequestProperty(eq("Content-Length"), anyString()))
                .thenThrow(new IOException("Connection failed"));

        request.send();

        verify(connectionMock, times(1)).setRequestProperty(eq("Content-Length"), anyString());
        verify(connectionMock, times(1)).setDoOutput(eq(false));
        verify(connectionMock, never()).getOutputStream();
        verify(connectionMock, never()).connect();

        throw new OAuthException("Problems while creating connection", new IOException("Connection failed"));
    }

    @Test
    public void testCreateConnection() throws Exception {
        Request request = new Request(Verb.GET, "http://example.com");

        URL url = new URL("http://example.com?param1=value1&param2=value2");
        String effectiveUrl = URLUtils.appendParametersToQueryString(url.toString(), request.querystringParams);
        assertEquals(effectiveUrl, request.createConnection().getURL().toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateConnectionWithBadURL() throws Exception {
        Request request = new Request(Verb.GET, "http://example.com");

        final String urlString = "http://example.com://example.com";
        try {
            request.createConnection();
            fail();
        } catch (Exception e) {
            assertEquals(MalformedURLException.class, e.getClass());
        }
    }

    @Test
    public void testAddHeaders() throws Exception {
        Request request = new Request(Verb.GET, "http://example.com")
                .addHeader("Header1", "Value1")
                .addHeader("Header2", "Value2");

        assertEquals("Value1", request.headers.get("Header1"));
        assertEquals("Value2", request.headers.get("Header2"));
        assertEquals(2, request.headers.size());
    }

    @Test
    public void testAddBody() throws Exception {
        Request request = new Request(Verb.POST, "http://example.com")
                .addBodyParameter("Param1", "Value1")
                .addPayload("Payload1");

        String encodedBody = URLUtils.formURLEncodeMap(request.bodyParams);
        byte[] byteBody = request.getByteBodyContents();

        assertEquals(encodedBody, new String(byteBody, StandardCharsets.UTF_8));
    }

    @Test
    public void testGetQueryStringParams() throws Exception {
        Request request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");

        Map<String, String> params = request.getQueryStringParams();
        assertEquals("value1", params.get("param1"));
        assertEquals("value2", params.get("param2"));
        assertEquals(2, params.size());
    }

    @Test
    public void testGetBodyParams() throws Exception {
        Request request = new Request(Verb.POST, "http://example.com")
                .addBodyParameter("Param1", "Value1")
                .addBodyParameter("Param2", "Value2");

        Map<String, String> params = request.getBodyParams();
        assertEquals("Value1", params.get("Param1"));
        assertEquals("Value2", params.get("Param2"));
        assertEquals(2, params.size());
    }

    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("http://example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws UnsupportedEncodingException {
        Request request = new Request(Verb.GET, "http://example.com")
                .addPayload("Payload1");

        String body = request.getBodyContents();
        assertEquals("Payload1", body);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetBodyContentsWithUnsupportedCharset() throws UnsupportedEncodingException {
        Request request = new Request(Verb.GET, "http://example.com")
                .addPayload("Payload1");

        request.setCharset("unsupportedCharset");
        request.getBodyContents();

        fail();
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.POST, "http://example.com");
        assertEquals(Verb.POST, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://example.com")
                .addHeader("Header1", "Value1")
                .addHeader("Header2", "Value2");

        Map<String, String> headers = request.getHeaders();
        assertEquals("Value1", headers.get("Header1"));
        assertEquals("Value2", headers.get("Header2"));
        assertEquals(2, headers.size());
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com")
                .setCharset("UTF-8");

        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");

        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");

        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");

        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.charset);
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");

        request.setConnectionKeepAlive(true);
        assertTrue(request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        Request request = new Request(Verb.GET, "http://example.com");

        request.setConnection(connectionMock);
        assertEquals(connectionMock, request.connection);
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("@Request(GET http://example.com)", request.toString());
    }
}