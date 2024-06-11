import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testResponseConstructor() throws IOException {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Response response = new Response(connection);
        // Basic functionality check
        Assert.assertEquals(200, response.getCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getHeaders());
        // Exception handling scenario
        connection.disconnect();
        connection.setInstanceFollowRedirects(false);
        URL invalidUrl = new URL("http://nonexistent.com");
        connection = (HttpURLConnection) invalidUrl.openConnection();
        Response exceptionResponse = null;
        try {
            exceptionResponse = new Response(connection);
        } catch (UnknownHostException e) {
            // Check exception is thrown
            Assert.assertNotNull(e);
            Assert.assertNull(exceptionResponse);
        }
    }

    @Test
    public void testParseBodyContents() throws IOException {
        String expectedBody = "Hello World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedBody.getBytes());
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.getOutputStream().write("{\"message\":\"" + expectedBody + "\"}".getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        Response response = new Response(connection);
        String actualBody = response.getBody();
        // Basic functionality check
        Assert.assertEquals(expectedBody, actualBody);
        // Edge case: empty body
        Response emptyBodyResponse = new Response(new URL("http://example.com"));
        Assert.assertEquals(EMPTY, emptyBodyResponse.getBody());
    }

    @Test
    public void testParseHeaders() throws IOException {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.getOutputStream().write("{\"message\":\"Hello World!\"}".getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        Response response = new Response(connection);
        Map<String, String> actualHeaders = response.getHeaders();
        // Basic functionality check
        Assert.assertEquals(expectedHeaders, actualHeaders);
        // Edge case: empty headers
        Response emptyHeadersResponse = new Response(new URL("http://example.com"));
        Assert.assertEquals(Collections.emptyMap(), emptyHeadersResponse.getHeaders());
    }

    @Test
    public void testWasSuccessful() throws IOException {
        URL url1 = new URL("http://example.com");
        Response response1 = new Response(new URL("http://example.com"));
        Assert.assertTrue(response1.wasSuccessful());
        URL url2 = new URL("http://example.com/error");
        Response response2 = new Response(new URL("http://example.com/error"));
        Assert.assertFalse(response2.wasSuccessful());
        // Edge case: invalid status code
        Response response3 = new Response(new URL("http://example.com"));
        response3.setCode(-1);
        Assert.assertFalse(response3.wasSuccessful());
    }

    @Test
    public void testGetBody() throws IOException {
        String expectedBody = "Hello World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedBody.getBytes());
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.getOutputStream().write("{\"message\":\"" + expectedBody + "\"}".getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        Response response = new Response(connection);
        String actualBody = response.getBody();
        // Basic functionality check
        Assert.assertEquals(expectedBody, actualBody);
        // Edge case: null body
        Response nullBodyResponse = new Response(new URL("http://example.com"));
        Assert.assertEquals(null, nullBodyResponse.getBody());
    }

    @Test
    public void testGetStream() throws IOException {
        String expectedBody = "Hello World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedBody.getBytes());
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.getOutputStream().write("{\"message\":\"" + expectedBody + "\"}".getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        Response response = new Response(connection);
        InputStream actualStream = response.getStream();
        // Basic functionality check
        Assert.assertEquals(inputStream, actualStream);
        // Edge case: null stream
        Response nullStreamResponse = new Response(new URL("http://example.com"));
        Assert.assertNull(nullStreamResponse.getStream());
    }

    @Test
    public void testGetCode() throws IOException {
        URL url1 = new URL("http://example.com");
        Response response1 = new Response(new URL("http://example.com"));
        Assert.assertEquals(200, response1.getCode());
        URL url2 = new URL("http://example.com/error");
        Response response2 = new Response(new URL("http://example.com/error"));
        Assert.assertEquals(404, response2.getCode());
        // Edge case: invalid status code
        Response response3 = new Response(new URL("http://example.com"));
        response3.setCode(-1);
        Assert.assertEquals(-1, response3.getCode());
    }

    @Test
    public void testGetHeaders() throws IOException {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.getOutputStream().write("{\"message\":\"Hello World!\"}".getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        Response response = new Response(connection);
        Map<String, String> actualHeaders = response.getHeaders();
        // Basic functionality check
        Assert.assertEquals(expectedHeaders, actualHeaders);
        // Edge case: empty headers
        Response emptyHeadersResponse = new Response(new URL("http://example.com"));
        Assert.assertEquals(Collections.emptyMap(), emptyHeadersResponse.getHeaders());
    }

    @Test
    public void testGetHeader() throws IOException {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.getOutputStream().write("{\"message\":\"Hello World!\"}".getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        Response response = new Response(connection);
        String actualHeader = response.getHeader("Content-Type");
        // Basic functionality check
        Assert.assertEquals("application/json", actualHeader);
        // Edge case: null header
        Response nullHeaderResponse = new Response(new URL("http://example.com"));
        Assert.assertNull(nullHeaderResponse.getHeader(null));
    }
}