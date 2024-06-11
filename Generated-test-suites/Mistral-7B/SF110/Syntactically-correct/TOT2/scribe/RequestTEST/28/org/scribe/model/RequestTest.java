import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;

public class RequestTest {

    @Test
    public void testRequestConstructor() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertFalse(request.isConnectionKeepAlive());
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNotNull(request.getHeaders());
        assertNotNull(request.getBodyParams());
        assertNotNull(request.getQuerystringParams());
    }
}