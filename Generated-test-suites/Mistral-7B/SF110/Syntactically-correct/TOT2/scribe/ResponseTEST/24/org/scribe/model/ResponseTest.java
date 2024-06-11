import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Response;
import java.net.HttpURLConnection;
import java.io.*;
import java.util.*;
import static org.mockito.Mockito.*;;

public class ResponseTest {

    @Test
    public void testResponseConstruction() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetBody() {
        String expectedBody = "Expected body";
        Response response = new Response(mock(HttpURLConnection.class));
        response.setBody(expectedBody);
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetStream() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Expected stream content".getBytes());
        Response response = new Response(mock(HttpURLConnection.class)) {
            @Override
            public InputStream getStream() {
                return inputStream;
            }
        };
        assertEquals(inputStream, response.getStream());
    }

    @Test
    public void testResponseWithErrorStatusCode() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(500);
        Response response = new Response(connection);
        assertEquals(500, response.getCode());
    }

    @Test(expected = UnknownHostException.class)
    public void testResponseWithUnknownHostException() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenThrow(new UnknownHostException());
        Response response = new Response(connection);
    }

    @Test
    public void testGetEmptyHeaders() {
        Response response = new Response(mock(HttpURLConnection.class));
        assertEquals(Collections.emptyMap(), response.getHeaders());
    }

    @Test
    public void testGetHeaderWithNullValue() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", null);
        Response response = new Response(mock(HttpURLConnection.class)) {
            @Override
            public Map<String, String> parseHeaders(HttpURLConnection conn) {
                return headers;
            }
        };
        assertNull(response.getHeader("Content-Type"));
    }

    @Test
    public void testGetHeaderWithNullKey() {
        Map<String, String> headers = new HashMap<>();
        headers.put(null, "Expected value");
        Response response = new Response(mock(HttpURLConnection.class)) {
            @Override
            public Map<String, String> parseHeaders(HttpURLConnection conn) {
                return headers;
            }
        };
        assertNull(response.getHeader(null));
    }

    // Add test cases for other methods.
}