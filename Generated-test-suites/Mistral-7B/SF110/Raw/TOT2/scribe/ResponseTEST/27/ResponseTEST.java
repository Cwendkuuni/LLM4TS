import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Response;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(new HttpURLConnection(new URL("http://example.com")) {
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("Test body".getBytes());
            }
        });

        assertEquals("Test body", response.getBody());
    }
}