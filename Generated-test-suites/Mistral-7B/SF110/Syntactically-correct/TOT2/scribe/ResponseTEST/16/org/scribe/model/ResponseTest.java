import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    @Test
    public void testResponseWithSuccessfulStatus() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(Mockito.mock(InputStream.class));
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        String expectedBody = "Successful body";
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(expectedBody.getBytes()));
        assertEquals(expectedBody, response.getBody());
    }

    // Test other methods

    @Test
    public void testResponseWithErrorStatus() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(500);
        Response response = new Response(connection);
        assertEquals(500, response.getCode());
    }

    @Test(expected = UnknownHostException.class)
    public void testResponseWithNullConnection() {
        Response response = new Response(null);
    }

    @Test
    public void testResponseWithEmptyBody() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        Response response = new Response(connection);
        assertEquals("", response.getBody());
    }

    @Test
    public void testResponseWithNullNameForHeader() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(new HashMap<>());
        Response response = new Response(connection);
        assertEquals(null, response.getHeader(null));
    }

    // Test edge cases and exception handling test cases here
}