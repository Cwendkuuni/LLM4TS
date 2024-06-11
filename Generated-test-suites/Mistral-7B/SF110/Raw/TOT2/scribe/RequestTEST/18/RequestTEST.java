import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;
import org.scribe.model.*;
import org.scribe.exceptions.*;

public class RequestTest {

    @Test
    public void testSend() throws IOException, OAuthException {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        Response response = request.send();
        assertTrue(response != null);
        verify(request).createConnection();
        verify(request).doSend();
    }

    @Test
    public void testSend_EdgesCasesAndExceptions() {
        // Empty URL
        Verb verb = Mockito.mock(Verb.class);
        String url = "";
        Request requestEmptyUrl = new Request(verb, url);
        assertThrows(IllegalArgumentException.class, requestEmptyUrl::send);

        // Null verb
        url = "http://example.com";
        Request requestNullVerb = new Request(null, url);
        assertThrows(NullPointerException.class, requestNullVerb::send);

        // Malformed URL
        String malformedUrl = "http://example.com:8080/?a=b&c=d:e";
        Request requestMalformedUrl = new Request(verb, malformedUrl);
        assertThrows(MalformedURLException.class, requestMalformedUrl::send);

        // IOException while creating connection
        doThrow(IOException.class).when(request).createConnection();
        Request requestIOExceptionCreate = new Request(verb, url);
        assertThrows(OAuthException.class, requestIOExceptionCreate::send);

        // OAuthException while sending request
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenThrow(new IOException());
        Request requestOAuthException = new Request(verb, url);
        request.setConnection(connection);
        assertThrows(OAuthException.class, request::send);

        // Null headers map
        Map<String, String> headersNull = null;
        Request requestNullHeaders = new Request(verb, url);
        requestNullHeaders.setHeaders(headersNull);
        assertThrows(NullPointerException.class, requestNullHeaders::send);

        // Empty bodyParams map
        Map<String, String> bodyParamsEmpty = new HashMap<>();
        Request requestEmptyBodyParams = new Request(verb, url);
        requestEmptyBodyParams.setBodyParams(bodyParamsEmpty);
        assertDoesNotThrow(requestEmptyBodyParams::send);

        // Invalid payload charset
        String payloadInvalidCharset = "Invalid charset payload";
        Request requestInvalidPayloadCharset = new Request(verb, url);
        requestInvalidPayloadCharset.addPayload(payloadInvalidCharset);
        assertThrows(OAuthException.class, requestInvalidPayloadCharset::send);

        // Connection timeout
        request.setConnectTimeout(1, TimeUnit.SECONDS);
        assertThrows(SocketTimeoutException.class, request::send);

        // Read timeout
        request.setReadTimeout(1, TimeUnit.SECONDS);
        assertThrows(SocketTimeoutException.class, request::send);
    }

    @Test
    public void testAddHeader() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        String headerName = "Content-Type";
        String headerValue = "application/json";
        request.addHeader(headerName, headerValue);
        Map<String, String> headers = request.getHeaders();
        assertEquals(headerValue, headers.get(headerName));
    }

    @Test
    public void testAddBodyParameter() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        String key = "key";
        String value = "value";
        request.addBodyParameter(key, value);
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(value, bodyParams.get(key));
    }

    @Test
    public void testAddQuerystringParameter() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com?param=value";
        Request request = new Request(verb, url);
        String key = "param";
        String value = "newValue";
        request.addQuerystringParameter(key, value);
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals(value, queryStringParams.get(key));
    }

    @Test
    public void testAddPayload() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        String payload = "payload";
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testGetQueryStringParams() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com?param1=value1&param2=value2";
        Request request = new Request(verb, url);
        Map<String, String> expected = new HashMap<>();
        expected.put("param1", "value1");
        expected.put("param2", "value2");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals(expected, queryStringParams);
    }

    @Test
    public void testGetBodyParams() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        String key = "key";
        String value = "value";
        request.addBodyParameter(key, value);
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(value, bodyParams.get(key));
    }

    @Test
    public void testGetUrl() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        assertEquals(url, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com?param=value&port=8080";
        Request request = new Request(verb, url);
        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        String bodyContents = "bodyContents";
        Request request = new Request(verb, url);
        request.addPayload(bodyContents);
        assertEquals(bodyContents, request.getBodyContents());
    }

    @Test
    public void testGetVerb() {
        Verb verb = Mockito.mock(Verb.class);
        Verb expected = Verb.GET;
        Request request = new Request(expected, "http://example.com");
        assertEquals(expected, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Request request = new Request(verb, url);
        request.setHeaders(headers);
        assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testGetCharset() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.setConnectTimeout(1, TimeUnit.MINUTES);
        assertEquals(1, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.setReadTimeout(1, TimeUnit.MINUTES);
        assertEquals(1, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        String charset = "UTF-8";
        Request request = new Request(verb, url);
        request.setCharset(charset);
        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testToString() {
        Verb verb = Mockito.mock(Verb.class);
        String url = "http://example.com";
        Request request = new Request(verb, url);
        assertEquals("@Request(GET http://example.com)", request.toString());
    }
}