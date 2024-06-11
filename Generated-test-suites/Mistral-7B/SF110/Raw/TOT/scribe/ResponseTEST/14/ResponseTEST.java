import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Response;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testResponseSuccess() {
        // Given
        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
        Mockito.when(connectionMock.getInputStream()).thenReturn(Mockito.mock(InputStream.class));
        Map<String, List<String>> headerFieldsMock = Mockito.mock(Map.class);
        Mockito.when(connectionMock.getHeaderFields()).thenReturn(headerFieldsMock);

        // When
        Response response = new Response(connectionMock);

        // Then
        Assert.assertEquals(200, response.getCode());
        Assert.assertNotNull(response.getStream());
        Assert.assertNotNull(response.getHeaders());
        Assert.assertNull(response.getBody());
    }

    @Test
    public void testResponseFailure() {
        // Given
        Response response = new Response(null);

        // Then
        Assert.assertEquals(404, response.getCode());
        Assert.assertNull(response.getBody());
        Assert.assertNull(response.getStream());
        Assert.assertNotNull(response.getHeaders());
    }

    @Test
    public void testResponseWithBody() {
        // Given
        HttpURLConnection connectionMock = Mockito.mock(HttpURLConnection.class);
        Mockito.when(connectionMock.getResponseCode()).thenReturn(200);
        Mockito.when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream("Test body".getBytes()));
        Map<String, List<String>> headerFieldsMock = new HashMap<>();
        headerFieldsMock.put("Content-Type", new ArrayList<>(){
            {
                add("text/plain");
            }
        });
        Mockito.when(connectionMock.getHeaderFields()).thenReturn(headerFieldsMock);

        // When
        Response response = new Response(connectionMock);

        // Then
        Assert.assertEquals(200, response.getCode());
        Assert.assertNotNull(response.getStream());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals("Test body", response.getBody());
        Assert.assertNotNull(response.getHeaders());
        Assert.assertEquals("text/plain", response.getHeader("Content-Type"));
    }
}