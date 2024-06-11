import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import static org.junit.Assert.*;

public class RequestTest {

    Request request;

    @Before
    public void setup() {
        request = new Request(Verb.GET, "http://example.com");
    }

    @Test
    public void testRequestConstructor() {
        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getBytePayload());
        assertFalse(request.isConnectionKeepAlive());
    }
}