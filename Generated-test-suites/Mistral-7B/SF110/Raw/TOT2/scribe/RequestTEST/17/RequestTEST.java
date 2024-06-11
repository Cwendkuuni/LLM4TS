import org.junit.Test;
import org.scribe.model.Request;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testRequestConstruction() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertFalse(request.isConnectionKeepAlive());
    }
}