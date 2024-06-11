import org.junit.Assert;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    @Test
    public void testRequestWithGETVerbAndEmptyQueryString() {
        final Request request = new Request(Verb.GET, "http://example.com");
        try {
            final Response response = request.send();
            Assert.assertEquals(200, response.getStatusCode());
            Assert.assertEquals("GET", request.getVerb().name());
            Assert.assertNull(request.getBytePayload());
            Assert.assertEquals(0, request.getHeaders().size());
            Assert.assertEquals(0, request.getQuerystringParams().size());
            Assert.assertEquals("http://example.com", request.getSanitizedUrl());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRequestWithGETVerbAndQueryStringParams() {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("param1", "value1");
        queryParams.put("param2", "value2");

        final Request request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
        request.addQuerystringParameters(queryParams);

        try {
            final Response response = request.send();
            Assert.assertEquals(200, response.getStatusCode());
            final Map<String, String> expectedQueryParams = new HashMap<>(Arrays.asList("param1", "value1", "param2", "value2"));
            Assert.assertEquals(expectedQueryParams, request.getQueryStringParams());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRequestWithPUTVerbAndBody() {
        final String bodyContent = "test content";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            writer.write(bodyContent);
        }

        final byte[] byteBody = outputStream.toByteArray();

        final Request request = new Request(Verb.PUT, "http://example.com");
        request.addBodyParameter("param", "value");
        request.addPayload(bodyContent);
        request.setPayload(byteBody);

        try {
            final Response response = request.send();
            Assert.assertEquals(200, response.getStatusCode());
            final String responseBody = new String(response.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            Assert.assertEquals(bodyContent, responseBody);
            Assert.assertEquals(1, request.getHeaders().size());
            Assert.assertEquals("Content-Length:" + byteBody.length, request.getHeaders().get("Content-Length"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRequestWithInvalidVerb() {
        final Request request = new Request(null, "http://example.com");

        try {
            request.send();
            Assert.fail();
        } catch (OAuthException e) {
            // Expected exception
        }
    }

    @Test
    public void testRequestWithNullUrl() {
        final Request request = new Request(Verb.GET, null);

        try {
            request.send();
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testRequestWithIOExceptionDuringConnectionCreation() {
        final String url = "http://example.com:12345/";
        final Request request = new Request(Verb.GET, url);

        try {
            request.send();
            Assert.fail();
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testRequestWithConnectionTimeout() throws IOException, InterruptedException {
        final String url = "http://example.com/";
        final int connectionTimeout = 50;
        final TimeUnit connectionTimeoutUnit = TimeUnit.MILLISECONDS;

        final Request request = new Request(Verb.GET, url);
        request.setConnectTimeout(connectionTimeout, connectionTimeoutUnit);

        final HttpURLConnection connection = mockConnection(connectionTimeout);
        request.setConnection(connection);

        try {
            request.send();
            Assert.fail();
        } catch (OAuthException e) {
            // Expected exception
        }
    }

    @Test
    public void testRequestWithReadTimeout() throws IOException, InterruptedException {
        final String url = "http://example.com/";
        final int readTimeout = 50;
        final TimeUnit readTimeoutUnit = TimeUnit.MILLISECONDS;

        final Request request = new Request(Verb.GET, url);
        request.setReadTimeout(readTimeout, readTimeoutUnit);

        final HttpURLConnection connection = mockConnection(500);
        request.setConnection(connection);

        final InputStream inputStream = new ByteArrayInputStream("test content".getBytes(StandardCharsets.UTF_8));
        connection.setInputStream(inputStream);

        try {
            request.send();
            Assert.fail();
        } catch (OAuthException e) {
            // Expected exception
        }
    }

    private HttpURLConnection mockConnection(int responseCode) throws IOException {
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(responseCode).when(connection).getResponseCode();
        return connection;
    }

    private static byte[] getByteBodyContents(String payload) {
        try {
            return payload.getBytes(StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}