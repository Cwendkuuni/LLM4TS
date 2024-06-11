import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    @Test
    public void testRequestConstructor() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertFalse(request.isConnectionKeepAlive());
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertEquals(Arrays.asList(), request.getQueryStringParams().values());
        assertEquals(new HashMap<>(), request.getBodyParams());
        assertEquals(new HashMap<>(), request.getHeaders());
    }

    @Test
    public void testSendWithGETRequest() throws IOException, OAuthException {
        String url = "http://example.com?foo=bar&baz=qux";
        Request request = new Request(Verb.GET, url);
        request.addQuerystringParameter("foo", "bar");
        request.addQuerystringParameter("baz", "qux");
        Request.Response response = request.send();
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getStatusMessage());
    }

    @Test(expected = OAuthException.class)
    public void testSendWithIOException() throws IOException, OAuthException {
        String url = "http://example.com/invalid-url";
        Request request = new Request(Verb.GET, url);
        request.send();
    }

    @Test
    public void testCreateConnection() throws IOException {
        String url = "http://example.com?foo=bar&baz=qux";
        Request request = new Request(Verb.GET, url);
        request.addQuerystringParameter("foo", "bar");
        request.addQuerystringParameter("baz", "qux");
        request.createConnection();
        assertNotNull(request.getConnection());
    }

    @Test
    public void testDoSendWithGETRequest() throws IOException, OAuthException {
        String url = "http://example.com?foo=bar&baz=qux";
        Request request = new Request(Verb.GET, url);
        request.addQuerystringParameter("foo", "bar");
        request.addQuerystringParameter("baz", "qux");
        Request.Response response = request.doSend();
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getStatusMessage());
    }

    @Test(expected = IOException.class)
    public void testDoSendWithIOException() throws IOException, OAuthException {
        String url = "http://example.com/invalid-url";
        Request request = new Request(Verb.GET, url);
        request.doSend();
    }

    @Test
    public void testAddHeaders() throws IOException, OAuthException {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Custom-Header", "Custom Value");
        request.addHeaders(headers);

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getRequestProperties()).thenReturn(headers);
        when(connection.getResponseCode()).thenReturn(200);

        Request.Response response = request.send();
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getStatusMessage());
        verify(connection).setRequestProperty("X-Custom-Header", "Custom Value");
    }

    @Test
    public void testAddBodyWithString() throws IOException, OAuthException {
        String url = "http://example.com";
        String body = "This is the body";
        Request request = new Request(Verb.POST, url);
        request.addBody(body);

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getDoOutput()).thenReturn(true);
        when(connection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(connection.getResponseCode()).thenReturn(200);

        request.send();
        verify(connection.getOutputStream()).write(body.getBytes());
    }

    @Test
    public void testAddBodyWithByteArray() throws IOException, OAuthException {
        String url = "http://example.com";
        byte[] body = "This is the body".getBytes();
        Request request = new Request(Verb.POST, url);
        request.addPayload(body);

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getDoOutput()).thenReturn(true);
        when(connection.setRequestProperty("Content-Length", String.valueOf(body.length))).thenReturn(true);
        when(connection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(connection.getResponseCode()).thenReturn(200);

        request.send();
        verify(connection.getOutputStream()).write(body);
    }

    @Test(expected = IOException.class)
    public void testAddBodyWithIOException() throws IOException, OAuthException {
        String url = "http://example.com";
        byte[] body = "This is the body".getBytes();
        Request request = new Request(Verb.POST, url);
        request.addPayload(body);

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getDoOutput()).thenReturn(true);
        doThrow(IOException.class).when(connection).getOutputStream();

        request.send();
    }

    @Test(expected = OAuthException.class)
    public void testAddBodyWithUnsupportedEncodingException() throws IOException, OAuthException {
        String url = "http://example.com";
        String body = "This is the body";
        Request request = new Request(Verb.POST, url);
        request.addBody(body);

        Request.Response response = request.doSend();
        assertEquals(415, response.getStatusCode());
        assertEquals("Unsupported Media Type", response.getStatusMessage());
    }

    @Test
    public void testGetByteBodyContents() throws UnsupportedEncodingException, IOException {
        String body = "This is the body";
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBody(body);

        byte[] expectedByteBody = body.getBytes();
        byte[] actualByteBody = request.getByteBodyContents();
        assertArrayEquals(expectedByteBody, actualByteBody);
    }

    @Test
    public void testGetQueryStringParams() throws MalformedURLException {
        String url = "http://example.com?foo=bar&baz=qux";
        Request request = new Request(Verb.GET, url);

        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("foo", "bar");
        expectedParams.put("baz", "qux");

        Map<String, String> actualParams = request.getQueryStringParams();
        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testAddQuerystringParameter() {
        String url = "http://example.com";
        String key = "foo";
        String value = "bar";
        Request request = new Request(Verb.GET, url);
        request.addQuerystringParameter(key, value);

        String expectedUrl = "http://example.com?" + key + "=" + value;
        assertEquals(expectedUrl, request.getUrl());
    }

    @Test
    public void testAddPayload() {
        String url = "http://example.com";
        String payload = "This is the payload";
        Request request = new Request(Verb.GET, url);
        request.addPayload(payload);

        String expectedUrl = url + "?" + URLUtils.formURLEncodeMap(Collections.singletonMap("payload", payload)).toString();
        assertEquals(expectedUrl, request.getUrl());
    }

    @Test
    public void testSetConnectTimeout() throws IOException {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getConnectTimeout()).thenReturn(connection.getConnectTimeout() + 5000);

        request.send();
        verify(connection).setConnectTimeout(10000);
    }

    @Test
    public void testSetReadTimeout() throws IOException {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getReadTimeout()).thenReturn(connection.getReadTimeout() + 5000);

        request.send();
        verify(connection).setReadTimeout(10000);
    }

    @Test
    public void testSetCharset() {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        request.setCharset("UTF-8");

        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);
        request.setConnectionKeepAlive(true);

        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testToString() {
        String url = "http://example.com";
        Request request = new Request(Verb.GET, url);

        assertEquals("@Request(GET http://example.com)", request.toString());
    }
}