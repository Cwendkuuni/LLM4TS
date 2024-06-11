import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Response;
import java.net.*;

public class ResponseTest {
    @Test
    public void testResponseConstruction() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            Response response = new Response(connection);
            Assert.assertEquals(200, response.getCode());
            Assert.assertNull(response.getBody()); // Check if body is null initially
            // Test headers
            Assert.assertNotNull(response.getHeaders());
            Assert.assertNotNull(response.getHeader("Content-Type"));
            // Test stream
            response.parseBodyContents(); // Parse body to make sure it's not empty
            Assert.assertNotNull(response.getStream());
            connection.disconnect();
        } finally {
            connection.disconnect();
        }
    }
}