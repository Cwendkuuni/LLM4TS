import org.junit.Test;
import org.scribe.model.Response;
import static org.junit.Assert.*;

public class ResponseTest {
    @Test
    public void testGetBody() {
        Response response = new Response(new HttpURLConnection(new URL("http://example.com"))) {
            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream("Test body".getBytes());
            }
        };
        String actualBody = response.getBody();
        assertEquals("Test body", actualBody);
    }
}