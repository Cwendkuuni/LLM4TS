import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testResponseInstance() throws IOException {
        Response response = new Response(createConnectionWithSuccessStatus());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getHeaders());
    }

    @Test(expected = IOException.class)
    public void testResponseInstanceWithException() throws IOException {
        Response response = new Response(createConnectionWithUnknownHostException());
        Assert.assertNotNull(response); // Allow response object creation as an exception is expected
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String expectedBody = "Expected body content";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedBody.getBytes());
        Response response = new Response(createConnectionWithSuccessStatus(expectedBody, inputStream));
        Assert.assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testParseHeaders() throws IOException {
        String key = "Content-Type";
        String value = "application/json";

        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put(key, new ArrayList<>(Collections.nSingletonList(value)));
        HttpURLConnection connection = createConnectionWithSuccessStatus();
        connection.setRequestProperty("Content-Type", value);
        connection.setRequestProperty("X-Custom-Header", "CustomValue");

        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();
        Assert.assertEquals(headerFields.get(key).get(0), headers.get(key));
        Assert.assertNull(headers.get("X-Custom-Header"));
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(createConnectionWithSuccessStatus());
        Assert.assertTrue(response.wasSuccessful());

        response = new Response(createConnectionWithErrorStatus());
        Assert.assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetCode() throws IOException {
        Response response = new Response(createConnectionWithSuccessStatus());
        Assert.assertEquals(200, response.getCode());

        response = new Response(createConnectionWithErrorStatus());
        Assert.assertEquals(404, response.getCode());
    }

    @Test
    public void testGetHeader() throws IOException {
        String key = "Content-Type";
        String value = "application/json";

        HttpURLConnection connection = createConnectionWithSuccessStatus();
        connection.setRequestProperty("Content-Type", value);

        Response response = new Response(connection);
        String headerValue = response.getHeader(key);
        Assert.assertEquals(value, headerValue);

        key = "NonExistent-Header";
        headerValue = response.getHeader(key);
        Assert.assertNull(headerValue);
    }

    private HttpURLConnection createConnectionWithSuccessStatus() throws IOException {
        return createConnectionWithSuccessStatus(null, null);
    }

    private HttpURLConnection createConnectionWithSuccessStatus(String body, InputStream inputStream) throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.setRequestProperty("Accept", "application/json");
        connection.setInstanceFollowRedirects(false);

        if (body != null) {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
            try (OutputStream output = connection.getOutputStream()) {
                output.write(body.getBytes());
            }
        }

        if (inputStream != null) {
            connection.setFixedLengthStreamingMode(inputStream.available());
            try (OutputStream output = connection.getOutputStream()) {
                StreamUtils.copy(inputStream, output);
            }
        }

        connection.connect();
        return connection;
    }

    private HttpURLConnection createConnectionWithUnknownHostException() throws IOException {
        URL url = new URL("http://non-existent-host.com");
        return (HttpURLConnection) url.openConnection();
    }
}