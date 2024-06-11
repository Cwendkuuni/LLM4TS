import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Response;
import java.io.ByteArrayInputStream;

public class ResponseTest {

    @Test
    public void testGetBody() {
        String expectedBody = "This is the expected body";
        Response response = new Response(new MockURLConnection(new URL("http://example.com"), 200));
        response.parseBodyContents();
        response.setBody(expectedBody);
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }
}