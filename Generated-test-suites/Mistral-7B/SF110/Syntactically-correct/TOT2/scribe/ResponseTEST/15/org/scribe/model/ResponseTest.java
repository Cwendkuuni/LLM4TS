import org.junit.Test;
import org.scribe.model.Response;
import static org.junit.Assert.*;

public class ResponseTest {
    @Test
    public void testGetBody() {
        Response response = new Response(new MockHttpURLConnection());
        String expectedBody = "Expected body";
        response.setBody(expectedBody);
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }
}