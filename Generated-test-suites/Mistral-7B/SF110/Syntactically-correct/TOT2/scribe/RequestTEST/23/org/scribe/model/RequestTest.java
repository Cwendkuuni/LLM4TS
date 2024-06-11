import org.junit.Test;
import org.scribe.model.Request;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testRequestConstructor() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getBytePayload());
        assertNull(request.getPayload());
        assertTrue(request.getQueryStringParams().isEmpty());
        assertTrue(request.getBodyParams().isEmpty());
        assertTrue(request.getHeaders().isEmpty());
    }
}