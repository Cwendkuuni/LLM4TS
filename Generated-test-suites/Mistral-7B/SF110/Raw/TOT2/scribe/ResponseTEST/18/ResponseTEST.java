import static org.junit.Assert.*;

public class ResponseTest {
    // ...
    
    @Test
    public void testGetBody() {
        String expectedBody = "Expected body";
        Response response = new Response(new URL("http://example.com").openConnection());
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }
}