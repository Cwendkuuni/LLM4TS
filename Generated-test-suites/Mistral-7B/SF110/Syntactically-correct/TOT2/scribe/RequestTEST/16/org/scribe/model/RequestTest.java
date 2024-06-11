import org.junit.Test;
import org.scribe.model.Request;
import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void testRequestConstruction() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNull(request.payload);
        assertNull(request.bytePayload);
        assertFalse(request.connectionKeepAlive);
        assertNotNull(request.verb);
        assertNotNull(request.url);
        assertTrue(request.querystringParams.isEmpty());
        assertTrue(request.bodyParams.isEmpty());
        assertTrue(request.headers.isEmpty());
    }
}