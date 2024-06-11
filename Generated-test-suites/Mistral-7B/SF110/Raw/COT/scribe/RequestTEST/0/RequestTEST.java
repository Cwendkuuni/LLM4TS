import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeUnit;

public class RequestTest {

    @Test
    public void testRequestConstructor() {
        Request request = new Request(Verb.GET, "http://example.com");
        Assert.assertNotNull(request);
        Assert.assertEquals(Verb.GET, request.getVerb());
        Assert.assertEquals("http://example.com", request.getUrl());
        Assert.assertNull(request.getByteBodyContents());
        Assert.assertNull(request.getPayload());
        Assert.assertEquals(new HashMap<>(), request.getBodyParams());
        Assert.assertEquals(new HashMap<>(), request.getHeaders());
        Assert.assertEquals(new HashMap<>(), request.getQuerystringParams());
    }

    @Test
    public void testSendMethodCallsCreateConnectionAndDoSend() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        HttpURLConnection connection = mockHttpURLConnection();
        request.setConnection(connection);

        Response response = request.send();

        Assert.assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCode());
        verify(connection).setRequestMethod("GET");
        verify(connection).setDoOutput(false);
        verifyNoMoreInteractions(connection);
    }

    @Test(expected = OAuthException.class)
    public void testSendMethodThrowsIOException() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        doThrow(new IOException("Test IOException")).when(mockHttpURLConnection()).setRequestMethod("GET");

        request.send();
    }

    @Test
    public void testCreateConnectionAppendsParametersToQueryString() throws IOException, MalformedURLException {
        String baseUrl = "http://example.com?param1=value1";
        Map<String, String> querystringParams = new HashMap<>();
        querystringParams.put("param2", "value2");

        Request request = new Request(Verb.GET, baseUrl);
        request.addQuerystringParameter("param3", "value3");

        String expectedUrl = "http://example.com?param1=value1&param2=value2&param3=value3";
        URL url = new Request(request.getUrl()).toURL();
        String actualUrl = url.toString();

        Assert.assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void testDoSendSetsRequestMethodAndAddsHeaders() throws IOException {
        String contentType = "application/json";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", contentType);

        Request request = new Request(Verb.POST, "http://example.com");
        request.setHeaders(headers);

        HttpURLConnection connection = mockHttpURLConnection();

        request.doSend();

        verify(connection).setRequestMethod("POST");
        for (final String key : headers.keySet()) {
            String value = headers.get(key);
            verify(connection).setRequestProperty(key, value);
        }
        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testDoSendAddsBodyWhenVerbIsPUTOrPOST() throws IOException {
        String body = "{\"name\":\"John\",\"age\":30}";

        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload(body);

        HttpURLConnection connection = mockHttpURLConnection();
        byte[] byteBody = body.getBytes(Charset.forName("UTF-8"));

        request.doSend();

        verify(connection).setRequestProperty("Content-Length", String.valueOf(byteBody.length));
        verify(connection).setDoOutput(true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        connection.getOutputStream().write(byteBody);
        byte[] actualBody = outputStream.toByteArray();
        Assert.assertArrayEquals(byteBody, actualBody);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetByteBodyContentsThrowsUnsupportedEncodingException() throws IOException, UnsupportedEncodingException {
        String payload = "test payload";
        Request request = new Request();
        request.addPayload(payload);
        String unsupportedCharset = "X-UNSUPPORTED-CHARSET";

        request.getByteBodyContents();
    }

    private HttpURLConnection mockHttpURLConnection() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        return connection;
    }
}