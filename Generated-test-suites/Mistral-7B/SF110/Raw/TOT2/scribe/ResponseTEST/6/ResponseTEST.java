import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Response;
import static org.mockito.Mockito.*;

public class ResponseTest {

    @Test
    public void testResponseConstructor() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetBody() {
        String expectedBody = "expected body";
        Response response = new Response(new ByteArrayInputStream(expectedBody.getBytes()));
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetStream() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        Response response = new Response(inputStream);
        InputStream actualStream = response.getStream();
        assertSame(inputStream, actualStream);
    }

    @Test
    public void testGetCode() {
        Response response = new Response(new ByteArrayInputStream("response body".getBytes()));
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new BasicHeaderElement[0]);
        Response response = new Response(connection);
        assertEquals(Collections.emptyMap(), response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new BasicHeaderElement[]{
                new BasicHeaderElement("Content-Type", "text/plain"),
                new BasicHeaderElement("Content-Length", "10")
        });
        Response response = new Response(connection);
        assertEquals("Content-Type", response.getHeader("Content-Type"));
        assertEquals("text/plain", response.getHeader("Content-Type"));
    }

    @Test(expected = UnknownSourceCodeException.class)
    public void testResponseConstructorWithUnknownHostException() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenThrow(new UnknownHostException());
        new Response(connection);
    }

    @Test
    public void testGetBodyWithNullStream() {
        Response response = new Response(null);
        assertNull(response.getBody());
    }

    @Test
    public void testGetBodyWithEmptyStream() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Response response = new Response(inputStream);
        assertEquals("", response.getBody());
    }

    @Test
    public void testGetHeaderWithNullName() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new BasicHeaderElement[0]);
        Response response = new Response(connection);
        assertNull(response.getHeader(null));
    }

    @Test
    public void testGetHeaderWithInvalidName() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new BasicHeaderElement[]{
                new BasicHeaderElement("Content-Type", "text/plain")
        });
        Response response = new Response(connection);
        assertNull(response.getHeader("Invalid-Name"));
    }

    @Test
    public void testGetHeaderWithMultipleValues() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new BasicHeaderElement[]{
                new BasicHeaderElement("Content-Type", "text/plain"),
                new BasicHeaderElement("Content-Type", "application/octet-stream")
        });
        Response response = new Response(connection);
        assertEquals("text/plain, application/octet-stream", response.getHeader("Content-Type"));
    }
}